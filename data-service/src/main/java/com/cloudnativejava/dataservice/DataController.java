package com.cloudnativejava.dataservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("data")
@RefreshScope
@Validated
public class DataController {

    private ObjectMapper mapper = new ObjectMapper();

    @GetMapping
    public Map getPrice(@RequestParam(value = "type", required = true, defaultValue = "gold") final String type) throws IOException {
        Map response = new HashMap();
        if (type.equals("gold") || type.equals("silver") || type.equals("platinum")){
            response = readJsonFileByType(type);
        }else{
            response.putIfAbsent("Code", "Type Not found");
        }
        return response;
    }

    private Map readJsonFileByType(String fileName) throws IOException{
        return mapper.readValue(new File(getClass().getResource("/" + fileName + ".json").getFile()), Map.class);
    }
}
