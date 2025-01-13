package com.app.entites;

import com.app.entites.type.SkuType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "tb_subscription")
public class Subscription extends AbstractAuditingEntity<Long> implements Serializable {
  @Serial private static final long serialVersionUID = 2038580641079721330L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "customer_id", nullable = false)
  // Foreign column to tb_user table
  private Long userId;

  @Column(name = "sku_id", nullable = false)
  // Foreign column to tb_vendor_sku table
  private Long skuId;

  @Column(name = "vendor_id", nullable = false)
  // Foreign column to tb_vendor table
  private Long vendorId;

  @Column(name = "price_id", nullable = false)
  // Foreign column to tb_sku_price table
  private Long priceId;

  private Integer quantity;

  // This can be fetched to get the service_attributes if the item type is SERVICE by skipping the
  // sku table.
  @JdbcType(value = PostgreSQLEnumJdbcType.class)
  @Enumerated(EnumType.STRING)
  @Column(name = "subscription_type", columnDefinition = "sku_type")
  private SkuType type;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @NotNull(message = "Subscription status  is mandatory.")
  @JdbcType(value = PostgreSQLEnumJdbcType.class)
  @Enumerated(EnumType.STRING)
  @Column(name = "status", columnDefinition = "subscription_status")
  private SubscriptionStatus status;

  @CollectionTable(
      name = "tb_subscription_custom_days",
      joinColumns = @JoinColumn(name = "subscription_id"))
  @ElementCollection(fetch = FetchType.EAGER)
  private List<Integer> customDays; // For custom date range

  // 1 - Monday
  @NotNull(message = "Delivery date is mandatory.")
  @Column(name = "next_delivery_date")
  private LocalDate nextDeliveryDate;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "delivery_address", columnDefinition = "jsonb")
  private Map<String, String> deliveryAddress;

  @Column(name = "update_version", updatable = true)
  private Integer updateVersion = 0;

  //  Future enhancement and replace for frequency ,deliveryMode
  // @ManyToOne(
  //   cascade = {CascadeType.MERGE},
  // fetch = FetchType.LAZY) // Sku is the "owner" of the relationship
  @ManyToOne
  @JoinColumn(name = "subscription_plan_id", nullable = false)
  private SubscriptionPlan subscriptionPlan;

  // Getters and Setters

  @Column(name = "special_notes")
  private String specialNotes;
}
