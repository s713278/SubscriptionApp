package com.app.entites;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
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
    private Long id;

    @Size(min = 4, max = 20, message = "First Name must be between 4 and 20 characters long")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "First Name must not contain numbers or special characters")
    private String firstName;

    private String lastName;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    // @Size(min = 10, max = 10, message = "Mobile Number must be exactly 10 digits
    // long")
    // @Pattern(regexp = "^\\d{10}$", message = "Mobile Number must contain only
    // Numbers")
    @Column(name = "", unique = true, nullable = false)
    private Long mobile;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "tb_user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = { CascadeType.ALL })
    private Cart cart;

    public Long getId() {
        return this.id;
    }

    // One user can manage many stores
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    List<Vendor> stores = new ArrayList<>();

    // Google or Facebook or ...
    private String regSource;

    private String regDevice;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "delivery_address", columnDefinition = "jsonb")
    private Map<String, String> deliveryAddress;

    private String otp;

    private LocalDateTime otpExpiration;

    public Boolean verified;

    public void setEmail(String email) {
        this.email = ( this.email!=null?email.trim().toLowerCase():this.email);
    }

    public String getEmail(String email) {
        return ( this.email!=null?email.trim().toLowerCase():this.email);
    }

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
}