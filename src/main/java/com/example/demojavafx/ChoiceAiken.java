package com.example.demojavafx;

public class ChoiceAiken {
    String choiceText;
    double Grade;
    public ChoiceAiken(String s, double x) {
        this.choiceText = s;
        this.Grade = x;
    }
    public ChoiceAiken(String s) {
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
