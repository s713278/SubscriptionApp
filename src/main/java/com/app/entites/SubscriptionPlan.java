package com.app.entites;

import java.io.Serializable;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import com.app.entites.type.DeliveryMode;
import com.app.entites.type.SubFrequency;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name="tb_sub_plan")
@Entity
public class SubscriptionPlan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @JdbcType(value  = PostgreSQLEnumJdbcType.class)
    @Column(name = "frequency", columnDefinition = "subscription_frequency")
    private SubFrequency frequency;

    private String description;

    @JdbcType(value  = PostgreSQLEnumJdbcType.class)
    @Enumerated(EnumType.STRING)
    @Column(name="delivery_mode",columnDefinition = "delivery_mode_enum")
    private DeliveryMode deliveryMode;

}
