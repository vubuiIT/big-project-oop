package com.example.demojavafx;
//
public class Choice {
    String choiceText;
    double Grade;
    public Choice (String s, double x) {
        this.choiceText = s;
        this.Grade = x;
    }
    public Choice (String s) {
        this.choiceText = s;
    }
    public void setChoiceText(String s){
        this.choiceText = s;
    }
    public void setGrade(double x){
        this.Grade = x;
    }
    public String getChoiceText(){
        return this.choiceText;
    }
    public double getGrade() {
        return this.Grade;
    }
}
