package com.example.demojavafx;

public class ChoiceAiken {
    String choiceText;
    float Grade;
    public ChoiceAiken(String s, float x) {
        this.choiceText = s;
        this.Grade = x;
    }
    public ChoiceAiken(String s) {
        this.choiceText = s;
    }
    public void setChoiceText(String s){
        this.choiceText = s;
    }
    public void setGrade(float x){
        this.Grade = x;
    }
    public String getChoiceText(){
        return this.choiceText;
    }
    public float getGrade() {
        return this.Grade;
    }
}
