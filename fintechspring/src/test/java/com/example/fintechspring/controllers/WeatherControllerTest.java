package com.example.fintechspring.controllers;

import com.example.fintechspring.DTO.WeatherDTO;
import com.example.fintechspring.models.Weather;
import com.example.fintechspring.repositories.CityHiberRepository;
import com.example.fintechspring.repositories.WeatherHiberRepository;
import com.example.fintechspring.repositories.WeatherTypeHiberRepository;
import com.example.fintechspring.services.WeatherApiService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class WeatherControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    @MockBean
    private WeatherApiService weatherApiService;
    @Autowired
    private WeatherHiberRepository weatherHiberRepository;
    @Autowired
    private WeatherController controller;
    @Autowired
    private WeatherTypeHiberRepository weatherTypeHiberRepository;
    @Autowired
    private CityHiberRepository cityHiberRepository;
    @BeforeAll
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @BeforeEach
    void clear() {
        controller.getWeathers().clear();
        weatherHiberRepository.deleteAll();
        weatherTypeHiberRepository.deleteAll();
        cityHiberRepository.deleteAll();
    }

    @Test
    void getWeatherWithVariable() throws Exception {
        List<Weather> weathers = controller.getWeathers();
        String city = "Moscow";
        LocalDateTime time = LocalDate.now().atTime(12, 12, 12);
        weathers.add(Weather.of(city, 1, time));
        weathers.add(Weather.of(city, 2, time.plusMinutes(1)));
        weathers.add(Weather.of(city, 3, time.plusMinutes(2)));

        mockMvc.perform(get("/api/wheather/Moscow"))
                .andExpectAll(
                        status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                        {
                            "%s": [1],
                            "%s": [2],
                            "%s": [3]
                        }
                        """.formatted(
                                time.toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME),
                                time.toLocalTime().plusMinutes(1).format(DateTimeFormatter.ISO_LOCAL_TIME),
                                time.toLocalTime().plusMinutes(2).format(DateTimeFormatter.ISO_LOCAL_TIME)
                                )
                        )
                );
    }

    @Test
    void getWeatherWithOutVariable() throws Exception {
        List<Weather> weathers = new ArrayList<>();
        String city = "Moscow";
        LocalTime time = LocalTime.MIDNIGHT;

        Map<LocalTime, List<Integer>> result = Map.of(time, List.of(1, 2, 3));
        mockMvc.perform(get("/api/wheather/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void checkJdbcWithCity() throws Exception {
        WeatherDTO weatherDTO = new WeatherDTO(22, "2023-10-01 12:12");
        weatherDTO.setType("Sunny");
        weatherDTO.setName("Moscow");
        Mockito.when(weatherApiService.getWeatherByCity("Moscow")).thenReturn(weatherDTO);
        
        mockMvc.perform(get("/api/wheather/weather-with-jdbc?city=Moscow"))
                .andExpectAll(
                        status().isOk(),
                        content().json("""
                                        {
                                            "name": "Moscow",
                                            "date": "2023-10-01T12:12:00",
                                            "temperature": 22,
                                            "type": "Sunny"
                                        }
                                        """)
                );
    }


    @Test
    void postWeatherCorrect() throws Exception {
        WeatherDTO weather = new WeatherDTO(22, "2023-10-03 12:12");
        weather.setName("Moscow");
        weather.setDate(LocalDateTime.parse("2023-10-03T12:12:00"));
        mockMvc.perform(post("/api/wheather/Moscow")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "temperature": 22,
                            "date": "2023-10-03 12:12"
                        }
                        """)
        ).andExpectAll(
                status().isOk(),
                content().json("""
                                {
                                    "message": "Wheather was added successfully!"
                                }
                                """)
        );
    }

    @Test
    void postWeatherAlreadyExists() throws Exception {
        WeatherDTO weather = new WeatherDTO(22, "2023-10-03 12:12");
        weather.setName("Moscow");
        mockMvc.perform(post("/api/wheather/Moscow")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "temperature": 22,
                            "date": "2023-10-03 12:12"
                        }
                        """)
        ).andExpectAll(
                status().isBadRequest(),
                content().json("""
                                {
                                    "message": "This wheather is already exists, if you want to change it use PUT method"
                                }
                                """)
        );
    }
}