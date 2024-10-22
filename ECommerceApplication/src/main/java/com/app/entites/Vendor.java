package com.app.entites;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import com.app.entites.type.MapTypeConverter;
import com.app.entites.type.VendorStatus;
import com.app.entites.type.VerificationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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


    @Serial
    private static final long serialVersionUID = -1023982518500140805L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5, message = "Business name must contain atleast 5 characters")
    private String businessName;

    @NotBlank
    @Size(min = 3, message = "Owner name must contain atleast 3 characters")
    private String ownerName;

    // @Size(min = 10, max = 10, message = "Mobile Number must be exactly 10 digits
    // long")
    // @Pattern(regexp = "^\\d{10}$", message = "Mobile Number must contain only
    // Numbers")
    @NotBlank(message = "Contact number is required.")
    private String contactNumber;

    @Email
    @NotBlank(message = "Contact email is required.")
    private String email;

    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    @Column(name = "status", columnDefinition = "vendor_status_enum")
    @Enumerated(EnumType.STRING)
    private VendorStatus status;
    
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus;

    @Column(name = "business_address", columnDefinition = "jsonb")
    //@Convert(converter = AddressTypeConverter.class)
    private  Map<String, String> businessAddress;

    @Column(name = "service_area", columnDefinition = "jsonb")
    @Convert(converter = MapTypeConverter.class)
    private Map<String, Object> serviceAreas;
    
}
