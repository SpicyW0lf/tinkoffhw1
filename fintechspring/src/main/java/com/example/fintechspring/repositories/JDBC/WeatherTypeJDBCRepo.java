package com.example.fintechspring.repositories.JDBC;

import com.example.fintechspring.models.JDBC.WeatherTypeJ;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class WeatherTypeJDBCRepo {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final RowMapper<WeatherTypeJ> typeMapper = (rs, rowNum) ->
            new WeatherTypeJ(
                    rs.getInt("id"),
                    rs.getString("name")
            );

    public List<WeatherTypeJ> findAll() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            return findAll(conn);
        }
    }

    public List<WeatherTypeJ> findAll(Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM type"
        );
        statement.execute();

        ResultSet rs = statement.getResultSet();
        List<WeatherTypeJ> types = new ArrayList<>();
        while (rs.next()) {
            types.add(typeMapper.mapRow(rs, rs.getRow()));
        }

        return types;
    }

    public Optional<WeatherTypeJ> findByName(String name) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            return findByName(name, conn);
        }
    }

    public Optional<WeatherTypeJ> findByName(String name, Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM type WHERE name=?"
        );
        statement.setString(1, name);
        statement.execute();
        ResultSet rs = statement.getResultSet();
        if (rs.next()) {
            return Optional.ofNullable(typeMapper.mapRow(rs, rs.getRow()));
        }
        return Optional.empty();
    }

    public void save(String type) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            save(type, conn);
        }
    }

    public void save(String type, Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO type (name) values(?)"
        );
        statement.setString(1, type);
        statement.executeUpdate();
    }

    public void update(String oldType, String newType) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            update(oldType, newType, conn);
        }
    }

    public void update(String oldType, String newType, Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "UPDATE type SET name=? WHERE name=?"
        );
        statement.setString(1, newType);
        statement.setString(2, oldType);
        statement.executeUpdate();
    }

    public void deleteType(String type) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            deleteType(type, conn);
        }
    }

    public void deleteType(String type, Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "DELETE type WHERE name=?"
        );
        statement.setString(1, type);
        statement.executeUpdate();
    }
}
