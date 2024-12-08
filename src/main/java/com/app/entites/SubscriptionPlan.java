package com.app.entites;

import com.app.entites.type.SubFrequency;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.io.Serializable;

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

}
