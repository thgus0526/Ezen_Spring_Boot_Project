package com.ezen.weather.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherData {
    private Main main;
    private Wind wind;
    // 필요한 다른 필드들도 추가할 수 있습니다.
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Main {
    private Double temp;
    private Integer humidity;
    // 필요한 다른 필드들도 추가할 수 있습니다.
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Wind {
    private Double speed;
    private Integer deg;
    // 필요한 다른 필드들도 추가할 수 있습니다.
}
