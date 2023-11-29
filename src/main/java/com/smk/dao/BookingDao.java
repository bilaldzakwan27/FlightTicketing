package com.smk.dao;

import com.smk.model.Booking;

import java.sql.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class BookingDao implements Dao<Booking, Integer> {
    private final Optional<Connection> connection;

    public BookingDao() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public Optional<Booking> get(int id) {
        return null;
    }

    @Override
    public Collection<Booking> getAll() {
        return null;
    }


    @Override
    public void update(Booking booking) {

    }

    @Override
    public void delete(Booking booking) {

    }

    @Override
    public Collection<Booking> search(String keyword) {
        return null;
    }

    @Override
    public Optional<Integer> save(Booking booking) {
        Booking nonNullBooking = Objects.requireNonNull(booking);
        String sql = "INSERT INTO booking (schedule_id, name, price) " +
                "VALUES(?,?,?)";
        return connection.flatMap(conn->{Optional<Integer> generatedId =  Optional.empty();
            try {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1,booking.getScheduleId());
                ps.setString(2,booking.getName());
                ps.setDouble(3,booking.getPrice());
                int numberOfInsertedRows = ps.executeUpdate();
                if (numberOfInsertedRows > 0){
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()){
                        generatedId = Optional.of(rs.getInt(1));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return generatedId;
        });
    }
}