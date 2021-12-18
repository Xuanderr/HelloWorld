package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.State;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("list")
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

    private String getName() {
        return "list";
    }

    @Override
    public List<State> readAll() {
        return states;
    }

    @Override
    public State read(int id) {
        for (State state: states) {
            if (state.getState_id() == id) {
                return state;
            }
        }
        return null;
    }

    @Override
    public int save(State state) {
        state.setState_id(STATE_COUNT++);
        states.add(state);
        return 1;
    }

    @Override
    public int update(int id, State state) {
        State forUp = read(id);
        if(forUp != null) {
            forUp.setTitle(state.getTitle());
            forUp.setAnons(state.getAnons());
            forUp.setFull_text(state.getFull_text());
            return 1;
        }
       return 0;
    }

    @Override
    public int delete(int id) {
        if (states.removeIf(state -> state.getState_id() == id)) {
            return 1;
        }
        return 0;
    }
}
