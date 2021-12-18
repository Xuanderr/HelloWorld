package com.pozdeev.HelloWorld.models;



//import javax.persistence.Id;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;

public class State {

    private Integer state_id;

    private String title;

    private String anons;

    private String full_text;

    public State() {  }


    public State(Integer id, String title, String anons, String full) {
        this.state_id = id;
        this.title = title;
        this.anons = anons;
        this.full_text = full;
    }

    public Integer getState_id() {
        return state_id;
    }

    public void setState_id(Integer state_id) {
        this.state_id = state_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnons() {
        return anons;
    }

    public void setAnons(String anons) {
        this.anons = anons;
    }

    public String getFull_text() {
        return full_text;
    }

    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }
}