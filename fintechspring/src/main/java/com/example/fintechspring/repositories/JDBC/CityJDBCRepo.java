package com.example.fintechspring.repositories.JDBC;

import com.example.fintechspring.DTO.WeatherDataDTO.CityRequest;
import com.example.fintechspring.models.JDBC.CityJ;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Repository
public class CityJDBCRepo {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<CityJ> cityMapper = (rs, rowNum) ->
            new CityJ(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getTimestamp("date").toLocalDateTime(),
                    rs.getInt("weather_id")
            );

    public List<CityJ> findAll() {
        return jdbcTemplate.query(
                """
                        SELECT * FROM city""",
                Map.of(),
                cityMapper
        );
    }

    public void save(CityJ cityJ) {
        jdbcTemplate.update(
                """
                        INSERT INTO city(name, date, weather_id) VALUES (:name, :date, :id)""",
                Map.of(
                        "name", cityJ.getName(),
                        "date", cityJ.getDate(),
                        "id", cityJ.getWeatherId()
                )
        );
    }

    public void deleteByNameAndDate(CityRequest cr) {
        jdbcTemplate.update(
                """
                        DELETE city WHERE name=:name AND date=:date""",
                Map.of(
                        "name", cr.getName(),
                        "date", cr.getDate()
                )
        );
    }
}
