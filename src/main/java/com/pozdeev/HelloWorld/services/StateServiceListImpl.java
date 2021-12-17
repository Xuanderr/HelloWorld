package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.State;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StateServiceListImpl implements StateService {

    private static int STATE_COUNT;
    private List<State> states;

    {
        states = new ArrayList<>();

        states.add(new State(STATE_COUNT++,"DAO", "Data Access Object",
                "The strategy of data/manage separation in MVC conception"));
        states.add(new State(STATE_COUNT++,"CRUD", "Create, read, update, delete",
                "Minimal set of operations for a Web application"));
        states.add(new State(STATE_COUNT++,"MVC", "Model View Controller",
                "Recommended conception for the Web application structure"));
        states.add(new State(STATE_COUNT++,"Spring", "Java Framework",
                "Very popular tool for Web application development"));
    }
    @Override
    public List<State> readAll() {
        return states;
    }

    @Override
    public State read(int id) {
        for (State state: states) {
            if (state.getId() == id) {
                return state;
            }
        }
        return null;
    }

    @Override
    public void save(State state) {
        state.setId(STATE_COUNT++);
        states.add(state);
    }

    @Override
    public boolean update(int id, State state) {
        State forUp = read(id);
        if(forUp != null) {
            forUp.setTitle(state.getTitle());
            forUp.setAnonse(state.getAnonse());
            forUp.setFull(state.getFull());
            return true;
        }
       return false;
    }

    @Override
    public boolean delete(int id) {
        return states.removeIf(state -> state.getId() == id);
    }
}
