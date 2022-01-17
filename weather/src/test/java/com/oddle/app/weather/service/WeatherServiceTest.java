package com.oddle.app.weather.service;

import com.oddle.app.weather.constant.TemperatureType;
import com.oddle.app.weather.entity.Weather;
import com.oddle.app.weather.model.TodayWeatherResponse;
import com.oddle.app.weather.model.WeatherRequest;
import com.oddle.app.weather.model.apicall.APIWeatherResponse;
import com.oddle.app.weather.model.apicall.weather.*;
import com.oddle.app.weather.repository.WeatherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;

public class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WeatherRepository weatherRepository;

    @Mock
    private WeatherService weatherService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        weatherService = new WeatherService(restTemplate, weatherRepository);
        ReflectionTestUtils.setField(weatherService, "apiWeatherUrl", "TEST_URL");
        ReflectionTestUtils.setField(weatherService, "apiWeatherKey", "TEST_KEY");
    }

    @Test
    public void getTodayWeather() {
        Mockito.when(restTemplate.getForEntity(anyString(), any(), anyMap()))
                .thenReturn(new ResponseEntity(getDummyResponse(), HttpStatus.OK));

        TodayWeatherResponse response = weatherService.getTodayWeather("Jakarta");
        Assertions.assertAll(
                () -> Assertions.assertNotNull(response.getCityName()),
                () -> Assertions.assertNotNull(response.getTemp()),
                () -> Assertions.assertNotNull(response.getTempType()),
                () -> Assertions.assertNotNull(response.getRequestedOn()));
    }

    @Test
    public void getTodayWeatherGotRestClientException() {
        Mockito.when(restTemplate.getForEntity(anyString(), any(), anyMap()))
                .thenThrow(new RestClientException("Error Occupation"));

        TodayWeatherResponse response = weatherService.getTodayWeather("Jakarta");
        Assertions.assertAll(
                () -> Assertions.assertNull(response.getId()),
                () -> Assertions.assertNull(response.getCityName()),
                () -> Assertions.assertEquals(0.0, response.getTemp()),
                () -> Assertions.assertEquals(TemperatureType.CELSIUS.name(), response.getTempType()),
                () -> Assertions.assertNotNull(response.getRequestedOn()));
    }

    @Test
    public void saveWeather() {
        WeatherRequest request = new WeatherRequest(1L, "Jakarta", 28.2, "Celsius", "");

        Assertions.assertAll(() -> Assertions.assertEquals("Jakarta", request.getCityName()),
                () -> Assertions.assertEquals(28.2, request.getTemp()),
                () -> Assertions.assertEquals("Celsius", request.getType()));

        weatherService.saveWeather(request);
    }

    @Test
    public void getWeathersByPeriodDay() {
        String cityName = "Jakarta";
        String period = "Day";

        Mockito.when(
                weatherRepository.findAllByIntervalADay(cityName))
                .thenReturn(getDummyCollection());

        Collection<TodayWeatherResponse> responses = weatherService.getWeathersByPeriod(cityName, period);
        assertThat(responses, hasItem(allOf(
                hasProperty("cityName", is("Jakarta")),
                hasProperty("temp", is(32.1)),
                hasProperty("tempType", is(TemperatureType.CELSIUS.name()))
        )));

    }

    @Test
    public void getWeathersByPeriodMonth() {
        String cityName = "Jakarta";
        String period = "Month";

        Mockito.when(
                weatherRepository.findAllByIntervalAMonth(cityName))
                .thenReturn(getDummyCollection());

        Collection<TodayWeatherResponse> responses = weatherService.getWeathersByPeriod(cityName, period);
        assertThat(responses, hasItem(allOf(
                hasProperty("cityName", is("Jakarta")),
                hasProperty("temp", is(32.1)),
                hasProperty("tempType", is(TemperatureType.CELSIUS.name()))
        )));

    }

    @Test
    public void getWeathersByPeriodYear() {
        String cityName = "Jakarta";
        String period = "Year";

        Mockito.when(
                weatherRepository.findAllByIntervalAYear(cityName))
                .thenReturn(getDummyCollection());

        Collection<TodayWeatherResponse> responses = weatherService.getWeathersByPeriod(cityName, period);
        assertThat(responses, hasItem(allOf(
                hasProperty("cityName", is("Jakarta")),
                hasProperty("temp", is(32.1)),
                hasProperty("tempType", is(TemperatureType.CELSIUS.name()))
        )));

    }

    @Test
    public void deactivateWeather() {
        Long id = 1L;

        Weather weather =
                new Weather("Jakarta", 32.1, TemperatureType.CELSIUS, true);
        weather.setId(1L);

        Assertions.assertEquals(1L, id);
        Mockito.when(weatherRepository.getOne(id)).thenReturn(weather);
        weatherService.deactivateWeather(id);
    }

    @Test
    public void updateWeatherPeriod() {
        WeatherRequest request = new WeatherRequest(1L, "Jakarta", 28.2, "Celsius", "2020-09-01");

        Weather weather =
                new Weather("Jakarta", 32.1, TemperatureType.CELSIUS, true);
        weather.setId(1L);

        Assertions.assertEquals(1L,  weather.getId());
        Mockito.when(weatherRepository.getOne(weather.getId())).thenReturn(weather);
        weatherService.updateWeatherPeriod(request);
    }

    public APIWeatherResponse getDummyResponse() {
        APIWeatherResponse response = new APIWeatherResponse();
        response.setCoordinate(new Coordinate(106.8451, -6.2146));
        response.setMain(new Main(721, "Haze", "haze", "50n"));
        response.setBase("stations");
        response.setTemperature(new Temperature(300.72, 305.59, 300.2, 303.44, 1011, 83));
        response.setVisibility(5000);
        response.setWind(new Wind(1.54, 140));
        response.setClouds(new Clouds(40));
        response.setDt(1642166804);
        response.setGeoLocation(new GeoLocation(1, 9383, "ID", 1642114076, 1642158879));
        response.setTimezone(25200);
        response.setName("Jakarta");
        response.setCod(200);

        return response;
    }

    public Collection<Weather> getDummyCollection() {
        Collection<Weather> weathers = new ArrayList<>();
        Weather weather1 =
                new Weather("Jakarta", 32.1, TemperatureType.CELSIUS, true);
        Weather weather2 =
                new Weather("Jakarta", 33.0, TemperatureType.CELSIUS, true);
        Weather weather3 =
                new Weather("Jakarta", 32.1, TemperatureType.CELSIUS, true);
        weathers.add(weather1);
        weathers.add(weather2);
        weathers.add(weather3);
        return weathers;
    }

}
