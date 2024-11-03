package com.app.entites;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_catalog")
public class Catalog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long catalogId;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    private String name;
    private String description;

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL)
    private List<Category> categories;
}
