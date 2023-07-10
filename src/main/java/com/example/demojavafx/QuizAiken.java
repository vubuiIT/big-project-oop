package com.example.demojavafx;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class QuizAiken {
    String Question;
    List<ChoiceAiken> choiceList;
    String Answers;
    List<BufferedImage> Illustrators;
    public QuizAiken() {
        choiceList = new ArrayList<ChoiceAiken>();
        Illustrators = new ArrayList<BufferedImage>();
    }
    public void setQuestion(String s) {
        this.Question = s;
    }
    public void setChoices(ChoiceAiken c) {
        this.choiceList.add(c);
    }
    public void setChoices(List<ChoiceAiken> l) {
        this.choiceList = l;
    }
    public void setAnswers (String s) {
        this.Answers = s;
    }
    public void setIllustrator (BufferedImage img) {
        this.Illustrators.add(img);
    }
    public void setIllustrator (List<BufferedImage> imgs) {
        this.Illustrators = imgs;
    }
    public String getQuestion() {
        return this.Question;
    }
    public List<ChoiceAiken> getChoices() {
        return choiceList;
    }
    public String getAnswers () {
        return this.Answers;
    }
    public List<BufferedImage> getIllustrators () { return this.Illustrators; }
}
