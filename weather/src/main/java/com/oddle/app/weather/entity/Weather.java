package com.oddle.app.weather.entity;

import com.oddle.app.weather.constant.TemperatureType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "weather")
public class Weather extends BaseEntity {

    private static final long serialVersionUID = 5678849534434451853L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weather_id_seq_gen")
    @SequenceGenerator(name = "weather_id_seq_gen", sequenceName = "weather_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "temperature")
    private double temp;

    @Enumerated(EnumType.STRING)
    @Column(name = "temp_type")
    private TemperatureType tempType;

    @Column(name = "is_active")
    private boolean isActive;

    public Weather(String cityName, double temp, TemperatureType tempType, boolean isActive) {
        this.cityName = cityName;
        this.temp = temp;
        this.tempType = tempType;
        this.isActive = isActive;
    }

    public static Weather create() {
        return new Weather();
    }

    public Weather withCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public Weather withTemperature(double temp) {
        this.temp = temp;
        return this;
    }

    public Weather withTempType(TemperatureType tempType) {
        this.tempType = tempType;
        return this;
    }

    public Weather withActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }
}
