package com.app.entites;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;

import com.app.entites.type.Gender;
import com.app.entites.type.UserType;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_customer")
@Getter
@Setter
public class Customer  extends  AbstractAuditingEntity<Long> implements Serializable {

    @Serial
    private static final long serialVersionUID = -8493127251609026343L;

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

   // @Size(min = 4, max = 20, message = "First Name must be between 4 and 20 characters long")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "First Name must not contain numbers or special characters")
    private String firstName;

    private String lastName;

    @Column(name = "type", columnDefinition = "user_type")
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Email
    @Column(unique = true, nullable = true)
    private String email;


    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "mobile", unique = true, nullable = false)
    private Long mobile;

    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    @Column(name = "gender", columnDefinition = "gender_enum")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dateOfBirth;

    @Transient
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tb_user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Transient
   @OneToOne(mappedBy = "user", cascade = { CascadeType.ALL },fetch = FetchType.LAZY)
    private Cart cart;

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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "delivery_instructions", columnDefinition = "jsonb")
    private Map<String,String> deliveryInstructions;



    @Column(name="mobile_verified")
    public Boolean mobileVerified = false;

    @Column(name="mobile_verified_timestamp")
    public LocalDateTime mobileVerifiedTime;

    public void setEmail(String email) {
        this.email = email.trim().toLowerCase();
    }

    public String getEmail(String email) {
        return this.email;
    }

    @ColumnDefault(value = "false")
    @Column(name="email_verified")
    private Boolean emailVerified = false;

 @Transient
 private String otp;

 @Transient
 private LocalDateTime otpExpiration;

 @Transient
   // @Column(name="email_activation_token")
    private String emailActivationToken;

 @Transient
   // @Column(name="reset_password_token")
    private String resetPasswordToken;

 @Transient
   // @Column(name="email_token_expiration")
    private LocalDateTime emailTokenExpiration;

    @ColumnDefault(value = "true")
    private boolean isActive;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "preferences", columnDefinition = "jsonb")
    private Map<String, String> preferences;

    public String getFullMobileNumber(){
        return countryCode+mobile;
    }
}
