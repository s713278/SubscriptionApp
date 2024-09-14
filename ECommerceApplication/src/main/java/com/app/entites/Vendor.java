package com.app.entites;

import com.app.entites.type.MapTypeConverter;
import com.app.entites.type.VerificationStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Data
@Table(name = "tb_vendor")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Vendor extends AbstractAuditingEntity<Long> implements Serializable {

    /**
     *
     */
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
    private String customerEmail;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Category> categories = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus;

    @Column(name = "business_address", columnDefinition = "jsonb")
    //@Convert(converter = AddressTypeConverter.class)
    private  Map<String, String> businessAddress;

    @Column(name = "service_area", columnDefinition = "jsonb")
    @Convert(converter = MapTypeConverter.class)
    private Map<String, Object> serviceAreas;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Instant createdDate = Instant.now();

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate = Instant.now();
}
