package com.example.fintechspring.repositories;

import com.example.fintechspring.models.WeatherEntity;
import com.example.fintechspring.models.WeatherType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherTypeHiberRepository extends JpaRepository<WeatherType, Long> {
    Optional<WeatherType> findByName(String name);
}
