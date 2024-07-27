package com.ebanking.system.service;

import com.ebanking.system.dto.UserRequest;
import com.ebanking.system.dto.UserResponse;
import com.ebanking.system.entity.User;
import com.ebanking.system.repository.IUserRepository;
import org.springframework.stereotype.Service;

import static com.ebanking.system.mapper.CommonMapper.mapToUserResponse;

/**
 * @see IUserService
 */
@Service
public class UserService implements IUserService {

    private final IUserRepository repository;

    public UserService(IUserRepository repository) {
        this.repository = repository;
    }

    /**
     * @see IUserService#createUser
     */
    @Override
    public UserResponse createUser(UserRequest userRequest) {
        return null;
    }

    /**
     * @see IUserService#getUserById
     */
    @Override
    public UserResponse getUserById(Long id) {
        User user = repository.select(id);
        return mapToUserResponse(user);
    }
}
