package com.oddle.app.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherRequest implements Serializable {

    private static final long serialVersionUID = -3904730221137853952L;

    private Long id;

    private String cityName;

    private double temp;

    private String type;

    private String date; //format ISO_LOCAL_DATE('2011-12-03')
}
