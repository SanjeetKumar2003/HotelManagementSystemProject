# Hotel Management – Upgraded (SQLite + JDBC)

## How to run
1) Ensure you have Java 17+ and Maven installed.
2) In this folder, run:
```
mvn -q -DskipTests package
java -jar target/hotel-management-1.0.0-jar-with-dependencies.jar
```
The app will create `hotel.db` (SQLite) in the working directory and show a CLI menu.

## Features included
- Customers, Rooms, Bookings (date-ranged, overlap checks)
- Dynamic pricing (weekend + occupancy multipliers)
- List/Add Rooms and Customers
- Create Booking with availability check and auto-quote
- Clean JDBC DAO structure; schema auto-initialized

## Extend next
- Payments, food items, orders
- Reports (revenue, occupancy)
- Roles/login, admin CRUD menus
- GUI (JavaFX) or REST (Spring Boot) using same DAO/Service layer
