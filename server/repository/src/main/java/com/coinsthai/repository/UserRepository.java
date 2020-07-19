package com.coinsthai.repository;

import com.coinsthai.model.User;

/**
 * @author
 */
public interface UserRepository extends AbstractRepository<User> {

    User findByEmail(String email);

    long countByEmail(String email);

}
