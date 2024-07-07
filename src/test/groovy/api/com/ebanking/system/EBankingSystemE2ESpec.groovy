package api.com.ebanking.system

import groovy.json.JsonSlurper
import jakarta.json.bind.Jsonb
import jakarta.json.bind.JsonbBuilder
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import util.HttpClientSetup
import util.TestContainersSpec

import java.net.http.HttpClient
import java.net.http.HttpResponse

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class EBankingSystemE2ESpec extends TestContainersSpec {

    @Shared
    HttpClient client

    @Shared
    Jsonb jsonb

    @LocalServerPort
    int port

    def setup() {
        client = HttpClient.newHttpClient()
        jsonb = JsonbBuilder.create()
    }

    def 'Successfully return permissions using the valid racfId US39 from JWT Token'() {
        given: 'the application uri'
        def uri = new URI("http://localhost:${port}/api/v1/demo")
        println("Requesting URI: $uri")

        when: 'calling the endpoint'
        def request = HttpClientSetup.createGetRequest(uri)
        def response = client.send(request, HttpResponse.BodyHandlers.ofString())

        then: 'the response should be OK'
        println("Response Status: ${response.statusCode()}")
        println("Response Body: ${response.body()}")
        response.statusCode() == 200
        def responseBody = new JsonSlurper().parseText(response.body())
        println(responseBody)
    }
}