package com.example.fintechspring.services;

import com.example.fintechspring.models.JDBC.WeatherTypeJ;
import com.example.fintechspring.repositories.JDBC.CityJDBCRepo;
import com.example.fintechspring.repositories.JDBC.WeatherJDBCRepo;
import com.example.fintechspring.repositories.JDBC.WeatherTypeJDBCRepo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
class WeatherJdbcServiceTest {

    @Container
    public static GenericContainer h2 = new GenericContainer(DockerImageName.parse("oscarfonts/h2"))
            .withExposedPorts(1521, 81)
            .withEnv("H2_OPTIONS", "-ifNotExists")
            .waitingFor(Wait.defaultWaitStrategy());

    @BeforeAll
    public static void beforeAll() {
        h2.start();
    }

    @AfterAll
    public static void afterAll() {
        h2.stop();
    }

    @BeforeEach
    void beforeEach() throws SQLException {
        weatherTypeJDBCRepo.deleteAll();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:h2:tcp://localhost:" + h2.getMappedPort(1521) + "/test");
    }

    @Autowired
    private WeatherTypeJDBCRepo weatherTypeJDBCRepo;
    @Autowired
    private WeatherJdbcService service;

    @Test
    void findAllTypes() throws SQLException {
        List<WeatherTypeJ> weathers = weatherTypeJDBCRepo.findAll();
        assertEquals(0, weathers.size());
    }

    @Test
    void createType() throws SQLException {
        weatherTypeJDBCRepo.save("Snowy");
        List<WeatherTypeJ> weatherTypes = weatherTypeJDBCRepo.findAll();
        assertEquals(1, weatherTypes.size());
    }

    @Test
    void updateType() throws SQLException {
        weatherTypeJDBCRepo.save("Sunny");
        service.updateType("Sunny", "Snowy");
        assertEquals("Snowy", weatherTypeJDBCRepo.findAll().get(0).getType());
    }

    @Test
    void deleteType() throws SQLException {
        weatherTypeJDBCRepo.save("Sunny");
        assertEquals(1, weatherTypeJDBCRepo.findAll().size());
        service.deleteType("Sunny");
        assertEquals(0, weatherTypeJDBCRepo.findAll().size());
    }
}