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

    def 'Successfully getting the user with valid JWT token'() {
        given: 'the endpoint to get the first user'
        def uri = new URI("$appUrl/user/1")

        when: 'calling the endpoint'
        def request = HttpClientSetup.createGetRequest(uri)
        def response = client.send(request, HttpResponse.BodyHandlers.ofString())

        then: 'the response should be OK'
        response.statusCode() == 200

        and: 'contain the message specified'
        def responseBody = new JsonSlurper().parseText(response.body())
        responseBody.id == 1
        responseBody.username == 'testuser'
        responseBody.firstName == 'Test'
        responseBody.lastName == 'User'
        responseBody.email == 'testuser@example.com'
    }

    def '403 response when call the #specified endpoint with user role'() {
        given: 'resource endpoints for each request type'
        def actions = [
                adminRequest: { ->
                    client.send(HttpClientSetup.createGetRequest(URI.create("$appUrl/admin")), HttpResponse.BodyHandlers.ofString())
                }
        ]

        when: 'calling the endpoint'
        def response = actions[actionType].call()

        then: 'the response should be 403 Forbidden'
        response.statusCode() == 403

        where: 'various requests are being passed'
        actionType     | specified
        'adminRequest' | 'admin'
    }
}
