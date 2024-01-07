package com.dev.zine.dao;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.Payment;
import java.util.Optional;
import java.util.List;



public interface PaymentDAO extends ListCrudRepository<Payment,Long> {
    
    Optional<Payment> findByOrderId(String orderId);
}
