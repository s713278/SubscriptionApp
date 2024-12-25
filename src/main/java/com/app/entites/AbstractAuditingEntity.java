package com.app.entites;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * Base abstract class for entities which will hold definitions for created,
 * last modified, created by, last modified by attributes.
 */
@Getter
@Setter
@MappedSuperclass
@JsonIgnoreProperties(value = { "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate" }, allowGetters = true)
public abstract class AbstractAuditingEntity<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public abstract T getId();


    @CreatedBy
    @Column(name = "created_by", nullable = false, length = 50)
    protected String createdBy;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    protected LocalDateTime createdDate ;

    @LastModifiedBy
    @Column(name = "last_modified_by", nullable = false,length = 50)
    protected String updatedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date",nullable = false)
    protected LocalDateTime lastModifiedDate ;

}
