package com.coinsthai.repository;

import com.coinsthai.pojo.common.StringIdentifiable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * Created by 
 */
@NoRepositoryBean
public interface AbstractRepository<T extends StringIdentifiable>
        extends JpaRepository<T, String> {

    List<T> findByIdIn(List<String> ids);
}
