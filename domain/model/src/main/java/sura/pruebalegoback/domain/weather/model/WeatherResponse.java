package sura.pruebalegoback.domain.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    @JsonProperty("current")
    private Map<String, Object> current;

    @JsonProperty("hourly")
    private Map<String, Object> hourly;

    @JsonProperty("daily")
    private Map<String, Object> daily;

    public Map<String, Object> getCurrent() { return current; }
    public void setCurrent(Map<String, Object> current) { this.current = current; }
    public Map<String, Object> getHourly() { return hourly; }
    public void setHourly(Map<String, Object> hourly) { this.hourly = hourly; }
    public Map<String, Object> getDaily() { return daily; }
    public void setDaily(Map<String, Object> daily) { this.daily = daily; }
}