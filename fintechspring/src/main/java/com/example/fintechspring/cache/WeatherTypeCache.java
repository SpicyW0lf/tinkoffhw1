package com.example.fintechspring.cache;

import com.example.fintechspring.models.JDBC.WeatherTypeJ;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Getter
public class WeatherTypeCache {

    private final Map<String, WeatherTypeJ> cache = new HashMap<>();
    private final LinkedList<WeatherTypeJ> lru = new LinkedList<>();
    @Value("${cache.course.size}")
    private int size;

    synchronized public void put(WeatherTypeJ type) {
        if (cache.containsKey(type.getType())) {
            lru.removeIf(typeLru -> type.getType().equals(typeLru.getType()));
        }
        if (lru.size() == size) {
            WeatherTypeJ removed = lru.removeLast();
            cache.remove(removed.getType());
        }
        cache.put(type.getType(), type);
        lru.addFirst(type);
    }

    synchronized public Optional<WeatherTypeJ> get(String type) {
        if (!cache.containsKey(type)) {
            return Optional.empty();
        }
        lru.removeIf(typeLru -> type.equals(typeLru.getType()));
        lru.addFirst(cache.get(type));

        return Optional.of(cache.get(type));
    }

    synchronized public void delete(String type) {
        lru.removeIf(typeLru -> type.equals(typeLru.getType()));
        cache.remove(type);
    }

    synchronized public void clear() {
        lru.clear();
        cache.clear();
    }
}
