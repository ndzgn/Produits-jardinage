package galand.projects.produits_jardinage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
@Builder
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, length = 100)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductCategory category;
    @Column(nullable = false, precision = 10,  scale = 2)
    private BigDecimal price;
    @Column(nullable = false)
    private Integer stock;
    @Column(length = 500)
    private String description;
    @Column(nullable = false)
    private Boolean active;
    @Column(nullable = false, updatable = false, name = "creation_date")
    private LocalDateTime creationDate;

    @PrePersist
    protected  void  onCreate()
    {
        if (creationDate == null)
        {
            creationDate = LocalDateTime.now();
        }

        if(active == null)
        {
            active = true;
        }
    }
}
