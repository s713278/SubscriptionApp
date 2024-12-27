package com.app.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "tb_role")
@NoArgsConstructor
public class Role {

  @Id
  @Column(name = "role_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long roleId;

  @NotBlank
  @Column(name = "ROLE_NAME")
  private String roleName;
}
