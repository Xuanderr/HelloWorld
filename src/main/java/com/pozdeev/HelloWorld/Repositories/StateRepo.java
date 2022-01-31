package com.pozdeev.HelloWorld.Repositories;

import com.pozdeev.HelloWorld.models.State;
//import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepo {

    List<State> readAll();

    State read(int id);

    int save(State state);

    int update(int id, State state);

    int delete(int id);


}