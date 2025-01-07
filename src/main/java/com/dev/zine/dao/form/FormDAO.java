package com.dev.zine.dao.form;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.form.Form;

public interface FormDAO extends ListCrudRepository<Form, Long> {
}
