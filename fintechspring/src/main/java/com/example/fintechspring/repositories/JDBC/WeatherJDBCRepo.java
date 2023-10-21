package com.example.fintechspring.repositories.JDBC;

import com.example.fintechspring.models.JDBC.WeatherJ;
import com.example.fintechspring.models.WeatherEntity;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class WeatherJDBCRepo {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<WeatherJ> weatherMapper = (rs, rowNum) ->
        new WeatherJ(
                rs.getInt("id"),
                rs.getInt("temperature"),
                rs.getInt("type_id")
        );

    public List<WeatherJ> findAll() {
        return jdbcTemplate.query(
                """
                        SELECT * FROM weather""",
                Map.of(),
                weatherMapper
        );
    }

    public void save(WeatherJ weatherJ) {
        jdbcTemplate.update(
                """
                        INSERT INTO weather(temperature, type_id) VALUES (:temperature, :typeId)""",
                Map.of(
                        "temperature", weatherJ.getTemperature(),
                        "typeId", weatherJ.getTypeId()
                )
        );
    }

    public Optional<WeatherJ> findByTemperatureAndType(int temperature, int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    """
                        SELECT * FROM weather WHERE temperature=:temp AND type_id=:id""",
                Map.of(
                        "temp", temperature,
                        "id", id
                ),
                weatherMapper
        ));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }
}
