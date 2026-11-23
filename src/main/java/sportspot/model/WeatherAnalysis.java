package sportspot.model;

import java.util.List;

public record WeatherAnalysis(
        double temperature,
        double windSpeedKph,
        boolean isRaining,
        List<SportSuitability> sports
) {
    public record SportSuitability(String name, boolean suitable, List<String> reasons) {
    }
}
