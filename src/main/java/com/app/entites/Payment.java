package com.app.entites;

import com.app.services.constants.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@Data
@Table(name = "tb_payment")
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(mappedBy = "payment", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Order order;

    // @NotBlank
    // @Size(min = 4, message = "Payment method must contain atleast 4 characters")
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    @Column(name = "payment_method", columnDefinition = "payment_type_enum")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentMethod;

    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "payment_id")
    private Address billingAddress;
}
