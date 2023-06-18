package com.example.demojavafx;

public class Question {
    private int id;
    private int categoryId;
    private String text;
    private String name;
    private String media;
    private float mark;

    public Question(int id, int categoryId, String text, String name, String media, float mark) {
        this.id = id;
        this.categoryId = categoryId;
        this.text = text;
        this.name = name;
        this.media = media;
        this.mark = mark;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public String getMedia() {
        return media;
    }

    public float getMark() {
        return mark;
    }
}