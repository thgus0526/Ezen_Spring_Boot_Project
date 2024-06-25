package com.ezen.weather.weather;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherService {

    public String fetchWeatherData(double latitude, double longitude) {
        String apiKey = "RJPEUzqySY-TxFM6spmPRQ";
        String url = String.format(
                "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=pT92G96xAGF0VK2U3O0kj%%2BmVmHumwJTe08EgnL98rAQTQQxeaqyiD85Sx9nrgex5BOEZp81ZKdK3a1llX6TMfw%%3D%%3D&pageNo=1&numOfRows=1000&dataType=JSON&base_date=%s&base_time=1100&nx=%d&ny=%d",
                getFormattedDate(), latitude, longitude);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }

    private String getFormattedDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return today.format(formatter);
    }
}