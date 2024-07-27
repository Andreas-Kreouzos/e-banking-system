package com.ebanking.system.controller

import com.ebanking.system.dto.UserResponse
import com.ebanking.system.service.IUserService
import jakarta.ws.rs.client.Entity
import spock.lang.Subject
import util.ResourceSpecification
import util.TestFixtures

class UserControllerSpec extends ResourceSpecification {

    static final String REGISTER_URL = '/api/v1/demo/register'

    @Subject
    UserController controller

    IUserService service

    @Override
    protected createResource() {
        service = Mock()
        controller = new UserController(service)
    }

    def 'Successfully calling the preview endpoint'() {
        given: 'a valid request'
        def request = TestFixtures.createUserRequest()

        and: 'an expected response'
        def expectedResponse = TestFixtures.createUserResponse()

        when: 'the resource is called'
        def response = jerseyTest.target(REGISTER_URL).request().post(Entity.json(request))

        then: 'response should have status 201'
        response.getStatus() == 201

        and: 'the cancel preview service is called once with the correct request parameters'
        1 * service.createUser({
            it.firstName == request.firstName &&
                    it.lastName == request.lastName &&
                    it.email == request.email &&
                    it.password == request.password &&
                    it.afm == request.afm &&
                    it.birthDate == request.birthDate &&
                    it.mobileNumber == request.mobileNumber &&
                    it.cardNumber == request.cardNumber
        }) >> expectedResponse

        and: 'the actual response should contain the expected values'
        def actualResponse = response.readEntity(UserResponse.class)
        actualResponse == expectedResponse
    }
}
