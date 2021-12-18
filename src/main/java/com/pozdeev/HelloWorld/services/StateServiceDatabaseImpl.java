package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.State;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component("database")
public class StateServiceDatabaseImpl implements StateService{

    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/HelloWorldDataBase";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "123";


    private String getName() {
        return "database";
    }

    private Connection getDBConnection() throws SQLException {
        // Maybe need in future
        //try {
        //    Class.forName(DB_DRIVER);
        //} catch (ClassNotFoundException e) {
        //    System.out.println(e.getMessage());
        //}
        return DriverManager.getConnection(DB_URL, DB_USER,DB_PASSWORD);
    }

    @Override
    public List<State> readAll() {

        try( Connection dbConnection = getDBConnection();
             Statement statement = dbConnection.createStatement())
        {
            String readAllSQL = "SELECT * FROM states ORDER BY state_id ASC ";
            ResultSet resultSet = statement.executeQuery(readAllSQL);

            // question about vision area?
            List<State> states = new ArrayList<>();

            while (resultSet.next()) {
                states.add(new State(resultSet.getInt("state_id"),
                        resultSet.getString("title"),
                        resultSet.getString("anons"),
                        resultSet.getString("full_text")));
            }
            return states;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public State read(int id) {

        try( Connection dbConnection = getDBConnection();
             Statement statement = dbConnection.createStatement())
        {
            String readOneSQL = String.format("SELECT * FROM states WHERE state_id = %d", id);
            ResultSet resultSet = statement.executeQuery(readOneSQL);

            // question about vision area?
            State state = new State();

            while (resultSet.next()) {
                state.setState_id(resultSet.getInt("state_id"));
                state.setTitle(resultSet.getString("title"));
                state.setAnons(resultSet.getString("anons"));
                state.setFull_text(resultSet.getString("full_text"));
            }
            return state;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public int save(State state) {

        try (Connection dbConnection = getDBConnection();
             Statement statement = dbConnection.createStatement()) {
            String readOneSQL = String.format("INSERT INTO states (title, anons, full_text) VALUES ('%s', '%s', '%s')",
                    state.getTitle(), state.getAnons(), state.getFull_text());

            return statement.executeUpdate(readOneSQL);

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public int update(int id, State state) {

        try (Connection dbConnection = getDBConnection();
             Statement statement = dbConnection.createStatement()) {
            String readOneSQL =
                    String.format("UPDATE states SET title = '%s', anons = '%s', full_text = '%s'  " +
                                    "WHERE state_id = %d",
                    state.getTitle(), state.getAnons(), state.getFull_text(), id);

            return statement.executeUpdate(readOneSQL);

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public int delete(int id) {

        try (Connection dbConnection = getDBConnection();
             Statement statement = dbConnection.createStatement()) {
            String readOneSQL =
                    String.format("DELETE FROM states WHERE state_id = %d", id);

            return statement.executeUpdate(readOneSQL);

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

}
