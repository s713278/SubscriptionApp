package com.app.entites;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.app.entites.type.DeliverySlot;
import com.app.entites.type.SubFrequency;
import com.app.entites.type.SubStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_Subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private SubFrequency frequency;
    
    @Enumerated(EnumType.STRING)
    private SubStatus status;
    

    @Enumerated(EnumType.STRING)
    private DeliverySlot timeSlot;
    
    private LocalDate startDate;

    private LocalDate endDate;
    
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    
    private LocalDateTime updatedAt;
    
}
