package com.example.carcrashproject_v20_10112024.Data.db.models;


public class QuestionConfiguration {
    private int id;
    private String description;
    private int answerType;

    // Constructors
    public QuestionConfiguration() { }

    public QuestionConfiguration(int id, String description, int answerType) {
        this.id = id;
        this.description = description;
        this.answerType = answerType;
    }
    public QuestionConfiguration(String description, int answerType) {
        this.description = description;
        this.answerType = answerType;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAnswerType() {
        return answerType;
    }

    public void setAnswerType(int answerType) {
        this.answerType = answerType;
    }
}

