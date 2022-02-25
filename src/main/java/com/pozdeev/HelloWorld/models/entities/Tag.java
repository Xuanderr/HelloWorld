package com.pozdeev.HelloWorld.models.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Article> articles;

    public Tag() { }

    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tag{" +
                " name='" + name + '\'' +
                '}';
    }
}
