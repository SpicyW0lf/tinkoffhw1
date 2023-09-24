package ru.petrov.fintech;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Weather {
    private static final Map<String, Long> areas = new HashMap<>();
    private static Long nextId = 1L;
    private final Long id;
    private final String name;
    private Integer temp;
    private Date date;

    private Weather(Long id, String name, Integer temp, Date date) {
        this.id = id;
        this.name = name;
        this.temp = temp;
        this.date = date;
    }

    public static Weather of(String name, Integer temp, Date date) {
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
