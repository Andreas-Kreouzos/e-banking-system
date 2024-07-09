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

    def setup() {
        client = HttpClient.newHttpClient()
        jsonb = JsonbBuilder.create()
    }

    def 'Successfully call the user endpoint with valid JWT token'() {
        given: 'the application uri'
        def uri = new URI("http://localhost:${port}/api/v1/demo")

        when: 'calling the endpoint'
        def request = HttpClientSetup.createGetRequest(uri)
        def response = client.send(request, HttpResponse.BodyHandlers.ofString())

        then: 'the response should be OK'
        response.statusCode() == 200

        and: 'contain the message specified'
        def responseBody = new JsonSlurper().parseText(response.body())
        responseBody.message == 'Hello World from Keycloak'
    }
}
