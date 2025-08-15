package com.hotel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Menu {
    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private final CustomerDao customerDao = new CustomerDao();
    private final RoomDao roomDao = new RoomDao();
    private final BookingService bookingService = new BookingService();

    public void loop() throws Exception {
        while (true) {
            System.out.println("\n=== HOTEL MENU ===");
            System.out.println("1) List Rooms");
            System.out.println("2) Add Room");
            System.out.println("3) List Customers");
            System.out.println("4) Add Customer");
            System.out.println("5) Create Booking");
            System.out.println("6) Exit");
            System.out.print("Choose: ");
            String choice = in.readLine();
            switch (choice) {
                case "1" -> listRooms();
                case "2" -> addRoom();
                case "3" -> listCustomers();
                case "4" -> addCustomer();
                case "5" -> createBooking();
                case "6" -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void listRooms() throws Exception {
        List<Room> rooms = roomDao.listAll();
        System.out.println("ID  | Number | Type   | BaseRate | Status");
        for (Room r : rooms) {
            System.out.printf("%3d | %6s | %-6s | %8s | %s%n", r.id, r.number, r.type, r.baseRate, r.status);
        }
    }
    private void addRoom() throws Exception {
        System.out.print("Room number: "); String number = in.readLine();
        System.out.print("Type (SINGLE/DOUBLE/DELUXE/SUITE): "); RoomType type = RoomType.valueOf(in.readLine().trim().toUpperCase());
        System.out.print("Base rate: "); BigDecimal rate = new BigDecimal(in.readLine().trim());
        Room r = new Room(); r.number = number; r.type = type; r.baseRate = rate; r.status = RoomStatus.AVAILABLE;
        long id = roomDao.create(r);
        System.out.println("Room created with id=" + id);
    }
    private void listCustomers() throws Exception {
        var cs = customerDao.listAll();
        System.out.println("ID  | Name          | Phone | Email");
        for (var c : cs) System.out.printf("%3d | %-13s | %-12s | %s%n", c.id, c.name, c.phone, c.email);
    }
    private void addCustomer() throws Exception {
        System.out.print("Name: "); String name = in.readLine();
        System.out.print("Phone: "); String phone = in.readLine();
        System.out.print("Email: "); String email = in.readLine();
        System.out.print("Gov ID: "); String gid = in.readLine();
        Customer c = new Customer(); c.name = name; c.phone = phone; c.email = email; c.govId = gid;
        long id = customerDao.create(c);
        System.out.println("Customer created with id=" + id);
    }
    private void createBooking() throws Exception {
        System.out.print("Customer ID: "); long custId = Long.parseLong(in.readLine());
        System.out.print("Check-in (YYYY-MM-DD): "); LocalDate inDate = LocalDate.parse(in.readLine().trim());
        System.out.print("Check-out (YYYY-MM-DD): "); LocalDate outDate = LocalDate.parse(in.readLine().trim());
        System.out.print("Room type (SINGLE/DOUBLE/DELUXE/SUITE): "); RoomType type = RoomType.valueOf(in.readLine().trim().toUpperCase());
        var avail = roomDao.findAvailable(type, inDate, outDate);
        if (avail.isEmpty()) { System.out.println("No rooms available for selected dates."); return; }
        System.out.println("Available rooms:"); for (var r : avail) System.out.println(" - " + r.number + " (rate " + r.baseRate + ")");
        System.out.print("Choose room number: "); String roomNum = in.readLine().trim();
        try {
            Booking b = bookingService.createBooking(custId, roomNum, inDate, outDate);
            System.out.println("Booking created. ID=" + b.id + ", Total=" + b.totalRoomAmount);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
