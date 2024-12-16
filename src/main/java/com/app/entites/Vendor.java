package com.app.entites;

import java.io.Serializable;
import java.util.Map;

import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;

import com.app.entites.type.ApprovalStatus;
import com.app.entites.type.VendorStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "tb_vendor")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Vendor extends AbstractAuditingEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Name of the vendor's business (e.g., "Mirdoddi Farm Fresh Dairy and Veggies").
    @NotBlank
    @Size(min = 5, message = "Business name must contain at least 5 characters")
    private String businessName;

    @NotBlank
    @Column(name = "business_type")
    private String businessType;

    @NotBlank
    @Size(min = 3, message = "Owner name must contain at least 3 characters")
    private String ownerName;

    //Contact & Address Details
    @NotBlank(message = "Contact person name is required")
    @Size(min = 3, message = "Contact person name at least 3 characters")
    private String contactPerson;

    private String description;

    // @Size(min = 10, max = 10, message = "Mobile Number must be exactly 10 digits
    // long")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile Number must contain only Numbers")
    @NotBlank(message = "Contact number is required.")
    private String contactNumber;


    //Vendor's email for notifications and correspondence.
    @Email
    @NotBlank(message = "Contact email is required.")
    @Column(name="communication_email")
    private String communicationEmail;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "business_address", columnDefinition = "jsonb")
    //@Convert(converter = AddressTypeConverter.class)
    private  Map<String, String> businessAddress;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "service_area", columnDefinition = "jsonb")
  //  @Convert(converter = MapTypeConverter.class)
    private Map<String, Object> serviceAreas;

    @Column(name="banner_image")
    private String bannerImage;

    //Operational Details
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    @Column(name = "status", columnDefinition = "vendor_status_enum")
    @Enumerated(EnumType.STRING)
    private VendorStatus status;

    @Column(name = "approval_status", columnDefinition = "verification_status_enum")
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Customer user;

}
