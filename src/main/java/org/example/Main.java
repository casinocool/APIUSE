package org.example;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://94.198.50.185:7081/api/users";

        try {
            // 1. GET запрос для получения session cookie
            ResponseEntity<List> getResponse = restTemplate.getForEntity(baseUrl, List.class);
            String sessionCookie = getResponse.getHeaders().getFirst("Set-Cookie").split(";")[0];

            // 2. Настройка заголовков с session cookie
            HttpHeaders headers = new HttpHeaders();
            headers.set("Cookie", sessionCookie);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 3. POST запрос - создать пользователя
            String userJson = "{\"id\":3,\"name\":\"James\",\"lastName\":\"Brown\",\"age\":25}";
            HttpEntity<String> postRequest = new HttpEntity<>(userJson, headers);
            ResponseEntity<String> postResponse = restTemplate.postForEntity(baseUrl, postRequest, String.class);
            String codePart1 = postResponse.getBody();
            System.out.println("Part 1: " + codePart1);

            // 4. PUT запрос - обновить пользователя
            String updatedUserJson = "{\"id\":3,\"name\":\"Thomas\",\"lastName\":\"Shelby\",\"age\":25}";
            HttpEntity<String> putRequest = new HttpEntity<>(updatedUserJson, headers);
            ResponseEntity<String> putResponse = restTemplate.exchange(
                    baseUrl, HttpMethod.PUT, putRequest, String.class);
            String codePart2 = putResponse.getBody();
            System.out.println("Part 2: " + codePart2);

            // 5. DELETE запрос - удалить пользователя
            HttpEntity<String> deleteRequest = new HttpEntity<>(headers);
            ResponseEntity<String> deleteResponse = restTemplate.exchange(
                    baseUrl + "/3", HttpMethod.DELETE, deleteRequest, String.class);
            String codePart3 = deleteResponse.getBody();
            System.out.println("Part 3: " + codePart3);

            // 6. Объединить код
            String finalCode = codePart1 + codePart2 + codePart3;
            System.out.println("\nFinal code: " + finalCode);
            System.out.println("Code length: " + finalCode.length());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}