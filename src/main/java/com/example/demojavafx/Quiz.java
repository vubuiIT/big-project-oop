package com.example.demojavafx;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    String Question;
    List<Choice> choiceList;
    String Answers;
    public Quiz() {
        choiceList = new ArrayList<Choice>();
    }
    public void setQuestion(String s) {
        this.Question = s;
    }
    public void setChoices(Choice c) {
        this.choiceList.add(c);
    }
    public void setChoices(List<Choice> l) {
        this.choiceList = l;
    }
    public void setAnswers (String s) {
        this.Answers = s;
    }
    public String getQuestion() {
        return this.Question;
    }
    public List<Choice> getChoices() {
        return choiceList;
    }
    public String getAnswers () {
        return this.Answers;
    }
}
