package com.pozdeev.HelloWorld.dao;

import com.pozdeev.HelloWorld.models.Person;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO implements PersonDAOInterface {

    private static int PEOPLE_COUNT;
    private List<Person> people;

    {
        people = new ArrayList<>();

        people.add(new Person(++PEOPLE_COUNT, "Tom"));
        people.add(new Person(++PEOPLE_COUNT, "Bob"));
        people.add(new Person(++PEOPLE_COUNT, "Mike"));
        people.add(new Person(++PEOPLE_COUNT, "Katy"));
    }

    @Override
    public List<Person> index() {
        return people;
    }

    @Override
    public Person show(int id) {
        return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }

    @Override
    public void save(Person person) {
        person.setId(++PEOPLE_COUNT);
        people.add(person);
    }

    @Override
    public void update(int id, Person person) {
        Person tobeUpdated = show(id);
        tobeUpdated.setName(person.getName());
    }

    @Override
    public void delete(int id) {
        people.removeIf(p -> p.getId() == id);
    }
}
