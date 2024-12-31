package com.app.entites;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Table(name = "tb_sku_sub_plan")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuSubscriptionPlan implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "sku_id", nullable = false)
  private Sku sku;

  @ManyToOne
  @JoinColumn(name = "sub_plan_id", nullable = false)
  private SubscriptionPlan subscriptionPlan;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "eligible_delivery_days", columnDefinition = "jsonb")
  private Map<String, Object> eligibleDeliveryDays;

  // Additional fields can be added as needed
  //  @Column(name = "validity")
  // private String validity;

}
