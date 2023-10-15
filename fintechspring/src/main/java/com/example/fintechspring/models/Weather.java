package com.example.fintechspring.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Data
public class Weather {
    private static final Map<String, Long> areas = new HashMap<>();
    private static Long nextId = 1L;
    private Long id;
    private String name;
    private Integer temperature;
    private LocalDateTime date;

    public static Weather of(String name, Integer temp, LocalDateTime date) {
        long nowId;

        if (areas.containsKey(name)) {
            nowId = areas.get(name);
        } else {
            nowId = nextId;
            areas.put(name, nextId++);
        }

        return new Weather(nowId, name, temp, date);
    }
}
