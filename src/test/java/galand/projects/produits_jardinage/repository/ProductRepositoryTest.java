package galand.projects.produits_jardinage.repository;

import galand.projects.produits_jardinage.entity.Product;
import galand.projects.produits_jardinage.entity.ProductCategory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Tests du repository Product")
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EntityManager entityManager;



    Product rosier;
    Product tondeuse;
    Product engrais;

    @BeforeEach
    void setUp() {
        //Preparation des donnees avant chaque Test
        rosier = Product.builder()
                .category(ProductCategory.PLANTE)
                .name("Rosier grimpant")
                .price(new BigDecimal("9500"))
                .stock(50)
                .description("Rosier grimpant rouge")
                .active(true)
                .build();

        tondeuse = Product.builder()
                .name("Tondeuse electrique")
                .category(ProductCategory.OUTIL)
                .price(new BigDecimal("150000"))
                .stock(5)
                .description("Tondeuse 1500W")
                .active(true)
                .build();

        engrais = Product.builder()
                .name("Engrais bio")
                .category(ProductCategory.ENGRAIS)
                .price(new BigDecimal("7000"))
                .stock(0)
                .description("Engrais biologique 5kg")
                .active(false)
                .build();

        entityManager.persist(rosier);
        entityManager.persist(tondeuse);
        entityManager.persist(engrais);
        entityManager.flush();
    }

    @Test
    @DisplayName("Doit retourner les produits par categorie")
    void findByCategory() {
        //Given
        Product arrosoir = Product.builder()
                .name("Arrosoir")
                .category(ProductCategory.OUTIL)
                .price(new BigDecimal("1500"))
                .description("Arrosoir a plantes manuel")
                .active(true)
                .stock(40)
                .build();
        entityManager.persist(arrosoir);
        //When
        List<Product> productList = productRepository.findByCategory(ProductCategory.OUTIL);

        //Then
        assertEquals(2, productList.size());
        assertEquals("Tondeuse electrique", productList.get(0).getName());

    }

    @Test
    @DisplayName("Doit retourner tous les produits actifs")
    void findByActiveTrue() {
        //When
        List<Product> productList = productRepository.findByActiveTrue();

        //Then
        assertEquals(2, productList.size());
        assertEquals(true, productList.get(0).getActive());
    }

    @Test
    @DisplayName("Doit enregistrer dans la base de donnees")
    void createProduct()
    {
        //Given
        Product arrosoir = Product.builder()
                .name("Arrosoir")
                .category(ProductCategory.OUTIL)
                .price(new BigDecimal("1500"))
                .description("Arrosoir a plantes manuel")
                .active(true)
                .stock(40)
                .build();
        //When
        Product product = productRepository.save(arrosoir);

        //Then
        assertNotNull(product);
        assertEquals("Arrosoir", product.getName());
        assertNotNull(product.getId());
    }

    @Test
    void findByActiveFalse() {
    }

    @Test
    void findByNameContainingIgnoreCase() {
    }

    @Test
    void findByPriceBetween() {
    }

    @Test
    void findByCategoryAndActiveTrue() {
    }

    @Test
    void findByNameIgnoreCase() {
    }

    @Test
    void countByCategory() {
    }

    @Test
    void existsByNameIgnoreCase() {
    }

    @Test
    @DisplayName("Doit trouver les produits en rupture de stock")
    void findLowStockProducts() {
        // When
        List<Product> lowStock = productRepository.findLowStockProducts
                (10);

        // Then
        assertEquals(1, lowStock.size());
        assertEquals("Tondeuse electrique", lowStock.get(0).getName());
    }

    @Test
    void findByCategoryAndOrderByPriceDesc() {
    }

    @Test
    @DisplayName("Retourne toutes les categories de produit actif")
    void findAllActiveCategories() {
        //When
        List<ProductCategory> productCategoryList = productRepository.findAllActiveCategories();
        //Then
        assertEquals(2, productCategoryList.size());
    }

    @Test
    @DisplayName("Doit retourner tous les produits disponibles dont le prix est superier a 50 000")
    void findAvailableProductsAbovePrice() {
        //When
        List<Product> productList = productRepository.findAvailableProductsAbovePrice(new BigDecimal("50000"));
        //Then
        assertEquals(1, productList.size());
        assertEquals("Tondeuse electrique", productList.get(0).getName());
    }
}