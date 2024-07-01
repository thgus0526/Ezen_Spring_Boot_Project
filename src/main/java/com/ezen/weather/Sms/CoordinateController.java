package com.ezen.weather.Sms;

import com.ezen.weather.user.SiteUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CoordinateController {

    private final String KAKAO_API_KEY = "ff48f5bc5c2956d90f13ecfdaaafc13b";

    @PostMapping("/getCoordinates")
    public List<Coordinate> getCoordinates(@RequestBody List<SiteUser> userList){
        System.out.println("테스트테스트");
        List<Coordinate> coordinates = new ArrayList<>();
        for(SiteUser siteUser : userList){
            String address = siteUser.getAddressStreet();
            Coordinate coordinate = convertAddressToCoordinate(address);
            if (coordinate != null) {
                coordinates.add(coordinate);
            }
        }
        return coordinates;
    }

    private Coordinate convertAddressToCoordinate(String address){
        System.out.println("테스트테스트2222");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;

        try {
            ResponseEntity<KakaoApiResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, KakaoApiResponse.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                KakaoApiResponse apiResponse = response.getBody();
                if (apiResponse != null && !apiResponse.getDocuments().isEmpty()) {
                    KakaoApiDocument document = apiResponse.getDocuments().get(0);
                    System.out.println(document.getY());
                    System.out.println(document.getX());
                    return new Coordinate(Double.parseDouble(document.getY()), Double.parseDouble(document.getX()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class Coordinate{
        private double latitude;
        private double longitude;
    }
    static class KakaoApiResponse {
        private List<KakaoApiDocument> documents;

        public List<KakaoApiDocument> getDocuments() {
            return documents;
        }

        public void setDocuments(List<KakaoApiDocument> documents) {
            this.documents = documents;
        }
    }
    static class KakaoApiDocument {
        private String x;
        private String y;

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }
    }
}
