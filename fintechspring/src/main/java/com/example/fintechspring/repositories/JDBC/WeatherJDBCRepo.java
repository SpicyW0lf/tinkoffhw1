package com.example.fintechspring.repositories.JDBC;

import com.example.fintechspring.models.JDBC.WeatherJ;
import com.example.fintechspring.models.WeatherEntity;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class WeatherJDBCRepo {
    private final DataSource dataSource;
    private final RowMapper<WeatherJ> weatherMapper = (rs, rowNum) ->
        new WeatherJ(
                rs.getInt("id"),
                rs.getInt("temperature"),
                rs.getInt("type_id")
        );

    public List<WeatherJ> findAll() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            return findAll(conn);
        }
    }

    public List<WeatherJ> findAll(Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM weather"
        );
        statement.execute();
        List<WeatherJ> weathers = new ArrayList<>();
        ResultSet rs = statement.getResultSet();
        while (rs.next()) {
            weathers.add(weatherMapper.mapRow(rs, rs.getRow()));
        }
        return weathers;
    }

    public void save(WeatherJ weatherJ) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            save(weatherJ, conn);
        }
    }

    public void save(WeatherJ weatherJ, Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO weather(temperature, type_id) VALUES (?, ?)"
        );
        statement.setInt(1, weatherJ.getTemperature());
        statement.setInt(2, weatherJ.getTypeId());
        statement.executeUpdate();
    }

    public Optional<WeatherJ> findByTemperatureAndType(int temperature, int id) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            return findByTemperatureAndType(temperature, id, conn);
        }
    }

    public Optional<WeatherJ> findByTemperatureAndType(int temperature, int id, Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM weather WHERE temperature=? AND type_id=?"
        );
        statement.setInt(1, temperature);
        statement.setInt(2, id);
        statement.execute();
        ResultSet rs = statement.getResultSet();

        if (rs.next()) {
            return Optional.ofNullable(weatherMapper.mapRow(rs, rs.getRow()));
        }
        return Optional.empty();
    }

    public WeatherJ findById(int id) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT  * FROM weather WHERE id=?"
            );
            statement.setInt(1, id);
            statement.execute();

            ResultSet rs = statement.getResultSet();
            if (rs.next()) {
                return weatherMapper.mapRow(rs, rs.getRow());
            }

            return null;
        }
    }
}
