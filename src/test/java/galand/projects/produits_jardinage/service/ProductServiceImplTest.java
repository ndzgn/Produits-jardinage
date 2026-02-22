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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


/**
 * Test unitaire avec Mockito
 * */
@ExtendWith(MockitoExtension.class)
@DisplayName("Test unitaire du ProductServiceImpl")
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    Product product;
    ProductDTO productDTO;
    CreateProductDTO createProductDTO;

    @BeforeEach()
    void setUp()
    {
         product = Product.builder()
                .category(ProductCategory.ENGRAIS)
                .name("POTASSIUM")
                .active(true)
                .id(1)
                .description("Engrais favorisant la pousse des fleurs")
                .stock(50)
                .price(new BigDecimal(5900))
                .build();


         productDTO = ProductDTO.builder()
                .id(1)
                .name("POTASSIUM")
                .active(true)
                .category(ProductCategory.ENGRAIS)
                .description("Engrais favorisant la pousse des fleurs")
                .stock(50)
                .price(new BigDecimal(5900))
                .build();

         createProductDTO = CreateProductDTO.builder()
                .name("POTASSIUM")
                .category(ProductCategory.ENGRAIS)
                .description("Engrais favorisant la pousse des fleurs")
                .stock(50)
                .price(new BigDecimal(5900))
                .build();
    }

    @Test
    @DisplayName("Doit creer un produit avec succes")
    void createProduct() {
        //Given
        when(productRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
        when(productMapper.toEntityFromCreate(any(CreateProductDTO.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        //When
        ProductDTO result = productService.createProduct(createProductDTO);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("POTASSIUM");
        assertThat(result.getId()).isEqualTo(1);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Doit rechercher un produit par son ID")
    void getProductByID() {

        //GIVEN
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        //WHEN
        ProductDTO result = productService.getProductByID(1);

        //THEN
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("POTASSIUM");
        assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    void getAllActiveProduct() {
    }

    @Test
    void getProductsByCategory() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void deleteProduct() {
    }

    @Test
    @DisplayName("Doit ajuster le stock avec succes")
    void adjustStock() {
        //GIVEN
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDTO);


        //WHEN
        ProductDTO result = productService.adjustStock(1, 20);

        //THEN
        assertThat(result).isNotNull();
        verify(productRepository).save(any());
    }

    @Test
    @DisplayName("Doit lever une exception si le stock est insuffisant")
    void adjustStock_insufficient()
    {
        //GIVEN
        when(productRepository.findById(1)).thenReturn(Optional.of(product));


        //WHEN AND THEN
        assertThatThrownBy(()->productService.adjustStock(1, -60)).isInstanceOf(InsufficientStockException.class);
        verify(productRepository, never()).save(any());
        verify(productMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Doit appliquer une remise valide")
    void applyDiscount() {
        //GIVEN
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDTO);

        //WHEN
        ProductDTO result = productService.applyDiscount(1, new BigDecimal(30));

        //THEN
        verify(productRepository).save(any());

    }

    @Test
    void getOutOfStockProducts() {
    }

    @Test
    void getLowStockProducts() {
    }

    @Test
    @DisplayName("Doit lever une exception si le prix est invalide")
    void validatePrice() {
        //GIVEN
        createProductDTO.setPrice(new BigDecimal(-4000));

        //WHEN AND THEN
        assertThatThrownBy(()->productService.createProduct(createProductDTO)).isInstanceOf(InvalidPriceException.class);
    }

    @Test
    @DisplayName("Doit lever une exception si le produit existe deja")
    void createProductDuplicateName()
    {
        //GIVEN
        when(productRepository.existsByNameIgnoreCase("POTASSIUM")).thenReturn(true);

        //WHEN AND THEN
        assertThatThrownBy(()->productService.createProduct(createProductDTO)).isInstanceOf(DuplicateProductException.class);
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Doit lever une exception si le produit n'existe")
    void getProductByIdNotFound()
    {
        //GIVEN
        when(productRepository.findById(23)).thenReturn(Optional.empty());

        //WHEN AND THEN
        assertThatThrownBy(()->productService.getProductByID(23)).isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("Doit lever une exception si la remise est invalide")
    void testApplyDiscount_InvalidDiscount() {
        // Given
        when(productRepository.findById(1)).thenReturn(Optional.of(product));


        // When / Then
        assertThatThrownBy(()-> productService.applyDiscount(1, new BigDecimal("60")))
                .isInstanceOf(InvalidPriceException.class);
    }

    @Test
    @DisplayName("Doit supprimer logiquement un produit")
    void testDeleteProduct_Success() {
    // Given
    when(productRepository.findById(1)).thenReturn(Optional.of(product));
    when(productRepository.save(any())).thenReturn(product);
    // When
        productService.deleteProduct(1);
    // Then
        verify(productRepository).save(argThat(p-> !p.getActive()));
    }
}