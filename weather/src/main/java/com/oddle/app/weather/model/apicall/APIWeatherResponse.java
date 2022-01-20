package com.oddle.app.weather.model.apicall;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oddle.app.weather.model.apicall.weather.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class APIWeatherResponse implements Serializable {

    private static final long serialVersionUID = 8747195730399871995L;

    @JsonProperty("coord") private Coordinate coordinate;

    @JsonProperty("weather") private Main main;

    private String base;

    @JsonProperty("main") private Temperature temperature;

    private int visibility;

    private Wind wind;

    private Clouds clouds;

    private int dt;

    @JsonProperty("sys") private GeoLocation geoLocation;

    private int timezone;

    private int id;

    private String name;

    private int cod;
}
