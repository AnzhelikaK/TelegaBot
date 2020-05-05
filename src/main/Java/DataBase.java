import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class DataBase {
    String userName = "root";
    String password = "5632156321";
    String connectionUrl = "jdbc:mysql://localhost:3306/fortelegabot?useUnicode=true&serverTimezone=Europe/Minsk"; //&serverTimezone=UTC
    Connection connection;

    // ???? throws ClassNotFoundException {   // SQLException   в методе где создается БД были эти ошибки
    public void getConnectDB() { // throws CSQLException
        try {
            connection = DriverManager.getConnection(connectionUrl, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeInDB(Model model, Date dateForDB) {
        String sqlPrepared = "insert into weather (name,date,temp,humidity,icon, description ) values (?,?,?,?,?,?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlPrepared);
            preparedStatement.setString(1, model.getNameOfCity());
            preparedStatement.setDate(2, new Date(dateForDB.getTime() + (3 * 3600_000 + 1000)));
            preparedStatement.setDouble(3, model.getTemp());
            preparedStatement.setDouble(4, model.getHumidity());
            preparedStatement.setString(5, model.getIcon());
            preparedStatement.setString(6, model.getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean answerOfDB(Model model, Date dateForDB) { // SQLException
        boolean respond = false;
        String sqlPrepared = "select temp, humidity, icon, description from weather where name=? and date=?;";  //   !!!! AND  !!!!
        // String sqlPrepared="select name, date, temp from weather where name='minsk';";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlPrepared);
            preparedStatement.setString(1, model.getNameOfCity());
            preparedStatement.setDate(2, new Date(dateForDB.getTime() + (3 * 3600_000 + 1000)));
//      preparedStatement.setDate(2, new Date(2020-04-29));
//      preparedStatement.setDate(2,dateForDB);   // ничего не приходит, так как ищет по прошлой дате (дата 2020-04-27/ А на эту дату в базе ничего нет
            ResultSet res = preparedStatement.executeQuery();

            if(res.next()) {

                                System.out.printf(res.getDate("date").toString());    //  МОжно ли достать дату из БД и привести ее к нашему UTC и т.о. отменить приведение БД
                model.setTemp(res.getDouble("temp"));
                model.setHumidity(res.getDouble("humidity"));
                model.setIcon(res.getString("icon"));
                model.setDescription(res.getString("description"));
                respond = true;
            }
            /// ? нужно ли это закрывать?
            if (res != null) res.close();
            if (preparedStatement != null) preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(model.getNameOfCity() + " " + model.getTemp() + " " + model.getHumidity());

        return respond;
    }

    public void closeDB() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
