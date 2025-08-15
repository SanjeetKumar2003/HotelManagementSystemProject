package com.hotel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

 class Customer {
    public long id;
    public String name;
    public String phone;
    public String email;
    public String govId;
}

 class Room {
    public long id;
    public String number;
    public RoomType type;
    public BigDecimal baseRate;
    public RoomStatus status;
}

class Booking {
    public long id;
    public long customerId;
    public long roomId;
    public LocalDate checkIn;
    public LocalDate checkOut;
    public String status; // BOOKED, CHECKED_IN, CHECKED_OUT, CANCELED
    public BigDecimal totalRoomAmount;
}

class FoodItem {
    public long id;
    public String name;
    public BigDecimal price;
    public boolean active;
}

class Order {
    public long id;
    public long bookingId;
    public LocalDateTime orderedAt;
}

 class OrderLine {
    public long id;
    public long orderId;
    public long foodItemId;
    public int qty;
    public java.math.BigDecimal lineTotal;
}
 class Payment {
    public long id;
    public long bookingId;
    public LocalDateTime paidAt;
    public java.math.BigDecimal amount;
    public String method;
    public String reference;
}
