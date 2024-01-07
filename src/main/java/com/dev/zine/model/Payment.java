package com.dev.zine.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Data

@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="amount", nullable = false)
    private Float amount;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "email")
    private String email;

    @Column(name="order_id")
    private String order;

    @Column(name="status")
    private String status;
}