package com.dev.zine.dao.form;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import com.dev.zine.model.form.Form;

public interface FormDAO extends ListCrudRepository<Form, Long> {
    @Query("SELECT f.id FROM Form f WHERE NOT EXISTS (" +
       "SELECT r FROM Response r JOIN r.question q WHERE q.form = f AND r.user.id = :userId)")
    List<Long> findFormsUserHasNotFilled(@Param("userId") Long userId);

}
