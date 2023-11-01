package com.example.fintechspring.repositories;

import com.example.fintechspring.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CityHiberRepository extends JpaRepository<City, Long> {
    List<City> findAllByName(String name);

    void deleteByNameAndDate(String name, LocalDateTime date);
    Optional<City> findByNameAndDate(String name, LocalDateTime date);
}
