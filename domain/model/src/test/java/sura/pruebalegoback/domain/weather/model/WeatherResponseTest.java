package sura.pruebalegoback.domain.weather.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class WeatherResponseTest {

    @Test
    void gettersAndSetters() {
        WeatherResponse wr = new WeatherResponse();
        HashMap<String, Object> current = new HashMap<>();
        HashMap<String, Object> hourly = new HashMap<>();
        HashMap<String, Object> daily = new HashMap<>();
        wr.setCurrent(current);
        wr.setHourly(hourly);
        wr.setDaily(daily);
        assertSame(current, wr.getCurrent());
        assertSame(hourly, wr.getHourly());
        assertSame(daily, wr.getDaily());
    }
}