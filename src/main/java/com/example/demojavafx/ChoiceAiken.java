package com.example.demojavafx;

import java.awt.image.BufferedImage;
import java.util.List;

public class ChoiceAiken {
    String choiceText;
    List<BufferedImage> imageList;
    double Grade;
    public ChoiceAiken(String s, List<BufferedImage> imgs, double x) {
        this.choiceText = s;
        this.Grade = x;
        this.imageList = imgs;
    }
    public ChoiceAiken(String s, List<BufferedImage> imgs) {
        this.choiceText = s;
        this.imageList = imgs;
    }
    public ChoiceAiken(String s) {
        this.choiceText = s;
    }
    public ChoiceAiken(String s, double x) {
        this.choiceText = s;
        this.Grade = x;
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
    public List<BufferedImage> getImage() { return this.imageList;}
    public void setImageList(List<BufferedImage> imgs) {
        this.imageList.addAll(imgs);
    }
    public void setImageList(BufferedImage img) {
        this.imageList.add(img);
    }
}
