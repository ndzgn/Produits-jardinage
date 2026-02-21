package galand.projects.produits_jardinage.service;

import galand.projects.produits_jardinage.dto.CreateProductDTO;
import galand.projects.produits_jardinage.dto.ProductDTO;
import galand.projects.produits_jardinage.entity.ProductCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    /**
     * Creer un produit
     * @param createDTO les donnees du produit
     * @return le produit cree
     * @throws galand.projects.produits_jardinage.exception.DuplicateProductException si le produit existe deja
     * @throws galand.projects.produits_jardinage.exception.InvalidPriceException si le prix est invalide
     * */
    ProductDTO createProduct(CreateProductDTO createDTO);


    /**
     * Recherche un produit par son id
     * @param id l'id du produit
     * @return le produit s'il existe
     * @throws galand.projects.produits_jardinage.exception.ProductNotFoundException si le produit avec cet id n'existe pas
     * */
    ProductDTO getProductByID(int id);

    /**
     * Liste tous les produits actifs
     * @return la liste des produits actifs
     * */
    List<ProductDTO> getAllActiveProduct();


    /**
     * Recherche les produits par categorie
     * @param category la categorie des produits recherches
     * @return la liste des produits trouves
     * */
    List<ProductDTO> getProducstByCategory(ProductCategory category);

    /**
     * Met a jour les donnees d'un produit
     * @param id l'id du produit dont on veut mettre les informations a jour
     * @return le  produit mis a jour
     * @throws galand.projects.produits_jardinage.exception.ProductNotFoundException si le produit que l'on veut mettre a jour
     * n'existe pas
     * */
    ProductDTO updateProduct(int id);


    /**
     * Supprime un produit de notre base de donnees
     * @param id du produit a supprimer
     * @throws galand.projects.produits_jardinage.exception.ProductNotFoundException si le produit a supprimer n'existe pas
     * */
    void deleteProduct(int id);

    /**
     * Met a jour le stock d'un produit soit l'incremente, soit le decremente
     * @param id l'id du produit dont on veut ajuster le stock
     * @param quantity la quantite de produit a ajuster
     * @return le produit avec le nouveau stock
     * @throws galand.projects.produits_jardinage.exception.ProductNotFoundException si le produit n'existe pas
     * @throws galand.projects.produits_jardinage.exception.InsufficientStockException si on veut decrementer et que la
     * quantite restante est insuffisante
     * */
    ProductDTO adjustStock(int id, Integer quantity);


    /**
     * Applique une remise sur un produit
     * @param id l'id du produit dont on veut appliquer la remise
     * @param discountPercentage le pourcentage de la remise
     * @return le produit avec le nouveau prix
     * @throws galand.projects.produits_jardinage.exception.ProductNotFoundException si le produit n'existe pas
     * @throws galand.projects.produits_jardinage.exception.InvalidPriceException si la remise est invalide
     * */
    ProductDTO applyDiscount(int id, BigDecimal discountPercentage);


    /**
     * Recherche la liste des produits en rupture de stock
     * @return la liste des produits en rupture de stock
     * */
    List<ProductDTO> getOutOfStockProducts();


    /**
     * Recherche les produits avec stock faible
     * @param threshold le seuil de stock
     * @return les produits avec stock faible
     * */
    List<ProductDTO> getLowStockProducts(int threshold);
}
