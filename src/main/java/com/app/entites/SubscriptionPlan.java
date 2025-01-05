package com.app.entites;

import com.app.entites.type.DeliveryMode;
import com.app.entites.type.SubFrequency;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Data
@NoArgsConstructor
@Table(name = "tb_sub_plan")
@Entity
public class SubscriptionPlan implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "subscription_eligible")
  private boolean subscriptionEligible;

  @Enumerated(EnumType.STRING)
  @JdbcType(value = PostgreSQLEnumJdbcType.class)
  @Column(name = "frequency", columnDefinition = "subscription_frequency")
  private SubFrequency frequency;

  @JdbcType(value = PostgreSQLEnumJdbcType.class)
  @Enumerated(EnumType.STRING)
  @Column(name = "delivery_mode", columnDefinition = "delivery_mode_enum")
  private DeliveryMode deliveryMode;

  private String description;
}
