package com.oddle.app.weather.model.apicall.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Wind implements Serializable {

    private static final long serialVersionUID = -4624378497917984071L;

    private double speed;

    private int deg;
}
