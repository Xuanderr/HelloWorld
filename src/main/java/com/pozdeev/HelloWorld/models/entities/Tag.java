package com.pozdeev.HelloWorld.models.entities;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Article> articles;

    public Tag() { }

    public Tag(Long id, String name) {
        this.Id = id;
        this.name = name;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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
                "Id=" + Id +
                ", name='" + name + '\'' +
                '}';
    }
}
