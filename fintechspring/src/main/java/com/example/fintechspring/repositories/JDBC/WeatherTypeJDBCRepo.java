package com.example.fintechspring.repositories.JDBC;

import com.example.fintechspring.models.JDBC.WeatherTypeJ;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class WeatherTypeJDBCRepo {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<WeatherTypeJ> typeMapper = (rs, rowNum) ->
            new WeatherTypeJ(
                    rs.getInt("id"),
                    rs.getString("name")
            );


    public List<WeatherTypeJ> findAll() {
        return jdbcTemplate.query(
                """
                        SELECT * FROM type""",
                Map.of(),
                typeMapper
        );
    }

    public Optional<WeatherTypeJ> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    """
                        SELECT * FROM type WHERE name=:name""",
                Map.of("name", name),
                typeMapper
        ));
        } catch(EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public void save(String type) {
        jdbcTemplate.update(
                """
                        INSERT INTO type (name) values(:type)""",
                Map.of("type", type)
        );
    }

    public void update(String oldType, String newType) {
        jdbcTemplate.update(
                """
                        UPDATE type SET name=:newType WHERE name=:oldType""",
                Map.of(
                        "newType", newType,
                        "oldType", oldType
                )
        );
    }


    public void deleteType(String type) {
        jdbcTemplate.update(
                """
                        DELETE type WHERE name=:type""",
                Map.of("type", type)
        );
    }
}
