package DAO;

import java.sql.*;
import java.util.function.Consumer;

public class WeatherDAO {

    private Connection connection = DBConnection.getConnection();

    // write into DB
    public void add(WeatherModel model) {
        String sql = "insert into weather(name, date, temp, humidity, icon, description) values (?,?,?,?,?,?);";

        Consumer<PreparedStatement> consumer = preparedStatement -> {

            try {
                preparedStatement.setString(1, model.getName());
                preparedStatement.setDate(2, model.getDate());
                preparedStatement.setDouble(3, model.getTemp());
                preparedStatement.setDouble(4, model.getHumidity());
                preparedStatement.setString(5, model.getIcon());
                preparedStatement.setString(6, model.getDescription());

            } catch (SQLException e) {
            }
        };

        try (PreparedStatement preparedStatement = createPreparedStatement(consumer, sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get from BD
    public WeatherModel getCityByDate(Date date, String city) {
        WeatherModel model = new WeatherModel();

        String sql = "select * from weather where name=? and date=?;";
        Consumer<PreparedStatement> consumer = p -> {
            try {
                p.setString(1, city);
                p.setDate(2, date);
            } catch (SQLException e) {
            }
        };

        try (PreparedStatement preparedStatement = createPreparedStatement(consumer, sql);   //   !!!!!! nice code
             ResultSet result = preparedStatement.executeQuery();
        ) {

            if (result.next()) {
                model.setId(result.getInt("id"));
                model.setName(result.getString("name"));
                model.setDate(result.getDate("date"));
                model.setTemp(result.getDouble("temp"));
                model.setHumidity(result.getDouble("humidity"));
                model.setIcon(result.getString("icon"));
                model.setDescription(result.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    private PreparedStatement createPreparedStatement(Consumer<PreparedStatement> consumer, String sql) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        consumer.accept(preparedStatement);
        return preparedStatement;
    }

}


