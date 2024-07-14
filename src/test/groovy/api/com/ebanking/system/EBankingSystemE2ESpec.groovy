package api.com.ebanking.system

import com.ebanking.system.EBankingSystemApplication
import groovy.json.JsonSlurper
import jakarta.json.bind.Jsonb
import jakarta.json.bind.JsonbBuilder
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import spock.lang.Shared
import util.HttpClientSetup
import util.TestContainersSpec

import java.net.http.HttpClient
import java.net.http.HttpResponse

import static util.HttpClientSetup.CLIENT_USERNAME
import static util.HttpClientSetup.CLIENT_ADMIN_NAME

@SpringBootTest(classes = EBankingSystemApplication, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EBankingSystemE2ESpec extends TestContainersSpec {

    @Shared
    HttpClient client

    @LocalServerPort
    int port

    @Shared
    Jsonb jsonb

    def appUrl

    def setup() {
        appUrl = "http://localhost:${port}/api/v1/demo"
        client = HttpClient.newHttpClient()
        jsonb = JsonbBuilder.create()
    }

    def 'Successfully getting the users with valid JWT tokens'() {
        given: 'the endpoints called with the appropriate roles'
        def actions = [
                userRequest : { ->
                    client.send(HttpClientSetup.createGetRequest(URI.create("$appUrl/user/1"), CLIENT_USERNAME), HttpResponse.BodyHandlers.ofString())
                },
                adminRequest: { ->
                    client.send(HttpClientSetup.createGetRequest(URI.create("$appUrl/admin/2"), CLIENT_ADMIN_NAME), HttpResponse.BodyHandlers.ofString())
                }
        ]

        when: 'calling them'
        def response = actions[actionType].call()

        then: 'the response should be OK'
        response.statusCode() == 200

        and: 'response contain the correct parameters'
        def responseBody = new JsonSlurper().parseText(response.body())
        responseBody.id == id
        responseBody.username == username
        responseBody.firstName == firstName
        responseBody.lastName == lastName
        responseBody.email == email

        where: 'various parameters are being provided'
        actionType     | id | username  | firstName | lastName   | email
        'userRequest'  | 1  | 'andreas' | 'Andreas' | 'Kreouzos' | 'andreas.kreouzos@hotmail.com'
        'adminRequest' | 2  | 'toni'    | 'Toni'    | 'Stark'    | 'toni.stark@hotmail.com'
    }

    def '403 response when call the #specified endpoint with #different role'() {
        given: 'the endpoints called with the wrong role'
        def actions = [
                adminRequest: { ->
                    client.send(HttpClientSetup.createGetRequest(URI.create("$appUrl/admin/2"), CLIENT_USERNAME), HttpResponse.BodyHandlers.ofString())
                },
                userRequest : { ->
                    client.send(HttpClientSetup.createGetRequest(URI.create("$appUrl/user/1"), CLIENT_ADMIN_NAME), HttpResponse.BodyHandlers.ofString())
                }
        ]

        when: 'calling them'
        def response = actions[actionType].call()

        then: 'the response should be 403 Forbidden'
        response.statusCode() == 403

        where: 'various requests are being passed'
        actionType     | specified | different
        'adminRequest' | 'admin'   | 'user'
        'userRequest'  | 'user'    | 'admin'
    }
}
