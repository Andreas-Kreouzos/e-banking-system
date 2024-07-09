package util

import groovy.json.JsonSlurper
import jakarta.json.bind.Jsonb
import jakarta.json.bind.JsonbBuilder
import jakarta.ws.rs.core.MediaType

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class HttpClientSetup {

    static final String CLIENT_ID = PropertyReader.getProperty('client.id')
    static final String CLIENT_USERNAME = PropertyReader.getProperty('client.username')
    static final String CLIENT_PASSWORD = PropertyReader.getProperty('client.password')
    static final String GRANT_TYPE = PropertyReader.getProperty('grant.type')
    static final String URL_JWT_TOKEN = TestContainersSpec.keycloak.getAuthServerUrl() + "/realms/eBanking/protocol/openid-connect/token"

    static Jsonb jsonb = JsonbBuilder.create()

    def static createGetRequest(URI uri) {
        return getRequestBuilder(uri, "Bearer ${getAccessToken()}")
    }

    def static getRequestBuilder(URI uri, String headerValues) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .header("Authorization", headerValues ?: "")
                .GET()
                .build()
    }

    def static createPostRequestWithoutToken(URI uri, def request) {
        return postRequestBuilder(uri, null, request)
    }

    def static createPostRequest(URI uri, def request) {
        return postRequestBuilder(uri, "Bearer ${getAccessToken()}", request)
    }

    def static postRequestBuilder(URI uri, String headerValues, def request) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .header("Authorization", headerValues ?: "")
                .POST(HttpRequest.BodyPublishers.ofString(jsonb.toJson(request)))
                .build()
    }

    def static getAccessToken() {
        HttpClient client = HttpClient.newBuilder().build()

        String requestBody = "grant_type=$GRANT_TYPE&client_id=$CLIENT_ID&username=$CLIENT_USERNAME&password=$CLIENT_PASSWORD"

        def response = client.send(HttpRequest.newBuilder()
                .uri(new URI(URL_JWT_TOKEN))
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build(),
                HttpResponse.BodyHandlers.ofString())

        def json = new JsonSlurper().parseText(response.body())

        return json.access_token
    }
}
