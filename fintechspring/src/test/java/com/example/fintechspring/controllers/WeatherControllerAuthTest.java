package com.example.fintechspring.controllers;

import com.example.fintechspring.models.User;
import com.example.fintechspring.repositories.UserRepository;
import com.example.fintechspring.services.CityHiberService;
import com.example.fintechspring.services.WeatherApiService;
import com.example.fintechspring.services.WeatherJdbcService;
import com.example.fintechspring.services.WeatherService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(WeatherController.class)
@ActiveProfiles("test")
class WeatherControllerAuthTest {
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
    public void getWeatherWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/wheather/Moscow"))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser("dimon")
    public void getWeatherWithAuthentication() throws Exception {
        mockMvc.perform(get("/api/wheather/Moscow"))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @Test
    void checkJdbcWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/wheather/weather-with-jdbc"))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser("dimon")
    void checkJdbcWithAuthentication() throws Exception {
        mockMvc.perform(get("/api/wheather/weather-with-jdbc"))
                .andExpect(authenticated())
                .andExpect(status().isBadRequest());
    }

    @Test
    void checkHiberWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/wheather/weather-with-hiber"))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser("dimon")
    void checkHiberWithAuthentication() throws Exception {
        mockMvc.perform(get("/api/wheather/weather-with-hiber"))
                .andExpect(authenticated())
                .andExpect(status().isBadRequest());
    }

    @Test
    void postWeatherWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/api/wheather/Moscow"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser("dimon")
    void postWeatherWithAuthentication() throws Exception {
        mockMvc.perform(post("/api/wheather/Moscow"))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "dimon", roles = {"ADMIN"})
    void postWeatherWithAdminAuthentication() throws Exception {
        mockMvc.perform(post("/api/wheather/Moscow"))
                .andExpect(authenticated())
                .andExpect(status().isBadRequest());
    }

    @Test
    void putWeatherWithoutAuthentication() throws Exception {
        mockMvc.perform(put("/api/wheather/Moscow"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser("dimon")
    void putWeatherWithAuthentication() throws Exception {
        mockMvc.perform(put("/api/wheather/Moscow"))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "dimon", roles = {"ADMIN"})
    void putWeatherWithAdminAuthentication() throws Exception {
        mockMvc.perform(put("/api/wheather/Moscow"))
                .andExpect(authenticated())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteWeatherWithoutAuthentication() throws Exception {
        mockMvc.perform(delete("/api/wheather/Moscow"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser("dimon")
    void deleteWeatherWithAuthentication() throws Exception {
        mockMvc.perform(delete("/api/wheather/Moscow"))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "dimon", roles = {"ADMIN"})
    void deleteWeatherWithAdminAuthentication() throws Exception {
        mockMvc.perform(delete("/api/wheather/Moscow"))
                .andExpect(authenticated())
                .andExpect(status().isBadRequest());
    }
}