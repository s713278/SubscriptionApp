package com.app.entites;

import com.app.entites.type.AddressTypeConverter;
import com.app.payloads.AddressDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "tb_customer")
@Getter
@Setter
public class Customer implements Serializable {

    private static final long serialVersionUID = -8493127251609026343L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Size(min = 4, max = 20, message = "First Name must be between 4 and 20 characters long")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "First Name must not contain numbers or special characters")
    private String firstName;

    private String lastName;

    @Email
    @Column(unique = true, nullable = false)
    private String email;
    
    //@Size(min = 10, max = 10, message = "Mobile Number must be exactly 10 digits long")
    //@Pattern(regexp = "^\\d{10}$", message = "Mobile Number must contain only Numbers")
    @Column(unique = true, nullable = false)
    private Long mobile;

    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    @OneToOne(
            mappedBy = "user",
            cascade = {CascadeType.ALL})
    private Cart cart;

    public Long getId() {
        return this.userId;
    }

    // One user can manage many stores
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    List<Vendor> stores = new ArrayList<>();

    // Google or Facebook or ...
    private String regSource;
    
    private String regDevice;
    
    @Column(name="delivery_address",columnDefinition = "jsonb")
    @Convert(converter = AddressTypeConverter.class)
    private AddressDTO deliveryAddress;
    
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Instant createdDate = Instant.now();
    
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate = Instant.now();
}
