package galand.projects.produits_jardinage.mapper;

import galand.projects.produits_jardinage.dto.CreateProductDTO;
import galand.projects.produits_jardinage.dto.ProductDTO;
import galand.projects.produits_jardinage.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper implements EntityMapper<Product, ProductDTO>{
    @Override
    public ProductDTO toDto(Product entity) {

        if (entity == null)
        {
            return  null;
        }

        return ProductDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(entity.getCategory())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .description(entity.getDescription())
                .active(entity.getActive())
                .creationDate(entity.getCreationDate())
                .build();
    }

    @Override
    public Product toEntity(ProductDTO dto) {
        if (dto == null)
        {
            return null;
        }

        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .description(dto.getDescription())
                .active(dto.getActive())
                .build();
    }

    /**
     * Convertit un CreateProductDTO vers une entite Product
     * @param createProductDTO  le DTO de creation
     * @return l'entite Product
     * */
    public Product toEntityFromCreate(CreateProductDTO createProductDTO)
    {
        if(createProductDTO == null)
        {
            return null;
        }
        return Product.builder()
                .name(createProductDTO.getName())
                .category(createProductDTO.getCategory())
                .price(createProductDTO.getPrice())
                .stock(createProductDTO.getStock())
                .description(createProductDTO.getDescription())
                .active(true)
                .build();
    }

    /**
     * Met a jour une entite existante avec les donnees d'un DTO
     * @param entity l'entite a mettre a jour
     * @param dto le DTO source
     * */
    public void updateEntityFromDto(ProductDTO dto, Product entity)
    {
        if(dto == null || entity == null)
        {
            return;
        }

        entity.setName(dto.getName());
        entity.setCategory(dto.getCategory());
        entity.setPrice(dto.getPrice());
        entity.setStock(dto.getStock());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.getActive());
    }

    @Override
    public List<ProductDTO> toDtosList(List<Product> entities) {
        return EntityMapper.super.toDtosList(entities);
    }

    @Override
    public List<Product> toEntitiesList(List<ProductDTO> dtos) {
        return EntityMapper.super.toEntitiesList(dtos);
    }


}
