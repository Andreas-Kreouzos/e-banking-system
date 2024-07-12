package com.ebanking.system.mapper;

import com.ebanking.system.dto.UserResponse;
import com.ebanking.system.entity.User;

public class CommonMapper {

    public static UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
