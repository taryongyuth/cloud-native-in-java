package com.cloudnativeinjava.calculationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @CrossOrigin
    @GetMapping("/zero-curve")
    public List getProductsByTypeAndName (@RequestParam(value = "type") final String type) throws IOException {
        String url = "http://DATA-SERVICE/data?type=" + type;
        Map result = restTemplate.getForObject(url, Map.class);
        Future future = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(result), Future.class);
        double spot = future.getSpot();

        List response = new ArrayList();
        for (Price price: future.getFuture()){
            double rate = getZeroRate(spot, price.getPrice(), price.getMonth());
            Map map = new HashMap();
            map.put("month", price.month);
            map.put("zero rate", rate);
            response.add(map);
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


