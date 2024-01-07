package com.dev.zine.dao;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.Message;
import com.dev.zine.model.Payment;
import com.dev.zine.model.Rooms;

import java.util.Optional;
import java.util.List;

public interface PaymentsDAO extends ListCrudRepository<Payment, Long> {

    Payment findByOrder(String orders);
}
