package com.oddle.app.weather.repository;

import com.oddle.app.weather.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

    @Query(
            value = "SELECT * FROM WEATHER w WHERE w.city_name = :cityName AND (DATE(w.created_date) BETWEEN " +
                    "DATE_SUB(NOW(), INTERVAL 1 DAY) AND " +
                    "DATE_SUB(NOW(), INTERVAL 2 DAY))" +
                    "AND w.is_active = 1",
            nativeQuery = true)
    Collection<Weather> findAllByIntervalADay(@Param("cityName") String cityName);

    @Query(
            value = "SELECT * FROM WEATHER w WHERE w.city_name = :cityName AND (DATE(w.created_date) BETWEEN " +
                    "DATE_SUB(NOW(), INTERVAL 1 DAY) AND " +
                    "DATE_SUB(NOW(), INTERVAL 7 DAY))" +
                    "AND w.is_active = 1",
            nativeQuery = true)
    Collection<Weather> findAllByIntervalAWeek(@Param("cityName") String cityName);

    @Query(
            value = "SELECT * FROM WEATHER w WHERE w.city_name = :cityName AND (DATE(w.created_date) BETWEEN " +
                    "DATE_SUB(NOW(), INTERVAL 1 MONTH) AND " +
                    "DATE_SUB(NOW(), INTERVAL 2 MONTH))" +
                    "AND w.is_active = 1",
            nativeQuery = true)
    Collection<Weather> findAllByIntervalAMonth(@Param("cityName") String cityName);

    @Query(
            value = "SELECT * FROM WEATHER w WHERE w.city_name = :cityName AND (DATE(w.created_date) BETWEEN " +
                    "DATE_SUB(NOW(), INTERVAL 1 YEAR) AND " +
                    "DATE_SUB(NOW(), INTERVAL 2 YEAR))" +
                    "AND w.is_active = 1",
            nativeQuery = true)
    Collection<Weather> findAllByIntervalAYear(@Param("cityName") String cityName);
}
