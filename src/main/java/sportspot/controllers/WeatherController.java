package sportspot.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sportspot.model.WeatherData;
import sportspot.services.WeatherService;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    public final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/(city)")
    public WeatherData getWeatherData(String city) {
        return weatherService.getWeatherData(city);
    }
}
