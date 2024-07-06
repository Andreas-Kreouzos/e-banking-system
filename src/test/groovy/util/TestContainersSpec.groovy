package util

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import groovy.sql.Sql
import org.jetbrains.annotations.NotNull
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.slf4j.LoggerFactory
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

    @Shared
    public static MySQLContainer mySQL

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
                .withReuse(true).withInitScript()
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
