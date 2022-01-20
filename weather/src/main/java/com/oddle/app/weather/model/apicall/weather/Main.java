package com.oddle.app.weather.model.apicall.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Main implements Serializable {

    private static final long serialVersionUID = -5611495708155925138L;

    private int id;

    private String main;

    private String description;

    private String icon;
}
