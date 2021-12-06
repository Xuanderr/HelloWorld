package com.pozdeev.HelloWorld.dao;

import com.pozdeev.HelloWorld.models.Person;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {

    private static int PE0PLE_COUNT;
    private List<Person> people;

    {
        people = new ArrayList<>();

        people.add(new Person(++PE0PLE_COUNT, "Tom"));
        people.add(new Person(++PE0PLE_COUNT, "Lich"));
        people.add(new Person(++PE0PLE_COUNT, "Mike"));
        people.add(new Person(++PE0PLE_COUNT, "Serg"));
    }

    public List<Person> index() {
        return people;
    }

    public Person show(int id) {
        if(id<0 | id>= people.size()) return null;
        return people.get(id);
    }
}
