import java.sql.*;
import java.util.Optional;

public class DataBase {
    String userName = "root";
    String password = "5632156321";
    String connectionUrl = "jdbc:mysql://localhost:3306/fortelegabot?useUnicode=true&serverTimezone=UTC";
    Connection connection;


   
public void getConnectDB(){ // throws CSQLException
    try {
        connection = DriverManager.getConnection(connectionUrl, userName, password);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public void writeInDB(Model model, Date dateForBD){
    System.out.println(dateForBD);
    String sqlPrepared= "insert into weather (name,date,temp,humidity,icon, description ) values (?,?,?,?,?,?);";
    System.out.println(sqlPrepared);
           try {
           PreparedStatement preparedStatement = connection.prepareStatement(sqlPrepared);
                                 preparedStatement.setString(1,model.getNameOfCity());
                                 preparedStatement.setDate(2,dateForBD);
                                 preparedStatement.setDouble(3,model.getTemp());
                                 preparedStatement.setDouble(4,model.getHumidity());
                                 preparedStatement.setString(5,model.getIcon());
                                 preparedStatement.setString(6,model.getDescription());
               preparedStatement.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public ResultSet answerOfDB(String request){ // SQLException
    ResultSet respond=null;
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        respond=preparedStatement.executeQuery();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return respond;
}

public void closeDB(){
    try {
        connection.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
