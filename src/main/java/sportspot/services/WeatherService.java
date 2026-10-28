package sportspot.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sportspot.model.WeatherData;

import java.util.List;

@Service
public class WeatherService {

    private static final String API_KEY = "ec02bdd102167057d6426c8ccffcc4df";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    private final RestClient restClient;

    public WeatherService() {
        this.restClient = RestClient.create();;
    }

    public WeatherData getWeatherData(String city) {
        OpenWeatherResponse response = restClient.get()
                .uri(BASE_URL + "?q={city}&appid={key}&units=metric", city, API_KEY)
                .retrieve()
                .body(OpenWeatherResponse.class); // Spring автоматично парсва JSON-а към нашия обект

        if (response == null) {
            throw new RuntimeException("Неуспешно извличане на данните за времето");
        }

        double temp = response.main().temp();
        double windKph = response.wind().speed() * 3.6;
        boolean isRaining = response.weather().stream().anyMatch(w -> w.main().equalsIgnoreCase("Rain"));
        return new WeatherData(temp, windKph, isRaining);
    }

    private record OpenWeatherResponse(MainData main, WindData wind, List<WeatherDesc> weather) {}
    private record MainData(double temp) {}
    private record WindData(double speed) {}
    private record WeatherDesc(String main) {}
}
