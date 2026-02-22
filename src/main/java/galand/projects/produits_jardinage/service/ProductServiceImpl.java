package galand.projects.produits_jardinage.service;

import galand.projects.produits_jardinage.dto.CreateProductDTO;
import galand.projects.produits_jardinage.dto.ProductDTO;
import galand.projects.produits_jardinage.entity.Product;
import galand.projects.produits_jardinage.entity.ProductCategory;
import galand.projects.produits_jardinage.exception.DuplicateProductException;
import galand.projects.produits_jardinage.exception.InsufficientStockException;
import galand.projects.produits_jardinage.exception.InvalidPriceException;
import galand.projects.produits_jardinage.exception.ProductNotFoundException;
import galand.projects.produits_jardinage.mapper.ProductMapper;
import galand.projects.produits_jardinage.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private static final Integer LOW_STOCK_THRESHOLD = 10;
    private static final BigDecimal MAX_DISCOUNT = new BigDecimal(50);


    /**
     * Creer un produit
     *
     * @param createDTO les donnees du produit
     * @return le produit cree
     * @throws DuplicateProductException si le produit existe deja
     * @throws InvalidPriceException     si le prix est invalide
     */
    @Override
    @Transactional
    public ProductDTO createProduct(CreateProductDTO createDTO) throws DuplicateProductException, InvalidPriceException{
        log.debug("Creation d'un nouveau produit: "+createDTO.getName());

        //On verifie le produit n'existe pas deja dans notre base de donnees
        if(productRepository.existsByNameIgnoreCase(createDTO.getName()))
        {
            log.error("Produit deja existant: "+createDTO.getName());
            throw  new DuplicateProductException(createDTO.getName());
        }

        //On verifie le prix est valide
        validatePrice(createDTO.getPrice());

        //Conversion et sauvegarde
        Product product = productMapper.toEntityFromCreate(createDTO);
        Product savedProduct = productRepository.save(product);

        log.info("Produit cree avec succes - ID: {}", savedProduct.getId());

        return productMapper.toDto(savedProduct);
    }

    /**
     * Recherche un produit par son id
     *
     * @param id l'id du produit
     * @return le produit s'il existe
     * @throws ProductNotFoundException si le produit avec cet id n'existe pas
     */
    @Override
    public ProductDTO getProductByID(Integer id) throws ProductNotFoundException, NullPointerException{

        log.debug("Recherche du produit avec l'ID:{} ", id);

        Product product = productRepository.findById(id).orElseThrow(()->new ProductNotFoundException(id));
        log.info("Le produit avec pour ID {} a ete trouve: {}", id, product.getName());
        return productMapper.toDto(product);
    }

    /**
     * Liste tous les produits actifs
     *
     * @return la liste des produits actifs
     */
    @Override
    public List<ProductDTO> getAllActiveProduct() {

        log.debug("Recherche de tous les produits actifs");

        List<Product> activeProduct = productRepository.findByActiveTrue();

        log.info("Tous les produits actifs ont ete trouves");

        return productMapper.toDtosList(activeProduct);
    }

    /**
     * Recherche les produits par categorie
     *
     * @param category la categorie des produits recherches
     * @return la liste des produits trouves
     */
    @Override
    public List<ProductDTO> getProducstByCategory(ProductCategory category) throws NullPointerException {
        log.debug("Recherche des produits de categorie: {}", category);

        if(category == null)
        {
            log.error("Objet null");
            throw new NullPointerException("La categorie est \"null\"");
        }

        List<Product> productList = productRepository.findByCategory(category);
        log.info("Les produits de categorie: {} ont ete trouve", category);

        return productMapper.toDtosList(productList);
    }

    /**
     * Met a jour les donnees d'un produit
     *
     * @param id l'id du produit dont on veut mettre les informations a jour
     * @return le  produit mis a jour
     * @throws ProductNotFoundException si le produit que l'on veut mettre a jour
     *                                  n'existe pas
     */
    @Override
    @Transactional
    public ProductDTO updateProduct(Integer id, ProductDTO productDTO) throws ProductNotFoundException, InvalidPriceException {

        log.debug("Mise a jour du produit avec pour ID - {}", id);
        Product product = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException(id));


        validatePrice(product.getPrice());


        //mise a jour en utilisant le mapper
        productMapper.updateEntityFromDto(productDTO, product);

        Product updatedProduct = productRepository.save(product);
        log.info("Mise a jour du produit ID {}, termine avec succes", id);

        return productMapper.toDto(updatedProduct);
    }

    /**
     * Supprime un produit de notre base de donnees
     *
     * @param id du produit a supprimer
     * @throws ProductNotFoundException si le produit a supprimer n'existe pas
     */
    @Override
    @Transactional
    public void deleteProduct(Integer id) throws ProductNotFoundException{
        log.debug("Suppresion logique du produit(desactivation) du produit avec pour ID: {}", id);
        Product product = productRepository.findById(id).orElseThrow(()->new ProductNotFoundException(id));

        product.setActive(false);
        productRepository.save(product);

        log.info("Produit desactive avec succes ");

    }

    /**
     * Met a jour le stock d'un produit soit l'incremente, soit le decremente
     *
     * @param id       l'id du produit dont on veut ajuster le stock
     * @param quantity la quantite de produit a ajuster
     * @return le produit avec le nouveau stock
     * @throws ProductNotFoundException   si le produit n'existe pas
     * @throws InsufficientStockException si on veut decrementer et que la
     *                                    quantite restante est insuffisante
     */
    @Override
    @Transactional
    public ProductDTO adjustStock(Integer id, Integer quantity) throws ProductNotFoundException, InsufficientStockException {

        log.debug("Ajustement du stock du produit ID: {} de {} unites", id, quantity);

        Product product = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException(id));
        int currentStock = product.getStock();
        int newStock = currentStock + quantity;

       if (quantity > 0)
       {
           //Ajustement du stock: incrementation
           product.setStock(newStock);
           log.info("Augmentation du stock reussie pour le produit: {}", product.getName());
       }
       else if (quantity < 0)
       {
           //Ajustement du stock: decrementation
           //On verifie si le stock est suffisant
           quantity = Math.abs(quantity);

           if (currentStock < quantity)
           {
               log.error("Stock insuffisant pour le produit: {}. Stock disponible : {}. Stock demande : {}", product.getName(), currentStock, quantity);
               throw new InsufficientStockException(product.getName(), quantity, currentStock);
           }

           product.setStock(newStock);
           log.info("Dimunition du stock reussi, pour le produit: {}", product.getName());
       }


        //Alerte si produit en rupture
        if (newStock > 0 && newStock <= LOW_STOCK_THRESHOLD)
        {
            log.warn("ALERTE: Stock faible pour le produit {} - stock actuel: {}",product.getName(), product.getStock());
        }

        //Desactiver le produit si en rupture de stock
        if (newStock == 0)
        {
            product.setActive(false);
            log.info("Rupture de stock pour le produit: {}", product.getName());
        }

        // on enregistre les modifications dans la base de donnees
        Product updatedProduct = productRepository.save(product);
        log.info("Nouveau stock pour {}: {}", updatedProduct.getName(), updatedProduct.getStock());

        return productMapper.toDto(updatedProduct);
    }

    /**
     * Applique une remise sur un produit
     *
     * @param id                 l'id du produit dont on veut appliquer la remise
     * @param discountPercentage le pourcentage de la remise
     * @return le produit avec le nouveau prix
     * @throws ProductNotFoundException si le produit n'existe pas
     * @throws InvalidPriceException    si la remise est invalide
     */
    @Override
    @Transactional
    public ProductDTO applyDiscount(Integer id, BigDecimal discountPercentage) throws ProductNotFoundException, InvalidPriceException{

        log.debug("Application d'une remise de {}% au produit - ID {}", discountPercentage, id);

        Product product = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException(id));

        //on verifie si le pourcentage de remise est valide cad superieur a 0 et inferieure a 50
        if (discountPercentage.compareTo(BigDecimal.ZERO) < 0 || discountPercentage.compareTo(MAX_DISCOUNT) >0 )
        {
            log.error("Le pourcentage de remise est invalide: {}%", discountPercentage);
            throw new InvalidPriceException(String.format("Le pourcentage de remise doit etre positif et inferieur a %f ", MAX_DISCOUNT.setScale(2, RoundingMode.HALF_UP)));
        }

        //On recupere le prix du produit
        BigDecimal price = product.getPrice();

        //On calcule le montant de la remise
        BigDecimal discountAmount = price.multiply(discountPercentage.divide(new BigDecimal(100),4, RoundingMode.HALF_UP));

        //On calcule le nouveau prix du produit
        BigDecimal newPrice = price.subtract(discountAmount);

        //On met a jour le prix du produit
        product.setPrice(newPrice);

        //On enregistre en base de donnees
        Product updatedProduct = productRepository.save(product);

        log.info("Remise de {}% appliquee avec succes au produit - {}. Ancien prix {} XAF- Nouveau prix {} XAF", discountPercentage, product.getName(), price, newPrice);
        return productMapper.toDto(updatedProduct);
    }

    /**
     * Recherche la liste des produits en rupture de stock
     *
     * @return la liste des produits en rupture de stock
     */
    @Override
    public List<ProductDTO> getOutOfStockProducts() {
        log.debug("Recherche des produits en rupture de stock");

        List<Product> outOfStockProducts = productRepository.findLowStockProducts(0);

        log.info("{} produits en rupture de stock", outOfStockProducts.size());

        return productMapper.toDtosList(outOfStockProducts);
    }

    /**
     * Recherche les produits avec stock faible
     *
     * @param threshold le seuil de stock
     * @return les produits avec stock faible
     */
    @Override
    public List<ProductDTO> getLowStockProducts(Integer threshold) {
        log.debug("Recherche des produits avec un stock faible");

        List<Product> lowStockProducts = productRepository.findLowStockProducts(LOW_STOCK_THRESHOLD);

        log.info("{} produits avec un stock faible", threshold);

        return productMapper.toDtosList(lowStockProducts);
    }


    /**
     * Fonction de validation du prix
     * */
    public void validatePrice(BigDecimal price) throws InvalidPriceException
    {
        log.debug("Validation du prix du produit");
        if(price == null || price.compareTo(BigDecimal.ZERO) <= 0)
        {
            log.error("Prix invalide: {}", price);
            throw new InvalidPriceException("Prix invalide: Le prix doit etre strictement positif");
        }
        log.info("Prix du produit valide");
    }
}
