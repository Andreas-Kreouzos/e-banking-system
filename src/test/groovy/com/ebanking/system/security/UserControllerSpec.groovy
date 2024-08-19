package com.ebanking.system.security

import com.ebanking.system.controller.UserController
import com.ebanking.system.dto.UserRequest
import com.ebanking.system.service.IUserService
import jakarta.json.bind.Jsonb
import jakarta.json.bind.JsonbBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import util.TestFixtures

import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserController)
@Import(NoSecurityConfig)
class UserControllerSpec extends Specification {

    static final String REGISTER_URL = '/api/v1/demo/register'

    @Autowired
    MockMvc mockMvc

    @MockBean
    IUserService userService

    static Jsonb jsonb = JsonbBuilder.create()

    def 'Should register a new user successfully'() {
        given: 'a request'
        def userRequest = TestFixtures.createUserRequest()

        and: 'a response'
        def userResponse = TestFixtures.createUserResponse()

        when: 'calling the service'
        when(userService.createUser({ req ->
            req.firstName == userRequest.firstName &&
                    req.lastName == userRequest.lastName &&
                    req.email == userRequest.email &&
                    req.password == userRequest.password &&
                    req.afm == userRequest.afm &&
                    req.birthDate == userRequest.birthDate &&
                    req.mobileNumber == userRequest.mobileNumber &&
                    req.cardNumber == userRequest.cardNumber
        } as UserRequest)).thenReturn(userResponse)

        then: 'the user gets registered'
        mockMvc.perform(post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonb.toJson(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonb.toJson(userResponse)))
    }
}

@Configuration
@EnableWebSecurity
class NoSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF for testing
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().permitAll() // Allow all requests without authentication
                );
        return http.build();
    }
}
