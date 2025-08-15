package com.hotel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class PricingService {
    public BigDecimal quote(Room room, LocalDate in, LocalDate out, double occupancyPct) {
        BigDecimal total = BigDecimal.ZERO;
        LocalDate d = in;
        while (d.isBefore(out)) {
            BigDecimal daily = room.baseRate;
            DayOfWeek dow = d.getDayOfWeek();
            if (dow == DayOfWeek.FRIDAY || dow == DayOfWeek.SATURDAY) daily = daily.multiply(new BigDecimal("1.15"));
            else if (dow == DayOfWeek.SUNDAY) daily = daily.multiply(new BigDecimal("1.05"));
            if (occupancyPct >= 0.80) daily = daily.multiply(new BigDecimal("1.10"));
            total = total.add(daily);
            d = d.plusDays(1);
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}

 class BookingService {
    private final RoomDao roomDao = new RoomDao();
    private final BookingDao bookingDao = new BookingDao();
    private final PricingService pricing = new PricingService();

    public Booking createBooking(long customerId, String roomNumber, LocalDate in, LocalDate out) throws Exception {
        Room room = roomDao.findByNumber(roomNumber).orElseThrow(() -> new IllegalArgumentException("Room not found"));
        if (bookingDao.overlaps(room.id, in, out)) throw new IllegalStateException("Room occupied in selected dates");
        double occupancy = occupancyPercent(in, out);
        var amount = pricing.quote(room, in, out, occupancy);
        Booking b = new Booking();
        b.customerId = customerId; b.roomId = room.id; b.checkIn = in; b.checkOut = out; b.status = "BOOKED"; b.totalRoomAmount = amount;
        return bookingDao.create(b);
    }

    public double occupancyPercent(LocalDate from, LocalDate to) throws Exception {
        List<Room> rooms = roomDao.listAll();
        if (rooms.isEmpty()) return 0;
        // naive: count any active booking during range as occupied
        int total = rooms.size();
        int occupied = 0;
        for (Room r : rooms) {
            if (new BookingDao().overlaps(r.id, from, to)) occupied++;
        }
        return (double) occupied / total;
    }
}
