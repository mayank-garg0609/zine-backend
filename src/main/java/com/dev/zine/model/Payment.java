package com.dev.zine.model;

import java.sql.Timestamp;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
    @Email
    private String email;

    @Column(name="orderId")
    private String orderId;

    @Column(name="status")
    private String status;

    @Column(name="type")
    private String type;

    @Column(name="remarks")
    private String remarks;

    @Column (name="pay_id")
    private String payId;

    @Column (name="signature")
    private String signature;
}