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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User /* extends AbstractAuditingEntity<Long> */ implements Serializable {

  private static final long serialVersionUID = -8493127251609026343L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Size(min = 5, max = 20, message = "First Name must be between 5 and 30 characters long")
  @Pattern(
      regexp = "^[a-zA-Z]*$",
      message = "First Name must not contain numbers or special characters")
  private String firstName;

  @Size(min = 5, max = 20, message = "Last Name must be between 5 and 30 characters long")
  @Pattern(
      regexp = "^[a-zA-Z]*$",
      message = "Last Name must not contain numbers or special characters")
  private String lastName;

  @Size(min = 10, max = 10, message = "Mobile Number must be exactly 10 digits long")
  @Pattern(regexp = "^\\d{10}$", message = "Mobile Number must contain only Numbers")
  @Column(unique = true, nullable = false)
  private String mobileNumber;

  @Email
  @Column(unique = true, nullable = false)
  private String email;

  private String password;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @ManyToMany(
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_address",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "address_id"))
  private List<Address> addresses = new ArrayList<>();

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
  List<Store> stores = new ArrayList<>();

  // Google or Facebook or ...
  private String source;
}
