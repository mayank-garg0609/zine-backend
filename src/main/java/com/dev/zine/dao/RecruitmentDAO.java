package com.dev.zine.dao;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.Recruitment;

public interface RecruitmentDAO extends ListCrudRepository<Recruitment, Long>{
    boolean existsByStage(Long id);
    Recruitment findByStage(Long stage);
}
