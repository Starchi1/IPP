package org.example.ipp_progect2.controller;


import org.example.ipp_progect2.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    private final WeatherService weatherService;
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // 1. Текущая погода
    @GetMapping("/weather/current")
    public String current(@RequestParam String city) {
        return weatherService.getCurrentWeather(city);
    }

    // 2. Прогноз
    @GetMapping("/weather/forecast")
    public String forecast(@RequestParam String city) {
        return weatherService.getForecast(city);
    }

    // 3. По координатам
    @GetMapping("/weather/coords")
    public String coords(@RequestParam double lat, @RequestParam double lon) {
        return weatherService.getWeatherByCoords(lat, lon);
    }
}
