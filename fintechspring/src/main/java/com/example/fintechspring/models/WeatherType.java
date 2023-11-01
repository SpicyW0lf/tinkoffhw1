package com.example.fintechspring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "type")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeatherType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<WeatherEntity> weatherEntities = new ArrayList<>();

    public WeatherType(String name) {
        this.name = name;
    }

    public void addWeather(WeatherEntity we) {
        this.weatherEntities.add(we);
        we.setType(this);
    }

    public void deleteWeather(WeatherEntity we) {
        this.weatherEntities.remove(we);
        we.setType(null);
    }
}
