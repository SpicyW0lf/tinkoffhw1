package com.example.fintechspring.services;

import com.example.fintechspring.DTO.WeatherDataDTO.CityRequest;
import com.example.fintechspring.DTO.WeatherDataDTO.WeatherRequest;
import com.example.fintechspring.exceptions.RuntimeSQLException;
import com.example.fintechspring.models.JDBC.CityJ;
import com.example.fintechspring.models.JDBC.WeatherJ;
import com.example.fintechspring.models.JDBC.WeatherTypeJ;
import com.example.fintechspring.repositories.JDBC.CityJDBCRepo;
import com.example.fintechspring.repositories.JDBC.WeatherJDBCRepo;
import com.example.fintechspring.repositories.JDBC.WeatherTypeJDBCRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class WeatherJdbcService {
    private final WeatherTypeJDBCRepo weatherTypeJDBCRepo;
    private final WeatherJDBCRepo weatherJDBCRepo;
    private final CityJDBCRepo cityJDBCRepo;
    private final DataSource dataSource;

    public List<WeatherTypeJ> findAllTypes() throws SQLException {
        return weatherTypeJDBCRepo.findAll();
    }

    public void createType(String type) throws SQLException {
        weatherTypeJDBCRepo.save(type);
    }

    public void updateType(String oldType, String newType) throws SQLException {
        weatherTypeJDBCRepo.update(oldType, newType);
    }

    public List<WeatherJ> findAllWeather() throws SQLException {
        return weatherJDBCRepo.findAll();
    }

    public WeatherJ findWeatherById(int id) throws SQLException {
        return weatherJDBCRepo.findById(id);
    }

    public void createWeather(WeatherRequest wr) throws SQLException {
        WeatherTypeJ type = weatherTypeJDBCRepo.findByName(wr.getType()).orElseThrow(NoSuchElementException::new);
        weatherJDBCRepo.save(new WeatherJ(wr.getTemperature(), type.getId()));
    }

    public void deleteType(String type) throws SQLException {
        weatherTypeJDBCRepo.deleteType(type);
    }

    public List<CityJ> findAllCities() throws SQLException {
        return cityJDBCRepo.findAll();
    }

    public List<CityJ> findAllCitiesByName(String name) throws SQLException {
        return cityJDBCRepo.findAllByName(name);
    }

    public WeatherTypeJ findType(String type) throws SQLException {
        return weatherTypeJDBCRepo.findByName(type).orElseThrow(SQLException::new);
    }

    public void createCity(CityRequest cr) throws SQLException {
        Connection conn = dataSource.getConnection();
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            WeatherTypeJ type = weatherTypeJDBCRepo.findByName(cr.getWeather().getType(), conn)
                    .orElseGet(() -> {
                        try {
                            weatherTypeJDBCRepo.save(cr.getWeather().getType(), conn);
                            return weatherTypeJDBCRepo.findByName(cr.getWeather().getType(), conn).get();
                        } catch (SQLException ex) {
                            throw new RuntimeSQLException();
                        }
                    });
            WeatherJ weather = weatherJDBCRepo.findByTemperatureAndType(cr.getWeather().getTemperature(), type.getId(), conn)
                    .orElseGet(() -> {
                        try {
                            weatherJDBCRepo.save(new WeatherJ(cr.getWeather().getTemperature(), type.getId()), conn);
                            return weatherJDBCRepo.findByTemperatureAndType(cr.getWeather().getTemperature(), type.getId(), conn).get();
                        } catch (SQLException ex) {
                            throw new RuntimeSQLException(ex.getMessage());
                        }
                    });

            cityJDBCRepo.save(
                    new CityJ(
                            cr.getName(),
                            cr.getDate(),
                            weather.getId()
                    ),
                    conn
            );
            conn.commit();
            conn.close();
        } catch (SQLException | RuntimeSQLException ex) {
            conn.rollback();
            throw new RuntimeException();
        }
    }

    public void deleteCity(CityRequest cr) throws SQLException {
        cityJDBCRepo.deleteByNameAndDate(cr);
    }

    public void deleteCityById(int id) throws SQLException {
        cityJDBCRepo.deleteById(id);
    }
}