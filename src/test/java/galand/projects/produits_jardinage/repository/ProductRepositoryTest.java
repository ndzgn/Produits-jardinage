package galand.projects.produits_jardinage.repository;

import galand.projects.produits_jardinage.entity.Product;
import galand.projects.produits_jardinage.entity.ProductCategory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.*;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Tests du repository Product")

class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;


    @Test
    @DisplayName("Doit retourner les produits par categorie")
    void findByCategory() {

        //When
        List<Product> productList = productRepository.findByCategory(ProductCategory.OUTIL);

        //Then
        assertEquals(2, productList.size());
        assertThat(productList.get(0).getName()).isEqualTo("Secateur");

    }


    @Test
    @DisplayName("Doit retourner tous les produits disponibles dont le prix est superier a 50 000")
    void findAvailableProductsAbovePrice() {
        //When
        List<Product> productList = productRepository.findAvailableProductsAbovePrice(new BigDecimal("4000"));
        //Then
        assertThat(productList).isNotNull();
        assertThat(productList).isNotEmpty();
        assertThat(productList.size()).isEqualTo(8);
    }

    @Test
    @DisplayName("Doit filtrer les produits par categorie et qui sont dans un intervalle de prix")
    void filterByCategoryAndPrice()
    {
        //GIVEN
        Sort sort = Sort.by("STOCK").descending();
        Pageable pageable = PageRequest.of(1, 2, sort);
        //WHEN
        Page<Product> page = productRepository.filterByCategoryAndPrice(new BigDecimal(1000), new BigDecimal(10000),"PLANTE", pageable);

        //THEN
        assertThat(page).isNotEmpty();
        assertThat(page.stream().toList().size()).isEqualTo(2);
    }
}