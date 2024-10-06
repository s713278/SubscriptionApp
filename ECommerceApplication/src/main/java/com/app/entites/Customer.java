package com.app.entites;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_customer")
@Getter
@Setter
public class Customer implements Serializable {

    private static final long serialVersionUID = -8493127251609026343L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 4, max = 20, message = "First Name must be between 4 and 20 characters long")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "First Name must not contain numbers or special characters")
    private String firstName;

    private String lastName;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "", unique = true, nullable = false)
    private Long mobile;

    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
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

    @Column(name="otp_code")
    private String otp;

    private LocalDateTime otpExpiration;

    @Column(name="mobile_verifed")
    public Boolean mobileVerifed;

    public void setEmail(String email) {
        this.email = email.trim().toLowerCase();
    }

    public String getEmail(String email) {
        return this.email;
    }

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
    
    @Column(name="email_verified")
    private Boolean emailVerified;
    
    @Column(name="email_activation_token")
    private String emailActivationToken;
    
    @Column(name="reset_password_token")
    private String resetPasswordToken;
    
    @Column(name="email_token_expiration")
    private LocalDateTime emailTokenExpiration;

}
