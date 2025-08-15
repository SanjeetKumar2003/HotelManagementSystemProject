package com.hotel;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.math.BigDecimal;

class CustomerDao {
    public long create(Customer c) throws SQLException {
        String sql = "INSERT INTO customers(name,phone,email,gov_id) VALUES(?,?,?,?)";
        try (var conn = Db.get(); var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.name); ps.setString(2, c.phone); ps.setString(3, c.email); ps.setString(4, c.govId);
            ps.executeUpdate();
            try (var rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getLong(1); }
        }
        throw new SQLException("No ID returned");
    }
    public Optional<Customer> findById(long id) throws SQLException {
        String sql = "SELECT id,name,phone,email,gov_id FROM customers WHERE id=?";
        try (var conn = Db.get(); var ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    Customer c = new Customer();
                    c.id = rs.getLong(1); c.name = rs.getString(2); c.phone = rs.getString(3); c.email = rs.getString(4); c.govId = rs.getString(5);
                    return Optional.of(c);
                }
            }
        }
        return Optional.empty();
    }
    public List<Customer> listAll() throws SQLException {
        String sql = "SELECT id,name,phone,email,gov_id FROM customers ORDER BY id DESC";
        List<Customer> out = new ArrayList<>();
        try (var conn = Db.get(); var ps = conn.prepareStatement(sql); var rs = ps.executeQuery()) {
            while (rs.next()) {
                Customer c = new Customer();
                c.id = rs.getLong(1); c.name = rs.getString(2); c.phone = rs.getString(3); c.email = rs.getString(4); c.govId = rs.getString(5);
                out.add(c);
            }
        }
        return out;
    }
}

 class RoomDao {
    public long create(Room r) throws SQLException {
        String sql = "INSERT INTO rooms(number,type,base_rate,status) VALUES(?,?,?,?)";
        try (var conn = Db.get(); var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.number); ps.setString(2, r.type.name()); ps.setBigDecimal(3, r.baseRate); ps.setString(4, r.status.name());
            ps.executeUpdate();
            try (var rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getLong(1); }
        }
        throw new SQLException("No ID");
    }
    public Optional<Room> findByNumber(String number) throws SQLException {
        String sql = "SELECT id,number,type,base_rate,status FROM rooms WHERE number=?";
        try (var conn = Db.get(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, number);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));

            }
        }
        return Optional.empty();
    }
    public List<Room> findAvailable(RoomType type, LocalDate from, LocalDate to) throws SQLException {
        String sql = "SELECT r.id,r.number,r.type,r.base_rate,r.status FROM rooms r WHERE r.type=? AND r.status='AVAILABLE' AND NOT EXISTS (" +
                "SELECT 1 FROM bookings b WHERE b.room_id=r.id AND b.status IN ('BOOKED','CHECKED_IN') AND NOT (b.check_out<=? OR b.check_in>=?)" +
                ") ORDER BY r.number";
        List<Room> out = new ArrayList<>();
        try (var conn = Db.get(); var ps = conn.prepareStatement(sql)) {
            ps.setString(1, type.name()); ps.setString(2, from.toString()); ps.setString(3, to.toString());
            try (var rs = ps.executeQuery()) { while (rs.next()) out.add(map(rs)); }
        }
        return out;
    }
    private Room map(ResultSet rs) throws SQLException {
        Room r = new Room();
        r.id = rs.getLong(1); r.number = rs.getString(2); r.type = RoomType.valueOf(rs.getString(3)); r.baseRate = rs.getBigDecimal(4);
        r.status = RoomStatus.valueOf(rs.getString(5));
        return r;
    }
    public List<Room> listAll() throws SQLException {
        List<Room> out = new ArrayList<>();
        try (var c = Db.get(); var ps = c.prepareStatement("SELECT id,number,type,base_rate,status FROM rooms ORDER BY number"); var rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        }
        return out;
    }
}

public class BookingDao {
    public boolean overlaps(long roomId, LocalDate from, LocalDate to) throws SQLException {
        String sql = "SELECT 1 FROM bookings WHERE room_id=? AND status IN ('BOOKED','CHECKED_IN') AND NOT (check_out<=? OR check_in>=?) LIMIT 1";
        try (var c = Db.get(); var ps = c.prepareStatement(sql)) {
            ps.setLong(1, roomId); ps.setString(2, from.toString()); ps.setString(3, to.toString());
            try (var rs = ps.executeQuery()) { return rs.next(); }
        }
    }
    public Booking create(Booking b) throws SQLException {
        String sql = "INSERT INTO bookings(customer_id,room_id,check_in,check_out,status,total_room_amount) VALUES(?,?,?,?,?,?)";
        try (var c = Db.get(); var ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, b.customerId); ps.setLong(2, b.roomId); ps.setString(3, b.checkIn.toString());
            ps.setString(4, b.checkOut.toString()); ps.setString(5, b.status); ps.setBigDecimal(6, b.totalRoomAmount);
            ps.executeUpdate();
            try (var rs = ps.getGeneratedKeys()) { if (rs.next()) b.id = rs.getLong(1); }
        }
        return b;
    }
    public Optional<Booking> findActiveByRoom(long roomId) throws SQLException {
        String sql = "SELECT id,customer_id,room_id,check_in,check_out,status,total_room_amount FROM bookings WHERE room_id=? AND status IN ('BOOKED','CHECKED_IN') ORDER BY id DESC LIMIT 1";
        try (var c = Db.get(); var ps = c.prepareStatement(sql)) {
            ps.setLong(1, roomId);
            try (var rs = ps.executeQuery()) { if (rs.next()) return Optional.of(map(rs)); }
        }
        return Optional.empty();
    }
    private Booking map(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.id = rs.getLong(1); b.customerId = rs.getLong(2); b.roomId = rs.getLong(3);
        b.checkIn = java.time.LocalDate.parse(rs.getString(4)); b.checkOut = java.time.LocalDate.parse(rs.getString(5));
        b.status = rs.getString(6); b.totalRoomAmount = rs.getBigDecimal(7);
        return b;
    }
}
