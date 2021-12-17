package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.State;
//import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateService {

    List<State> readAll();

    State read(int id);

    void save(State state);

    boolean update(int id, State state);

    boolean delete(int id);


}