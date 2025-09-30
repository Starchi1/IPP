package org.example.ipp_progect2.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private static final String API_KEY = "b44edf8f1b8e9f588dee31dcc1732f6e";
    private static final String CURRENT_URL =
            "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=ru";

    private static final String FORECAST_URL =
            "https://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s&units=metric&lang=ru";

    private static final String COORD_URL =
            "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric&lang=ru";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    // 1. Текущая погода
    public String getCurrentWeather(String city) {
        String response = restTemplate.getForObject(String.format(CURRENT_URL, city, API_KEY), String.class);
        try {
            JsonNode root = mapper.readTree(response);
            String description = root.path("weather").get(0).path("description").asText();
            double temp = root.path("main").path("temp").asDouble();
            return String.format("Сейчас в %s: %s, %.1f°C", city, description, temp);
        } catch (Exception e) {
            return "Ошибка при получении текущей погоды.";
        }
    }

    // 2. Прогноз на 5 дней
    public String getForecast(String city) {
        String response = restTemplate.getForObject(String.format(FORECAST_URL, city, API_KEY), String.class);
        try {
            JsonNode root = mapper.readTree(response);
            StringBuilder result = new StringBuilder("Прогноз погоды в " + city + ":\n");

            // выводим первые 3 прогноза (примерно на сутки)
            for (int i = 0; i < 3; i++) {
                JsonNode item = root.path("list").get(i);
                String time = item.path("dt_txt").asText();
                String description = item.path("weather").get(0).path("description").asText();
                double temp = item.path("main").path("temp").asDouble();
                result.append(String.format("%s: %s, %.1f°C%n", time, description, temp));
            }
            return result.toString();
        } catch (Exception e) {
            return "Ошибка при получении прогноза.";
        }
    }

    // 3. Погода по координатам
    public String getWeatherByCoords(double lat, double lon) {
        String response = restTemplate.getForObject(String.format(COORD_URL, lat, lon, API_KEY), String.class);
        try {
            JsonNode root = mapper.readTree(response);
            String city = root.path("name").asText();
            String description = root.path("weather").get(0).path("description").asText();
            double temp = root.path("main").path("temp").asDouble();
            return String.format("Погода по координатам [%.2f, %.2f] (%s): %s, %.1f°C",
                    lat, lon, city, description, temp);
        } catch (Exception e) {
            return "Ошибка при получении данных по координатам.";
        }
    }
}
