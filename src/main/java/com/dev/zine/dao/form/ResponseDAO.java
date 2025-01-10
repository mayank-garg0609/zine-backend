package com.dev.zine.dao.form;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.form.Response;

public interface ResponseDAO extends ListCrudRepository<Response, Long> {
}

