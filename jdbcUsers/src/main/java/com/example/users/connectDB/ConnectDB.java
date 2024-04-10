package com.example.users.connectDB;

import com.example.users.model.User;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

@Component
public class ConnectDB {
    private final static String urlJDBC = "jdbc:postgresql://192.168.215.27:5432/postgres";
    private final static String userJDBC = "postgres";
    private final static String passwordJDBC = "postgres";

    public User selectUserByLogin(String login) throws CustomException  {
        User user = null;
        Connection connection = null;
        Statement statement = null;  //интерфейс. предоставляет методы для отправки SQL-запросов и получения результатов (для выполнения статических SQL-запросов без параметров)
        ResultSet resultSet = null; //Класс ResultSet представляет результирующий набор данных по строчно

        try {
            connection = DriverManager.getConnection(urlJDBC, userJDBC, passwordJDBC);
            statement = connection.createStatement();
            String query = "SELECT * FROM auth JOIN email on email.login=auth.login WHERE auth.login = '" + login + "'";
            resultSet = statement.executeQuery(query); //executeQuery - получаем данные из таблицы

            if (resultSet.next()) {
                user = new User(login, resultSet.getString("password"), resultSet.getDate("date"),
                        resultSet.getString("email"));
            }
            else {
                throw new CustomException("User with login '" +login+ "' not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {resultSet.close();}
                if (statement != null) {statement.close();}
                if (connection != null) {connection.close();}
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public int insertUser(User user) {
        int rowsAffected = 0;

        String query = "INSERT INTO auth (login, password, date) VALUES (?, ?, ?);\n"+
                "INSERT INTO email (email,login) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(urlJDBC, userJDBC, passwordJDBC);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setDate(3, (Date) user.getDate());

            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getLogin());

            rowsAffected = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;

    }
}


