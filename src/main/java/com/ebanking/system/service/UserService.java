package com.ebanking.system.service;

import com.ebanking.system.dto.UserResponse;
import com.ebanking.system.entity.User;
import com.ebanking.system.repository.IUserRepository;
import org.springframework.stereotype.Service;

import static com.ebanking.system.mapper.CommonMapper.mapToUserResponse;

/**
 * TODO: Add an appropriate description for the class
 *
 * @author Andrekreou
 */
@Service
public class UserService implements IUserService {

    private final IUserRepository repository;

    public UserService(IUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = repository.select(id);
        return mapToUserResponse(user);
    }
}
