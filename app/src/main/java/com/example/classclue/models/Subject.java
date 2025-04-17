package com.example.classclue.models;

import java.io.Serializable;

public class Subject implements Serializable {
    private String id;
    private String name;
    private String code;
    private static final long serialVersionUID = 1L;

    public Subject() {
        // Required empty constructor for Firebase
    }

    public Subject(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}