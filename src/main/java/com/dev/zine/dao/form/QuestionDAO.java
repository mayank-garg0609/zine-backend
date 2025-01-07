package com.dev.zine.dao.form;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.form.Question;

public interface QuestionDAO extends ListCrudRepository<Question, Long> {
}
