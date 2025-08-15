package com.hotel;

import java.sql.*;

public final class Db {
    private static final String URL = "jdbc:sqlite:hotel.db";
    static {
        try { Class.forName("org.sqlite.JDBC"); } catch (Exception e) { throw new RuntimeException(e); }
    }
    public static Connection get() throws SQLException { return DriverManager.getConnection(URL); }

    public static void initSchema(String ddl) {
        try (var c = get(); var st = c.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON;");
            for (String part : ddl.split(";")) {
                var p = part.trim();
                if (!p.isEmpty()) st.execute(p + ";");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to init schema", e);
        }
    }
}
