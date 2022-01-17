package com.oddle.app.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TodayWeatherResponse implements Serializable {

    private static final long serialVersionUID = 1209936581661731719L;

    private Long id;
    private String cityName;
    private double temp;
    private String tempType;
    private String requestedOn;

    public static TodayWeatherResponse create() {
        return new TodayWeatherResponse();
    }

    public TodayWeatherResponse withCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public TodayWeatherResponse withTemp(double temp) {
        this.temp = temp;
        return this;
    }

    public TodayWeatherResponse withTempType(String tempType) {
        this.tempType = tempType;
        return this;
    }

    public TodayWeatherResponse withRequestedOn(String requestedOn) {
        this.requestedOn = requestedOn;
        return this;
    }

    public TodayWeatherResponse build() {
        return new TodayWeatherResponse(id, cityName, temp, tempType, requestedOn);
    }
}
