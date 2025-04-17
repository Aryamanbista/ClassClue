package com.example.classclue.models;

import java.io.Serializable;

public class Event implements Serializable {
    private String id;
    private String subjectId;
    private String name;
    private String type; // Exam, Test, Assignment, etc.
    private String date; // YYYY-MM-DD
    private String time; // HH:MM
    private String location;
    private static final long serialVersionUID = 1L;

    public Event() {
        // Required empty constructor for Firebase
    }

    public Event(String id, String name, String type, String date, String time, String location) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}