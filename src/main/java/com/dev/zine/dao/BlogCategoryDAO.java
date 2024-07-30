package com.dev.zine.dao;

import org.springframework.data.repository.ListCrudRepository;
import com.dev.zine.model.BlogCategory;
import java.util.Optional;

public interface BlogCategoryDAO extends ListCrudRepository<BlogCategory, Integer>{
    Optional<BlogCategory> findByCategoryIgnoreCase(String category);
    BlogCategory findByCategoryID(Integer categoryID);
}
