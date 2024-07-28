package com.ebanking.system.controller;

import com.ebanking.system.dto.UserRequest;
import com.ebanking.system.dto.UserResponse;
import com.ebanking.system.service.IUserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * Handles the incoming requests for the users
 */
@RestController
@RequestMapping("/api/v1/demo")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final IUserService service;

    public UserController(IUserService service) {
        this.service = service;
    }

    /**
     * Register a new user.
     *
     * @param userRequest the request containing user details
     * @return the response entity with user details
     */
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest userRequest) {
        LOGGER.info("Received request: {}", userRequest);
        UserResponse user = service.createUser(userRequest);
        LOGGER.info("Returning response: {}", user);
        return ResponseEntity.status(CREATED).body(user);
    }

    /**
     * Retrieve user details by ID (client user).
     *
     * @param id the user ID
     * @return the response entity with user details
     */
    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<UserResponse> hello(@PathVariable("id") Long id) {
        UserResponse user = service.getUserById(id);
        return ResponseEntity.status(OK).body(user);
    }

    /**
     * Retrieve user details by ID (client admin).
     *
     * @param id the user ID
     * @return the response entity with user details
     */
    @GetMapping(value = "/admin/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<UserResponse> hello2(@PathVariable("id") Long id) {
        UserResponse user = service.getUserById(id);
        return ResponseEntity.status(OK).body(user);
    }
}
