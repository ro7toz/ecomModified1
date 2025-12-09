package com.wok.app.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EntityListeners;  // ADD THIS IMPORT
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;  // ADD THIS IMPORT

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@EntityListeners(AuditingEntityListener.class)
abstract public class AbstractMappedEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @CreatedDate
    @JsonFormat(shape = Shape.STRING)
    @Column(name = "created_at")
    private Instant createdAt;
    
    @LastModifiedDate
    @JsonFormat(shape = Shape.STRING)
    @Column(name = "updated_at")
    private Instant updatedAt;
}
