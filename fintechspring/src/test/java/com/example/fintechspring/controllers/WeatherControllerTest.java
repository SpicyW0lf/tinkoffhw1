package com.example.fintechspring.controllers;

import ch.qos.logback.core.testUtil.MockInitialContext;
import com.example.fintechspring.DTO.WeatherDTO;
import com.example.fintechspring.DTO.WeatherDataDTO.CityRequest;
import com.example.fintechspring.DTO.WeatherDataDTO.WeatherRequest;
import com.example.fintechspring.models.Weather;
import com.example.fintechspring.services.CityHiberService;
import com.example.fintechspring.services.WeatherApiService;
import com.example.fintechspring.services.WeatherJdbcService;
import com.example.fintechspring.services.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(WeatherController.class)
@ActiveProfiles("test")
class WeatherControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private WeatherService weatherService;
    @MockBean
    private WeatherApiService weatherApiService;
    @MockBean
    private WeatherJdbcService weatherJdbcService;
    @MockBean
    private CityHiberService cityHiberService;

    @Test
    void getWeatherWithVariable() throws Exception {
        List<Weather> weathers = new ArrayList<>();
        String city = "Moscow";
        LocalTime time = LocalTime.MIDNIGHT;

        Map<LocalTime, List<Integer>> result = Map.of(time, List.of(1, 2, 3));
        Mockito.when(weatherService.findTemperaturesForToday(weathers, city)).thenReturn(result);
        mockMvc.perform(get("/api/wheather/Moscow"))
                .andExpectAll(
                        status().isOk(),
                        content().json("""
                        {
                            "00:00": [1, 2, 3]
                        }
                        """)
                );
    }

    @Test
    void getWeatherWithOutVariable() throws Exception {
        List<Weather> weathers = new ArrayList<>();
        String city = "Moscow";
        LocalTime time = LocalTime.MIDNIGHT;

        Map<LocalTime, List<Integer>> result = Map.of(time, List.of(1, 2, 3));
        Mockito.when(weatherService.findTemperaturesForToday(weathers, city)).thenReturn(result);
        mockMvc.perform(get("/api/wheather/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void checkJdbcWithCity() throws Exception {
        WeatherDTO weatherDTO = new WeatherDTO(22, "2023-10-01 12:12");
        weatherDTO.setType("Sunny");
        weatherDTO.setName("Moscow");
        Mockito.when(weatherApiService.getWeatherByCity("Moscow")).thenReturn(weatherDTO);
        Mockito.doNothing().when(weatherJdbcService).createCity(Mockito.any());
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
        Mockito.verify(weatherJdbcService).createCity(Mockito.any());
    }


    @Test
    void postWeatherCorrect() throws Exception {
        WeatherDTO weather = new WeatherDTO(22, "2023-10-03 12:12");
        List<Weather> weathers = new ArrayList<>();
        weather.setName("Moscow");
        weather.setDate(LocalDateTime.parse("2023-10-03T12:12:00"));
        Mockito.when(weatherService.createWeather(Mockito.any(), Mockito.any())).thenReturn(true);
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
        List<Weather> weathers = new ArrayList<>();
        weather.setName("Moscow");
        Mockito.when(weatherService.createWeather(Mockito.any(), Mockito.any())).thenReturn(false);
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