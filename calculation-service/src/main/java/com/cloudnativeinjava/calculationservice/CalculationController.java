package com.cloudnativeinjava.calculationservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = "calculate")
public class CalculationController {

    private RestTemplate restTemplate;

    @Autowired
    public CalculationController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/zero-curve")
    public Map getProductsByTypeAndName (@RequestParam(value = "type") final String type) throws IOException {
        String url = "http://DATA-SERVICE/data?type=" + type;
        Map result = restTemplate.getForObject(url, Map.class);
        Future future = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(result), Future.class);
        double spot = future.getSpot();

        Map response = new HashMap();
        for (Price price: future.getFuture()){
            double rate = getZeroRate(spot, price.getPrice(), price.getMonth());
            response.put(price.month, rate);
        }
        return response;
    }

    private Double getZeroRate(double spot, double futurePrice, int month){
        return ( ( ( futurePrice / spot ) - 1 ) * (12 / month) * 100 );
    }

    @Data
    public static class Future{
        private double spot;
        private List<Price> future;
    }

    @Data
    public static class Price{
        private int month;
        private double price;
    }
}


