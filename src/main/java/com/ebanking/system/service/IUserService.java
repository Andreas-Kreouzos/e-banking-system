package com.ebanking.system.service;

import com.ebanking.system.dto.UserResponse;

/**
 * TODO: Add an appropriate description for the class
 *
 * @author Andrekreou
 */
public interface IUserService {

    UserResponse getUserById(Long id);
}
