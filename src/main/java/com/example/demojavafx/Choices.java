package com.example.demojavafx;


public class Choices {
    private final int id;
    private final int questionId;
    private final float grade;
    private final String pic;
    private final String text;

    public Choices(int id, int questionId, float grade, String pic, String text) {
        this.id = id;
        this.questionId = questionId;
        this.grade = grade;
        this.pic = pic;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public float getGrade() {
        return grade;
    }

    public String getPic() {
        return pic;
    }

    public String getText() {
        return text;
    }
}