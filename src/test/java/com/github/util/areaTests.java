package com.github.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.SearchHouseApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class areaTests extends SearchHouseApplicationTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testArea() throws IOException {

        String data = "北京";

        JsonNode jsonNode = objectMapper.readTree(new File("src/main/resources/static/lib/json/city_region.json"));

        // 设置区划代码
        for (JsonNode item : jsonNode) {
            if (item.get("name").toString().contains("浦东")) {
                System.out.println( item.get("id").asText());
            }
        }

    }
}
