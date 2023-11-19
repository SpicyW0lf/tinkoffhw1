package com.example.fintechspring.cache;

import com.example.fintechspring.controllers.WeatherJDBCController;
import com.example.fintechspring.models.JDBC.WeatherTypeJ;
import com.example.fintechspring.services.WeatherJdbcService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class WeatherTypeCacheTest {
    @MockBean
    private WeatherJdbcService jdbcService;
    @Autowired
    private WeatherTypeCache cache;
    @Autowired
    private WeatherJDBCController controller;

    @BeforeEach
    void beforeEach() {
        cache.clear();
    }

    @Test
    void getIfNotInCash() throws Exception {
        assertEquals(0, cache.getCache().size());
        assertEquals(0, cache.getLru().size());
        WeatherTypeJ typeJ = new WeatherTypeJ(1, "Sunny");
        Mockito.when(jdbcService.findType("Sunny")).thenReturn(typeJ);

        controller.findType("Sunny");
        assertEquals(typeJ, cache.get("Sunny").get());
    }

    @Test
    void getIfInCache() throws Exception {
        WeatherTypeJ typeJ = new WeatherTypeJ(1, "Sunny");
        cache.put(typeJ);

        controller.findType("Sunny");

        verify(jdbcService, times(0)).findType("Sunny");
    }

    @Test
    void getIfDeleted() throws Exception {
        cache.put(new WeatherTypeJ(1, "Sunny"));
        assertEquals(1, cache.getCache().size());

        controller.deleteType("Sunny");

        assertEquals(0, cache.getCache().size());
    }

    @Test
    void getIfAdded() throws Exception {
        assertEquals(0, cache.getCache().size());

        when(jdbcService.findType("Sunny")).thenReturn(new WeatherTypeJ(1, "Sunny"));
        controller.createType("Sunny");

        assertEquals(1, cache.getCache().size());
    }

    @Test
    void getIfWarmed() {
        WeatherTypeJ typeJ = new WeatherTypeJ(1, "Sunny");
        WeatherTypeJ typeJ2 = new WeatherTypeJ(2, "Rainy");

        cache.put(typeJ);
        cache.put(typeJ2);
        assertNotEquals(cache.getLru().getFirst(), typeJ);
        cache.get(typeJ.getType());
        assertEquals(cache.getLru().getFirst(), typeJ);
    }

    @Test
    void deleteIfFull() {
        cache.put(new WeatherTypeJ("1"));
        cache.put(new WeatherTypeJ("2"));
        cache.put(new WeatherTypeJ("3"));
        cache.put(new WeatherTypeJ("4"));
        cache.put(new WeatherTypeJ("5"));

        assertTrue(cache.getCache().containsKey("1"));
        cache.put(new WeatherTypeJ("6"));
        assertFalse(cache.getCache().containsKey("1"));
    }

    @Test
    void deleteIfDelete() {
        cache.put(new WeatherTypeJ("Sunny"));

        assertTrue(cache.get("Sunny").isPresent());
        cache.delete("Sunny");
        assertTrue(cache.get("Sunny").isEmpty());
    }
}