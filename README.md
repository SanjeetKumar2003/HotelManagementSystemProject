# 🏨 Hotel Management System

A **Java-based Hotel Management System** built using **Object-Oriented Programming (OOP)** principles and **SQLite** for data storage.  
This project is a console-based application designed to manage hotel bookings, rooms, customers, and billing operations.

---

## ✨ Features
- 🛎 **Room Management**
  - Add, view, and manage room details.
  - Track room availability (Single, Double, Suite, etc.).
- 📅 **Booking System**
  - Book rooms for customers with check-in and check-out dates.
  - Automatically update room availability.
- 👤 **Customer Management**
  - Add and manage customer information.
  - Store contact details and government ID.
- 🍽 **Food & Services (Optional)**
  - Record food orders for guests.
  - Maintain an order history.
- 💰 **Billing**
  - Generate final bills with room charges and service charges.
- 📊 **Reports**
  - View occupancy rates, revenue, and booking history.
  
---

## 🛠 Technologies Used
- **Java 17**
- **SQLite** (via JDBC)
- **Maven** (for dependency management & build)
- **OOP Principles** (Encapsulation, Inheritance, Polymorphism)

---

## 📂 Project Structure
HotelManagementSystemProject/
├── src/main/java/com/hotel/
│ ├── Main.java
│ ├── Db.java
│ ├── dao/ (Data Access Objects)
│ ├── models/ (Entity Classes)
│ ├── services/ (Business Logic)
│ └── menu/ (CLI Menu Handling)
├── pom.xml
└── README.md