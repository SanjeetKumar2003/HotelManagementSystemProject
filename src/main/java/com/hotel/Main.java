package com.hotel;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    private static final String SCHEMA = """
PRAGMA foreign_keys = ON;
CREATE TABLE IF NOT EXISTS customers (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  phone TEXT,
  email TEXT,
  gov_id TEXT,
  created_at TEXT DEFAULT (datetime('now'))
);
CREATE TABLE IF NOT EXISTS rooms (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  number TEXT NOT NULL UNIQUE,
  type TEXT NOT NULL,
  base_rate NUMERIC NOT NULL,
  status TEXT NOT NULL DEFAULT 'AVAILABLE'
);
CREATE TABLE IF NOT EXISTS bookings (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  customer_id INTEGER NOT NULL REFERENCES customers(id),
  room_id INTEGER NOT NULL REFERENCES rooms(id),
  check_in TEXT NOT NULL,
  check_out TEXT NOT NULL,
  status TEXT NOT NULL,
  total_room_amount NUMERIC DEFAULT 0
);
CREATE TABLE IF NOT EXISTS food_items (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  price NUMERIC NOT NULL,
  active INTEGER NOT NULL DEFAULT 1
);
CREATE TABLE IF NOT EXISTS orders (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  booking_id INTEGER NOT NULL REFERENCES bookings(id),
  ordered_at TEXT DEFAULT (datetime('now'))
);
CREATE TABLE IF NOT EXISTS order_lines (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  order_id INTEGER NOT NULL REFERENCES orders(id),
  food_item_id INTEGER NOT NULL REFERENCES food_items(id),
  qty INTEGER NOT NULL,
  line_total NUMERIC NOT NULL
);
CREATE TABLE IF NOT EXISTS services (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  price NUMERIC NOT NULL,
  active INTEGER NOT NULL DEFAULT 1
);
CREATE TABLE IF NOT EXISTS service_usages (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  booking_id INTEGER NOT NULL REFERENCES bookings(id),
  service_id INTEGER NOT NULL REFERENCES services(id),
  used_at TEXT DEFAULT (datetime('now')),
  qty INTEGER NOT NULL,
  line_total NUMERIC NOT NULL
);
CREATE TABLE IF NOT EXISTS payments (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  booking_id INTEGER NOT NULL REFERENCES bookings(id),
  paid_at TEXT DEFAULT (datetime('now')),
  amount NUMERIC NOT NULL,
  method TEXT,
  reference TEXT
);
CREATE TABLE IF NOT EXISTS audit (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  occurred_at TEXT DEFAULT (datetime('now')),
  actor TEXT,
  action TEXT,
  details TEXT
);
""";

    public static void main(String[] args) throws Exception {
        // Init schema on first run
        Db.initSchema(SCHEMA);
        System.out.println("Hotel DB initialized in hotel.db");
        new Menu().loop();
        System.out.println("Goodbye!");
    }
}
