package com.dev.zine.dao;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.Recruitment;

public interface RecruitmentDAO extends ListCrudRepository<Recruitment, Long>{
    
}
