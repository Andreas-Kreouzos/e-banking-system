package com.ebanking.system.security

import io.github.joke.spockmockable.Mockable
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import spock.lang.Specification

import java.lang.reflect.Field

@Mockable(className = 'JwtGrantedAuthoritiesConverter')
class JwtAuthConverterSpec extends Specification {

    JwtAuthConverter jwtAuthConverter
    Jwt jwt
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter

    def setup() {
        jwtGrantedAuthoritiesConverter = Mock()
        jwtAuthConverter = new JwtAuthConverter()
        setPrivateField(jwtAuthConverter, 'jwtGrantedAuthoritiesConverter', jwtGrantedAuthoritiesConverter)
        jwt = Mock()
    }

    def setPrivateField(Object target, String fieldName, Object value) {
        Field field = target.getClass().getDeclaredField(fieldName)
        field.setAccessible(true)
        field.set(target, value)
    }

    def 'Convert with default principal claim'() {
        given: 'the default principal claim'
        jwt.getClaim(JwtClaimNames.SUB) >> 'test-subject'

        and: 'the resource roles in the JWT'
        jwt.getClaim('resource_access') >> ['test-resource': ['roles': ['user', 'admin']]]
        jwtGrantedAuthoritiesConverter.convert(jwt) >> [new SimpleGrantedAuthority('ROLE_user'),
                                                        new SimpleGrantedAuthority('ROLE_admin')]

        when: 'the JWT is converted to an authentication token'
        AbstractAuthenticationToken token = jwtAuthConverter.convert(jwt)

        then: 'the token should not be null'
        token != null

        and: 'should contain the correct principal and roles'
        token.name == 'test-subject'
        token.authorities*.authority.containsAll(['ROLE_user', 'ROLE_admin'])
    }

    def 'Convert with custom principal claim'() {
        given: 'a custom principal claim'
        jwtAuthConverter.principleAttribute = 'custom-claim'
        jwt.getClaim('custom-claim') >> 'custom-principal'

        and: 'the resource roles in the JWT'
        jwt.getClaim('resource_access') >> ['test-resource': ['roles': ['user']]]
        jwtGrantedAuthoritiesConverter.convert(jwt) >> [new SimpleGrantedAuthority('ROLE_user')]

        when: 'the JWT is converted to an authentication token'
        AbstractAuthenticationToken token = jwtAuthConverter.convert(jwt)

        then: 'the token should not be null'
        token != null

        and: 'should contain the correct custom principal and roles'
        token.name == 'custom-principal'
        token.authorities*.authority.contains('ROLE_user')
    }

    def 'Extract resource roles with no resource access'() {
        given: 'a JWT that has no resource access claim'
        jwt.getClaim('resource_access') >> null

        when: 'resource roles are extracted from the JWT'
        def roles = jwtAuthConverter.extractResourceRoles(jwt)

        then: 'the roles should be empty'
        roles.isEmpty()
    }

    def 'Extract resource roles with no roles for resourceId'() {
        given: 'a JWT that has a resource access claim, but no roles for the specified resource ID'
        jwtAuthConverter.resourceId = 'missing-resource'
        jwt.getClaim("resource_access") >> ['test-resource': ['roles': ['user']]]

        when: 'resource roles are extracted from the JWT'
        def roles = jwtAuthConverter.extractResourceRoles(jwt)

        then: 'the roles should be empty'
        roles.isEmpty()
    }
}
