package galand.projects.produits_jardinage.controller;

import galand.projects.produits_jardinage.dto.CreateProductDTO;
import galand.projects.produits_jardinage.dto.ProductDTO;
import galand.projects.produits_jardinage.entity.Product;
import galand.projects.produits_jardinage.entity.ProductCategory;
import galand.projects.produits_jardinage.service.ProductService;
import galand.projects.produits_jardinage.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductServiceImpl  productService;

    /**
     * GET /api/v1/products
     * Recupere tous les produits actifs
     * */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts()
    {

        log.info("GET /api/v1/products - Recuperation de tous les produits");
        List<ProductDTO> products = productService.getAllActiveProduct();
        return ResponseEntity.ok(products);
    }

    /**
     * GET api/v1/products/{id}
     * Recupere un produit a partir de son id
     * */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProdcutById(@PathVariable int id)
    {
        log.info("GET /api/v1/products/{} - Recuperation du produit", id);
        ProductDTO productDTO = productService.getProductByID(id);
        return ResponseEntity.ok(productDTO);
    }

    /**
     * GET /api/v1/products/category/{category}
     * Recupere tous les produits par categorie
     * */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDTO>> getProdcutsByCategory(@PathVariable ProductCategory category)
    {
        log.info("GET /api/v1/products/category/{}", category);
        List<ProductDTO> products = productService.getProducstByCategory(category);
        return ResponseEntity.ok(products);
    }

    /**
     * POST /api/v1/products
     * Cree un nouveau produit
     * */
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody CreateProductDTO createDto){
        log.info("POST /api/v1/prodcuts - Creation d'un produit: {}", createDto.getName());
        ProductDTO createdProduct = productService.createProduct(createDto);
        URI location = URI.create("/api/v1/products/"+createdProduct.getId());
        return ResponseEntity.created(location).body(createdProduct);
    }


    /**
     * PUT /api/v1/products/{id}
     * Met a jour un produit existant
     * */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable int id, @RequestBody ProductDTO productDTO) {
        log.info("PUT /api/products/{} - Mise a jour du produit", id);
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }


    /**
     * DELETE /api/v1/products/{id}
     * Supprime logiquement un produit
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id)
    {
        log.info("DELETE /api/v1/products/{} - Suppression du produit", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH /api/v1/products/{id}/stock
     * Ajuste le stock d'un produit
     * */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductDTO> adjustStock(@PathVariable int id, @RequestParam int quantity)
    {
        log.info("PATCH /api/v1/products/{}/stock - Ajustement: {}", id, quantity);
        ProductDTO product = productService.adjustStock(id, quantity);
        return ResponseEntity.ok(product);
    }

    /**
     * PATCH /api/v1/products/{id}/discount
     * Applique une remise sur le produit
     * */
    @PatchMapping("/{id}/discount")
    public ResponseEntity<ProductDTO> applyDiscount(@PathVariable int id, @RequestParam BigDecimal percentage)
    {
        log.info("PATCH /api/v1/products/{}/discount - Remise:{}%", id, percentage);
        ProductDTO productDTO = productService.applyDiscount(id, percentage);
        return ResponseEntity.ok(productDTO);
    }

    /**
     * GET /api/v1/products/search/low-stock
     * Recherche les produits avec stock faible
     * */
    @GetMapping("/search/low-stock")
    public ResponseEntity<List<ProductDTO>> getLowStockProducts(@RequestParam(defaultValue = "10") int threshold)
    {
        log.info("GET /api/v1/products/search/low-stock?threshold = {}", threshold);
        List<ProductDTO> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(products);
    }


    /**
     * GET /api/v1/products/search/out-of-stock
     * Recherche les produits en rupture de stock
     * */
    @GetMapping("/search/out-of-stock")
    public ResponseEntity<List<ProductDTO>> getOutOfStockProducts()
    {
        log.info("GET /api/v1/products/search/out-of-stock");
        List<ProductDTO> products = productService.getOutOfStockProducts();
        return ResponseEntity.ok(products);
    }
}
