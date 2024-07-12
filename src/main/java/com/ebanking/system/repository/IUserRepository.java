package com.ebanking.system.repository;

import com.ebanking.system.entity.User;

/**
 * Defines persistence set of operations for {@link User} objects
 */
public interface IUserRepository {

    /**
     * Inserts the provided {@link User} object
     *
     * @param user the user object provided
     */
    Integer insert(User user);

    /**
     * Select the user by using the id
     *
     * @param id the id of the user
     */
    User select(Long id);

    /**
     * Update the user by using the id
     *
     * @param id the id of the user
     */
    Integer update(Long id, String firstName, String lastName);
}
