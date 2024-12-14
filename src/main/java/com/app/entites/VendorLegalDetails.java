package com.app.entites;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class VendorLegalDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String GSTNumber;

    private String PANNumber;

    private String businessRegNumber;

    @OneToOne(cascade = { CascadeType.MERGE })
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;
}
