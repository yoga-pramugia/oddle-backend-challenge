package com.oddle.app.weather.service;

import com.oddle.app.weather.constant.TemperatureType;
import com.oddle.app.weather.entity.Weather;
import com.oddle.app.weather.model.TodayWeatherResponse;
import com.oddle.app.weather.model.WeatherRequest;
import com.oddle.app.weather.model.apicall.APIWeatherResponse;
import com.oddle.app.weather.repository.WeatherRepository;
import com.oddle.app.weather.utility.DateHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.oddle.app.weather.utility.DateHelper.getFormattedLocalDate;

@Service
public class WeatherService {

    private static final Logger LOGGER = LogManager.getLogger(WeatherService.class.getName());

    private final RestTemplate restTemplate;
    private final WeatherRepository weatherRepository;

    public WeatherService(RestTemplate restTemplate, WeatherRepository weatherRepository) {
        this.restTemplate = restTemplate;
        this.weatherRepository = weatherRepository;
    }

    @Value("${api.weather.url}")
    private String apiWeatherUrl;

    @Value("${api.weather.key}")
    private String apiWeatherKey;

    /**
     * Get today weather by city name
     * Temperature currently default in Celsius (C)
     * @param city name of city
     */
    public TodayWeatherResponse getTodayWeather(String city) {
        LOGGER.info("Start - getWeatherToday for city : {}", city);
        double temp = 0;

        APIWeatherResponse data = getWeatherFromAPI(city);
        if (data.getTemperature() != null) {
            //convert Kelvin into Celsius (C = K - 273.15)
            temp = data.getTemperature().getTemp() - 273.15;
        }

        LOGGER.info("Finish - getWeatherToday success");

        return TodayWeatherResponse
                .create()
                .withCityName(data.getName())
                .withTemp(temp)
                .withTempType(TemperatureType.CELSIUS.name())
                .withRequestedOn(getFormattedLocalDate(LocalDateTime
                        .ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault())))
                .build();
    }

    /**
     * Save weather from api
     * @param request request from api
     */
    @Transactional
    public void saveWeather(WeatherRequest request) {
        LOGGER.info("Start - saveWeather with request: {}", request);

        LOGGER.info("Set value into weather");
        Weather weather = Weather
                .create()
                .withCityName(request.getCityName())
                .withTemperature(request.getTemp())
                .withTempType(request.getType().equalsIgnoreCase(TemperatureType.CELSIUS.name())
                        ? TemperatureType.CELSIUS : TemperatureType.KELVIN)
                .withActive(true);

        weatherRepository.save(weather);
        LOGGER.info("Finish - saveWeather success");
    }

    /**
     * Get collection of weathers based on period (DAY, MONTH, YEAR)
     * @param cityName  name of city
     * @param period DAY, MONTH, YEAR
     */
    @Transactional(readOnly = true)
    public Collection<TodayWeatherResponse> getWeathersByPeriod(String cityName, String period) {
        LOGGER.info("Start - getWeatherByPeriod with city: {}, period: {}", cityName, period);

        LOGGER.info("Get list of weather by period");
        Collection<Weather> weathers = new ArrayList<>();
        switch (period.toUpperCase()) {
            case "DAY":
                weathers.addAll(weatherRepository.findAllByIntervalADay(cityName));
                break;
            case "MONTH":
                weathers.addAll(weatherRepository.findAllByIntervalAMonth(cityName));
                break;
            case "YEAR":
                weathers.addAll(weatherRepository.findAllByIntervalAYear(cityName));
                break;
        }

        LOGGER.info("Success get list of weather by period");
        return weathers.stream().map(weather -> {
            TodayWeatherResponse response = TodayWeatherResponse.create();
            response.setId(weather.getId());
            response.withCityName(weather.getCityName());
            response.withTemp(weather.getTemp());
            response.withTempType(weather.getTempType().name());
            return response;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Deactivate weather data based on id
     * @param id id from table Weather
     */
    @Transactional
    public void deactivateWeather(Long id) {
        LOGGER.info("Start - deactivateWeather with id: {}", id);
        Weather weather = weatherRepository.getOne(id);
        weather.setActive(false);
        LOGGER.info("Finish - deactivateWeather success");
    }

    /**
     * Update period of weather based on id
     * @param request weather request
     */
    @Transactional
    public void updateWeatherPeriod(WeatherRequest request) {
        LOGGER.info("Start - updateWeatherPeriod with id: {}", request.getId());
        Weather weather = weatherRepository.getOne(request.getId());
        weather.setCreatedDate(DateHelper.setFormattedDate(request.getDate()));
        LOGGER.info("Finish - updateWeatherPeriod success");
    }

    /**
     * Get Weather from api.openweathermap.org
     * @param city name of the city
     */
    private APIWeatherResponse getWeatherFromAPI(String city) {
        LOGGER.info("Start - APIWeatherResponse with city: {}", city);
        APIWeatherResponse result = new APIWeatherResponse();

        LOGGER.info("Set uri parameter");
        Map<String, Object> params = new HashMap<>();
        params.put("cityName", city);
        params.put("apiKey", apiWeatherKey);

        try {
            ResponseEntity<APIWeatherResponse> response = restTemplate.getForEntity(apiWeatherUrl, APIWeatherResponse.class, params);
            result = response.getBody();
        } catch (RestClientException rex) {
            LOGGER.warn("Couldn't get data from url: {}", apiWeatherUrl);
        }

        LOGGER.info("Finish - APIWeatherResponse with response: {}", result);
        return result;
    }

}
