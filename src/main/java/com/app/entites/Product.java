package com.app.entites;

import java.io.Serializable;

import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tb_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AbstractAuditingEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @NotBlank
    @Size(min = 3, message = "Product name must contain atleast 3 characters")
    private String name;

    private String imagePath;

    @NotBlank
    @Size(min = 25, message = "Product description must contain at least 25 characters")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private ObjectNode features;

    @Override
    public Long getId() {
        return this.productId;
    }
}
