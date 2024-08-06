package com.dev.zine.dao;

import com.dev.zine.model.Event;
import org.springframework.data.repository.ListCrudRepository;

public interface EventDAO extends ListCrudRepository<Event, Long> {
}
