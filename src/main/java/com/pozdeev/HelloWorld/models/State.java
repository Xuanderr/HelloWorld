package com.pozdeev.HelloWorld.models;



//import javax.persistence.Id;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;

public class State {

    private Integer id;

    private String title;

    private String anonse;

    private String full;

    public State() {  }


    public State(Integer id, String title, String anonse, String full) {
        this.id = id;
        this.title = title;
        this.anonse = anonse;
        this.full = full;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnonse() {
        return anonse;
    }

    public void setAnonse(String anonse) {
        this.anonse = anonse;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }
}