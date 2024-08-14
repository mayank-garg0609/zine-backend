package com.dev.zine.dao;

import com.dev.zine.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface TaskDAO extends JpaRepository<Task, Long> {
    Optional<Task> findById(Long id);
}
