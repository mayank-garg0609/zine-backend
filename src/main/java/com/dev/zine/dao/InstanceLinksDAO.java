package com.dev.zine.dao;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.InstanceLinks;
import com.dev.zine.model.TaskInstance;

public interface InstanceLinksDAO extends ListCrudRepository<InstanceLinks, Long>{
    List<InstanceLinks> findByTaskInstance(TaskInstance taskInstance);
}
