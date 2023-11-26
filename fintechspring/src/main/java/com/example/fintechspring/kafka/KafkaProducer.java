package com.example.fintechspring.kafka;

import com.example.fintechspring.kafka.events.WeatherEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    @Value("${kafka.topic-name}")
    private String topic;

    private final KafkaTemplate<String, WeatherEvent> kafkaTemplate;

    public void sendWeatherEvent(WeatherEvent event) {
        String key = event.getKey();
        kafkaTemplate.send(topic, key, event);
    }
}
