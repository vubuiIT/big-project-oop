package com.example.demojavafx;

public class Question {
    private int id;
    private int categoryId;
    private String text;
    private String name;
    private String mediaName;
    private byte[] media;
    private float mark;

    public Question(int id, int categoryId, String text, String name, byte[] media, float mark, String mediaName) {
        this.id = id;
        this.categoryId = categoryId;
        this.text = text;
        this.name = name;
        this.media = media;
        this.mark = mark;
        this.mediaName = mediaName;
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

    public byte[] getMedia() {
        return media;
    }
    public String getMediaName() {
        return mediaName;
    }

    public float getMark() {
        return mark;
    }
}