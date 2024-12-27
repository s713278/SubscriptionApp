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

  @JoinColumn(name = "gst_number")
  private String GSTNumber;

  @JoinColumn(name = "pan_number")
  private String PANNumber;

  @JoinColumn(name = "reg_number")
  private String regNumber;

  @OneToOne(cascade = {CascadeType.MERGE})
  @JoinColumn(name = "vendor_id")
  private Vendor vendor;
}
