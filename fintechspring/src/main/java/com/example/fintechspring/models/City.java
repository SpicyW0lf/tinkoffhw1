package com.example.fintechspring.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "city")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "date")
    private LocalDateTime date;
    @ManyToOne(fetch = FetchType.EAGER)
    private WeatherEntity weather;

    public City(String name, LocalDateTime date, WeatherEntity weather) {
        this.name = name;
        this.date = date;
        this.weather = weather;
    }
}
