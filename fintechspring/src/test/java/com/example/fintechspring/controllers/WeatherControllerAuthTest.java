package com.example.fintechspring.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WeatherControllerAuthTest {


    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeAll
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

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
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser("dimon")
    void postWeatherWithAuthentication() throws Exception {
        mockMvc.perform(post("/api/wheather/Moscow"))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin")
    void postWeatherWithAdminAuthentication() throws Exception {
        mockMvc.perform(post("/api/wheather/Moscow"))
                .andExpect(authenticated())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void putWeatherWithoutAuthentication() throws Exception {
        mockMvc.perform(put("/api/wheather/Moscow"))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser("dimon")
    void putWeatherWithAuthentication() throws Exception {
        mockMvc.perform(put("/api/wheather/Moscow"))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin")
    void putWeatherWithAdminAuthentication() throws Exception {
        mockMvc.perform(put("/api/wheather/Moscow"))
                .andExpect(authenticated())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteWeatherWithoutAuthentication() throws Exception {
        mockMvc.perform(delete("/api/wheather/Moscow"))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser("dimon")
    void deleteWeatherWithAuthentication() throws Exception {
        mockMvc.perform(delete("/api/wheather/Moscow"))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin")
    void deleteWeatherWithAdminAuthentication() throws Exception {
        mockMvc.perform(delete("/api/wheather/Moscow"))
                .andExpect(authenticated())
                .andExpect(status().isBadRequest());
    }
}