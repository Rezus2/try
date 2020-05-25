package com.example.myapplication;

import java.io.Serializable;

public class Matches implements Serializable {
    private long id;
    private String name;
    private String surname;
    private String middlename;
    private String group;

    public Matches (long id, String nm, String sn, String mn,String gr) {
        this.id = id;
        this.name = nm;
        this.surname = sn;
        this.middlename = mn;
        this.group =gr;
    }



    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getGroup() {
        return group;
    }
}