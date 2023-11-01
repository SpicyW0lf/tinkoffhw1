package com.example.fintechspring.repositories.JDBC;

import com.example.fintechspring.DTO.WeatherDataDTO.CityRequest;
import com.example.fintechspring.models.JDBC.CityJ;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Repository
public class CityJDBCRepo {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final RowMapper<CityJ> cityMapper = (rs, rowNum) ->
            new CityJ(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getTimestamp("date").toLocalDateTime(),
                    rs.getInt("weather_id")
            );

    public List<CityJ> findAll() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            return findAll(conn);
        }
    }

    public List<CityJ> findAll(Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM city");
        statement.execute();
        ResultSet rs = statement.getResultSet();
        List<CityJ> cities = new ArrayList<>();
        while (rs.next()) {
            cities.add(cityMapper.mapRow(rs, rs.getRow()));
        }
        return cities;
    }

    public void save(CityJ cityJ) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            save(cityJ, conn);
        }
    }

    public void save(CityJ cityJ, Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO city(name, date, weather_id) VALUES (?, ?, ?)"
        );
        statement.setString(1, cityJ.getName());
        statement.setTimestamp(2, Timestamp.valueOf(cityJ.getDate()));
        statement.setInt(3, cityJ.getWeatherId());
        statement.executeUpdate();
    }

    public void deleteByNameAndDate(CityRequest cr) throws SQLException {
        try(Connection conn = dataSource.getConnection()) {
            deleteByNameAndDate(cr, conn);
        }
    }

    public void deleteByNameAndDate(CityRequest cr, Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "DELETE city WHERE name=? AND date=?"
        );
        statement.setString(1, cr.getName());
        statement.setTimestamp(2, Timestamp.valueOf(cr.getDate()));
        statement.executeUpdate();
    }
}
