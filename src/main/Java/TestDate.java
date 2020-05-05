import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.sql.Date.*;

// класс создан только для тестирования работы с датой
public class TestDate {
    public void go(){
        String userName = "root";
        String password = "5632156321";
        String connectionUrl = "jdbc:mysql://localhost:3306/fortelegabot?useUnicode=true&serverTimezone=Europe/Minsk"; //&serverTimezone=UTC   serverTimezone=Europe/Minsk
        Connection connection;
        LocalDateTime date1=LocalDateTime.now();
        Timestamp dateTime= Timestamp.valueOf(date1);

                LocalDate date0=LocalDate.now();
        Date date= valueOf(date0);

        System.out.println("LocalDateTime " + date1);
        System.out.println("Timestamp " + dateTime);
        System.out.println("LocalDate " + date0);
        System.out.println("Date " + date);
        try {
            connection = DriverManager.getConnection(connectionUrl, userName, password);
            String sqlPrepared = "insert into fordate (dateTime, date ) values (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlPrepared);
            preparedStatement.setTimestamp(1,dateTime);
            preparedStatement.setDate(2,date);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            connection=DriverManager.getConnection(connectionUrl, userName, password);
            String sql2="select dateTime, date from fordate;";
            PreparedStatement pr=connection.prepareStatement(sql2);
           ResultSet rS= pr.executeQuery();
            while(rS.next()){
                System.out.println(rS.getTimestamp("datetime"));
                System.out.println(rS.getDate("date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }




    }
}
