package com.smk.dao;

import com.smk.model.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

public class LocationDao implements Dao<Location, Integer> {

    private final Optional<Connection> connection;
    public LocationDao() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public Optional<Location> get(int id) {
        return Optional.empty();
    }

    @Override
    public Collection<Location> getAll() {
        Collection<Location> result = new LinkedList<>();
        String sql = "SELECT * from location";
        connection.ifPresent(connection ->{
            try {
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");

                    Location location = new Location();
                    location.setId(id);
                    location.setName(name);
                    result.add(location);

                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        } );
        return result;
    }

    @Override
    public Optional<Integer> save(Location location) {
        return Optional.empty();
    }

    @Override
    public void update(Location location) {

    }

    @Override
    public void delete(Location location) {

    }

    @Override
    public Collection<Location> search(String keyword) {
        return null;
    }
}