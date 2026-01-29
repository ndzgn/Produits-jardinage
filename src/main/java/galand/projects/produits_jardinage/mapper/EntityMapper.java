package galand.projects.produits_jardinage.mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface generique pour le mapping Entity <--> DTO
 * @param <E> Type de l'entite
 * @param <D> Type du DTO
 * */
public interface EntityMapper <E, D>{
    /**
     * Convertit une entite en DTO
     * @param entity l'entite a convertir
     * @return le DTO correspondant
     * */
    D toDto (E entity);

    /**
     * Convertit un DTO en entite
     * @param dto le dto a convertir
     * @return l'entite correspondante
     * */
    E toEntity(D dto);

    /**
     * Convertir une liste de DTO en liste d'entite
     * @param entities liste d'entite a mapper en liste de DTO
     * @return la liste de DTOs
     * */
    default List<D> toDtosList (List<E> entities)
    {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * @param dtos La liste des DTOs a covertir en liste de Entity
     * @return la liste d'entite
     * */
    default List<E> toEntitiesList(List<D> dtos)
    {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
