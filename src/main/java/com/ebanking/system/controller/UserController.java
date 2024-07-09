package com.ebanking.system.controller;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: Add an appropriate description for the class
 */
@RestController
@RequestMapping("/api/v1/demo")
public class UserController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('client_user')")
    public String hello() {
        return "{\"message\": \"Hello World from Keycloak\"}";
    }

    @GetMapping(value = "/hello-2", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('client_admin')")
    public String hello2() {
        return "{\"message\": \"Hello World from Admin Keycloak\"}";
    }
}
