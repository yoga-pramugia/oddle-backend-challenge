package com.oddle.app.weather.controller;

import com.oddle.app.weather.model.TodayWeatherResponse;
import com.oddle.app.weather.model.WeatherRequest;
import com.oddle.app.weather.service.WeatherService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RestController
public class WeatherController {

    private static final Logger LOGGER = LogManager.getLogger(WeatherController.class.getName());

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("")
    public Map<String, Object> getWeathers() {
        return Collections.singletonMap("message", "Welcome to Oddle Backend Challenge");
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/v1/weather", produces = MediaType.APPLICATION_JSON_VALUE)
    public TodayWeatherResponse getWeatherTodayByCity(@RequestParam(name = "cityName") String cityName){
        LOGGER.info("getWeatherTodayByCity - cityName: {}", cityName);
        return weatherService.getTodayWeather(cityName);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/v1/weather", produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveWeatherTodayByCity(@RequestBody WeatherRequest request){
        LOGGER.info("saveWeatherTodayByCity - request: {}", request);

        weatherService.saveWeather(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/v1/weather/period", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<TodayWeatherResponse> getWeatherByPeriod(@RequestParam(name = "cityName") String cityName,
                                                               @RequestParam(name = "period") String period){
        LOGGER.info("getWeatherByPeriod - cityName: {} and period: {}", cityName, period);

        return weatherService.getWeathersByPeriod(cityName, period);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/v1/weather/deactive", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deactivateWeather(@RequestParam(name = "id") String id){
        LOGGER.info("deactivateWeather - id: {}", id);

        weatherService.deactivateWeather(Long.valueOf(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/v1/weather/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateWeatherPeriod(@RequestBody WeatherRequest request){
        LOGGER.info("updateWeatherPeriod - request: {}", request);

        weatherService.updateWeatherPeriod(request);
    }
}