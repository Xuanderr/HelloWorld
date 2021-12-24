package com.pozdeev.HelloWorld.Repositories;

import com.pozdeev.HelloWorld.models.State;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component("database")
public class StateRepoDatabaseImpl implements StateRepo{

    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/HelloWorldDataBase";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "123";

    private static final String SELECT_ALL = "SELECT * FROM states ORDER BY state_id ASC";
    private static final String SELECT = "SELECT * FROM states WHERE state_id = ?";
    private static final String INSERT = "INSERT INTO states (title, anons, full_text) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE states SET title = ?, anons = ?, full_text = ? WHERE state_id = ?";
    private static final String DELETE = "DELETE FROM states WHERE state_d = ?";


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
             PreparedStatement preparedStatement = dbConnection.prepareStatement(SELECT_ALL))
        {
            ResultSet resultSet = preparedStatement.executeQuery();

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
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public State read(int id) {

        try( Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(SELECT))
        {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            // question about vision area?
            State state = new State();

            while (resultSet.next()) {
                state.setStateId(resultSet.getInt("state_id"));
                state.setTitle(resultSet.getString("title"));
                state.setAnons(resultSet.getString("anons"));
                state.setFullText(resultSet.getString("full_text"));
            }
            return state;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    // hibernate
    @Override
    public int save(State state) {

        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(INSERT))
        {
            preparedStatement.setString(1, state.getTitle());
            preparedStatement.setString(2, state.getAnons());
            preparedStatement.setString(3, state.getFullText());

            return preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public int update(int id, State state) {

        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UPDATE))
        {
            preparedStatement.setString(1, state.getTitle());
            preparedStatement.setString(2, state.getAnons());
            preparedStatement.setString(3, state.getFullText());
            preparedStatement.setInt(4, id);

            return preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public int delete(int id) {

        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(DELETE))
        {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

}
