package com.app.entites;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_vendor_legal_details")
public class VendorLegalDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String GSTNumber;

    private String PANNumber;

    private String regNumber;

    @OneToOne(cascade = { CascadeType.MERGE })
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;
}
