package util

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dasniko.testcontainers.keycloak.KeycloakContainer
import groovy.sql.Sql
import org.jetbrains.annotations.NotNull
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.slf4j.LoggerFactory
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.DockerClientFactory
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.ext.ScriptUtils
import org.testcontainers.jdbc.JdbcDatabaseDelegate
import spock.lang.Shared
import spock.lang.Specification

import javax.sql.DataSource
import java.time.Duration

class TestContainersSpec extends Specification {

    public MySQLContainer mySQL

    public static KeycloakContainer keycloak

    public static Network network = createReusableNetwork('e-banking-network')

    @Shared
    Sql sql

    @Shared
    DataSource dataSource

    def setup() {
        mySQL = new MySQLContainer<>("mysql:8.0.28")
                .withDatabaseName('E_BANKING_SYSTEM')
                .withExposedPorts(3306)
                .withNetwork(network)
                .withReuse(true)
                .withStartupTimeout(Duration.ofMinutes(3))
                .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("mySQL")))
        mySQL.start()

        def delegate = new JdbcDatabaseDelegate(mySQL, "")
        ScriptUtils.runInitScript(delegate, "cleanup.sql")
        ScriptUtils.runInitScript(delegate, "schema.sql")

        sql = Sql.newInstance(mySQL.jdbcUrl, mySQL.username, mySQL.password, mySQL.driverClassName)

        HikariConfig config = new HikariConfig()
        config.jdbcUrl = mySQL.jdbcUrl
        config.username = mySQL.username
        config.password = mySQL.password
        config.driverClassName = mySQL.driverClassName
        config.maximumPoolSize = 5
        dataSource = new HikariDataSource(config)

        keycloak = new KeycloakContainer('quay.io/keycloak/keycloak:25.0.1')
                .withRealmImportFile('realm-export.json')
                .withNetwork(network)
                .withReuse(true)
                .withStartupTimeout(Duration.ofMinutes(3))
                .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger('keycloak')))
        keycloak.start()

        String keycloakIssuerUri = keycloak.getAuthServerUrl() + "/realms/eBanking"
        System.setProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri", keycloakIssuerUri)
    }

    @DynamicPropertySource
    static void registerKeycloakProperties(DynamicPropertyRegistry registry) {
        registry.add('spring.security.oauth2.resourceserver.jwt.issuer-uri',
                () -> keycloak.getAuthServerUrl() + "/realms/eBanking")
        registry.add('keycloak.auth-server-url', keycloak::getAuthServerUrl)
        registry.add('keycloak.realm', () -> "eBanking")
        registry.add('keycloak.resource', () -> "e-banking-system")
    }

    static Network createReusableNetwork(String name) {
        String id = DockerClientFactory.instance().client().listNetworksCmd().exec().stream()
                .filter(network -> network.getName().equals(name)
                        && network.getLabels() == DockerClientFactory.DEFAULT_LABELS)
                .map(com.github.dockerjava.api.model.Network::getId)
                .findFirst()
                .orElseGet(() -> DockerClientFactory.instance().client().createNetworkCmd()
                        .withName(name)
                        .withOptions(['mtu': '1350'])
                        .withCheckDuplicate(true)
                        .withLabels(DockerClientFactory.DEFAULT_LABELS)
                        .exec().getId())

        return new Network() {
            @Override
            String getId() {
                return id
            }

            @Override
            void close() {
            }

            @Override
            Statement apply(@NotNull Statement statement, @NotNull Description description) {
                return statement
            }
        }
    }
}
