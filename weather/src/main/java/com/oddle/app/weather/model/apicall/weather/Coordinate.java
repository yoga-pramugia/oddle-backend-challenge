package com.oddle.app.weather.model.apicall.weather;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Coordinate implements Serializable {

    private static final long serialVersionUID = 1502197689636040415L;

    private double lon;
    private double lat;
}
