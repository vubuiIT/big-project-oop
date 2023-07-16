package com.example.demojavafx;


public class Choices {
    private final int id;
    private final int questionId;
    private final float grade;
    private final byte[] pic;
    private final String text;
    private final String picName;

    public Choices(int id, int questionId, float grade, byte[] pic, String text, String picName) {
        this.id = id;
        this.questionId = questionId;
        this.grade = grade;
        this.pic = pic;
        this.text = text;
        this.picName = picName;
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

    public byte[] getPic() {
        return pic;
    }

    public String getText() {
        return text;
    }
    public String getPicName() {
        return picName;
    }
}