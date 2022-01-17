package com.oddle.app.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oddle.app.weather.constant.TemperatureType;
import com.oddle.app.weather.model.TodayWeatherResponse;
import com.oddle.app.weather.model.WeatherRequest;
import com.oddle.app.weather.service.WeatherService;
import com.oddle.app.weather.utility.DateHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WeatherController.class, useDefaultFilters = false)
public class WeatherControllerTest {
    private MockMvc mockMvc;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;


    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController)
                .build();
    }

    @Test
    public void getWeatherTodayByCity() throws Exception {
        Mockito.when(weatherService.getTodayWeather(anyString()))
                .thenReturn(TodayWeatherResponse.create()
                        .withCityName("Jakarta")
                        .withTemp(32.1)
                        .withTempType(TemperatureType.CELSIUS.name())
                        .withRequestedOn(DateHelper.getFormattedLocalDate(LocalDateTime
                                .ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()),
                                        ZoneId.systemDefault()))));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/weather?cityName=name")
                        .param("name", "Jakarta")
                        .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cityName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.temp").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tempType").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestedOn").isNotEmpty());
    }

    @Test
    public void saveWeatherTodayByCity() throws Exception {
        Mockito.doNothing().when(weatherService).saveWeather(any(WeatherRequest.class));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/v1/weather").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .writeValueAsString(new WeatherRequest(1L, "Jakarta", 28.2,
                                "Celsius", "2020-09-01")));

        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }

    @Test
    public void getWeatherByPeriod() throws Exception {
        Mockito.when(weatherService.getWeathersByPeriod(anyString(), anyString()))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/weather/period?cityName=name")
                        .param("name", "Jakarta")
                        .param("period", "Day")
                        .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(status().isOk());
    }

    @Test
    public void deactivateWeather() throws Exception {
        Mockito.doNothing().when(weatherService).deactivateWeather(anyLong());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/v1/weather/deactive?id=1")
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE));

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void updateWeatherPeriod() throws Exception {
        Mockito.doNothing().when(weatherService).updateWeatherPeriod(any());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/v1/weather/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .writeValueAsString(new WeatherRequest(1L, "Jakarta", 28.2,
                                "Celsius", "2020-09-01")));

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }
}
