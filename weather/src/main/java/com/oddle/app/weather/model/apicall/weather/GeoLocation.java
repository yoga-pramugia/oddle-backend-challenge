package com.oddle.app.weather.model.apicall.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeoLocation implements Serializable {

    private static final long serialVersionUID = -2598561625710038242L;

    private int type;

    private int id;

    private String country;

    private int sunrise;

    private int sunset;
}
