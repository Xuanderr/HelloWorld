package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.State;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component    // <-- COMMENT THIS
public class StateServiceDatabaseImpl implements StateService{

    private static String DB_DRIVER = "org.postgresql.Driver";
    private static String DB_URL = "jdbc:postgresql://localhost:5432/HelloWorldDataBase";
    private static String DB_USER = "postgres";
    private static String DB_PASSWORD = "123";


    private Connection getDBConnection() throws SQLException {
        // Maybe need in future
        //try {
        //    Class.forName(DB_DRIVER);
        //} catch (ClassNotFoundException e) {
        //    System.out.println(e.getMessage());
        //}
        Connection dbConnection = DriverManager.getConnection(DB_URL, DB_USER,DB_PASSWORD);
        return dbConnection;
    }

    @Override
    public List<State> readAll() {

        try( Connection dbConnection = getDBConnection();
             Statement statement = dbConnection.createStatement())
        {
            String readAllSQL = "SELECT * from States";
            ResultSet resultSet = statement.executeQuery(readAllSQL);

            // question about vision area?
            List<State> states = new ArrayList<>();

            while (resultSet.next()) {
                states.add(new State(resultSet.getInt("state_id"),
                        resultSet.getString("title"),
                        resultSet.getString("anonse"),
                        resultSet.getString("full")));
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
            String readOneSQL = String.format("SELECT * FROM States WHERE state_id = %d", id);
            ResultSet resultSet = statement.executeQuery(readOneSQL);

            // question about vision area?
            State state = new State();

            while (resultSet.next()) {
                state.setId(resultSet.getInt("state_id"));
                state.setTitle(resultSet.getString("title"));
                state.setAnonse(resultSet.getString("anonse"));
                state.setFull(resultSet.getString("full"));
            }
            return state;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void save(State state) {

        try (Connection dbConnection = getDBConnection();
             Statement statement = dbConnection.createStatement()) {
            String readOneSQL = String.format("INSERT INTO States (title, anonse, full) VALUES ('%s', %s, '%s')",
                    state.getTitle(), state.getAnonse(), state.getFull());

            statement.execute(readOneSQL);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    @Override
    public boolean update(int id, State state) {

        try (Connection dbConnection = getDBConnection();
             Statement statement = dbConnection.createStatement()) {
            String readOneSQL =
                    String.format("UPDATE States SET title = '%s', anonse = '%s', full = '%s'  " +
                                    "WHERE state_id = %d",
                    state.getTitle(), state.getAnonse(), state.getFull(), id);

            statement.execute(readOneSQL);

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {

        try (Connection dbConnection = getDBConnection();
             Statement statement = dbConnection.createStatement()) {
            String readOneSQL =
                    String.format("DELETE FROM States WHERE state_id = %d", id);

            statement.execute(readOneSQL);

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
