package com.app.entites;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Setter;

/**
 * Base abstract class for entities which will hold definitions for created,
 * last modified, created by, last modified by attributes.
 */
@MappedSuperclass
@JsonIgnoreProperties(value = { "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate" }, allowGetters = true)
public abstract class AbstractAuditingEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public abstract T getId();

    @Setter
    @Column(name = "created_by", nullable = true, length = 50)
    protected String createdBy;

    @ColumnDefault("timestamptz(6) default CURRENT_TIMESTAMP")
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    protected LocalDateTime createdDate = LocalDateTime.now();

    @ColumnDefault("varchar(50) default System")
    @Column(name = "last_modified_by", length = 50)
    protected String lastModifiedBy;

    @ColumnDefault("timestamptz(6) default CURRENT_TIMESTAMP")
    @UpdateTimestamp
    @Column(name = "last_modified_date")
    protected LocalDateTime lastModifiedDate = LocalDateTime.now();

}
