package com.ebanking.system.service;

import com.ebanking.system.dto.UserRequest;
import com.ebanking.system.dto.UserResponse;
import com.ebanking.system.entity.User;

/**
 * Manipulates the procedures for the {@link User} entity
 */
public interface IUserService {

    /**
     * Creates a {@link User} by using the input request
     *
     * @param userRequest the request that contains the details of the new {@link User}
     * @return the {@link UserResponse} containing the {@link User} details
     */
    UserResponse createUser(UserRequest userRequest);

    /**
     * Gets a {@link User} by using the id
     *
     * @param id the id of the {@link User}
     * @return the {@link UserResponse} containing the {@link User} details
     */
    UserResponse getUserById(Long id);
}
