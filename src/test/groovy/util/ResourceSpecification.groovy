package util

import groovy.json.JsonSlurper
import jakarta.ws.rs.core.Application
import jakarta.ws.rs.core.Response
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.test.JerseyTest
import spock.lang.Specification

abstract class ResourceSpecification extends Specification {

    JerseyTest jerseyTest
    JsonSlurper jsonSlurper

    protected abstract createResource()

    def setup() {
        jerseyTest = new JerseyTest() {
            @Override
            protected Application configure() {
                new ResourceConfig()
                        .register(createResource())
            }
        }
        jerseyTest.setUp()
        jsonSlurper = new JsonSlurper()
    }

    def parse(Response response) {
        jsonSlurper.parseText(response.readEntity(String))
    }

    def cleanup() {
        jerseyTest.tearDown()
    }
}
