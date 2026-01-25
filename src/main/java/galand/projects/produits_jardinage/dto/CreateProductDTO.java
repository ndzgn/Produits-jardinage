package galand.projects.produits_jardinage.dto;

import galand.projects.produits_jardinage.entity.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductDTO {
    private String name;
    private ProductCategory category;
    private BigDecimal price;
    private Integer stock;
    private String description;
}
