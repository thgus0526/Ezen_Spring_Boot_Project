package com.ezen.weather.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public String getWeather(@RequestParam("lat") double latitude,
                             @RequestParam("lon") double longitude, Model model) {
        String weatherData = weatherService.fetchWeatherData(latitude, longitude);
        model.addAttribute("weatherData", weatherData);
        return "weather";
    }
}