package com.oddle.app.weather.model.apicall.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Clouds implements Serializable {

    private static final long serialVersionUID = -2373461542974631928L;

    private int all;
}
