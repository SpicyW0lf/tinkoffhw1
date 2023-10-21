package com.example.fintechspring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "weather")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeatherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "temperature")
    private int temperature;
    @OneToMany(mappedBy = "weather", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<City> cities = new ArrayList<>();
    @ManyToOne(fetch = FetchType.EAGER)
    private WeatherType type;

    public WeatherEntity(int temperature, WeatherType type) {
        this.temperature = temperature;
        this.type = type;
    }

    public void addCity(City city) {
        this.cities.add(city);
        city.setWeather(this);
    }

    public void deleteCity(City city) {
        this.cities.remove(city);
        city.setWeather(null);
    }
}
