package com.pozdeev.HelloWorld.dao;
import com.pozdeev.HelloWorld.models.Person;
import java.util.List;

public interface PersonDAOInterface {
    List<Person> index();
    Person show(int id);
    void save(Person person);
    void update(int id, Person person);
    void delete(int id);
}
