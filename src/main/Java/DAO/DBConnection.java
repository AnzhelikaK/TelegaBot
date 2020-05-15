package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;
    private DBConnection() { }

    public static Connection getConnection() {
        String userName = "root";
        String password = "123";
        String connectionUrl="jdbc:mysql://localhost:3306/fortelegabot?useUnicode=true&serverTimezone=Europe/Moscow";
        try {
            connection= DriverManager.getConnection(connectionUrl, userName, password );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
