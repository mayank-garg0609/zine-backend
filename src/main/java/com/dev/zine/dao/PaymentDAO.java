package com.dev.zine.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.Payment;
import java.util.Optional;
import java.util.List;

public interface PaymentDAO extends ListCrudRepository<Payment,Long> {
    
    Optional<Payment> findByOrderId(String orderId);

    @Query("SELECT p FROM Payment p WHERE p.type='Donation' AND p.status='Paid'")
    List<Payment> getAllDonations();
}
