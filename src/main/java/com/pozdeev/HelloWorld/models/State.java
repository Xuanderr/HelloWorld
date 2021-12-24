package com.pozdeev.HelloWorld.models;
//import javax.persistence.Id;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;


public class State {

    private Integer stateId;

    private String title;

    private String anons;

    private String fullText;

    public State() {  }


    public State(Integer id, String title, String anons, String full) {
        this.stateId = id;
        this.title = title;
        this.anons = anons;
        this.fullText = full;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
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

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }
}