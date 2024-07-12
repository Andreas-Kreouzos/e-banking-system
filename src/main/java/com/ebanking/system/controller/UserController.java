package com.ebanking.system.controller;

import com.ebanking.system.dto.UserResponse;
import com.ebanking.system.service.IUserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

/**
 * TODO: Add an appropriate description for the class
 */
@RestController
@RequestMapping("/api/v1/demo")
public class UserController {

    private final IUserService service;

    public UserController(IUserService service) {
        this.service = service;
    }

    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<UserResponse> hello(@PathVariable("id") Long id) {
        UserResponse user = service.getUserById(id);
        return ResponseEntity.status(OK).body(user);
    }

    @GetMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('client_admin')")
    public String hello2() {
        return "{\"message\": \"Hello World from Admin Keycloak\"}";
    }
}
