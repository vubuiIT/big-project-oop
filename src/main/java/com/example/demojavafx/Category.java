package com.example.demojavafx;

public class Category {
    private final int id;
    private final int parentId;
    private final String info;
    private final String name;

    public Category(int id, int parentId, String info, String name) {
        this.id = id;
        this.parentId = parentId;
        this.info = info;
        this.name = name;
    }

    // Getter methods

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    public String getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }
    private int amountQuesInCategory() {
        DatabaseConnector connector = new DatabaseConnector();
        connector.connect();
        int amountQuestions = connector.amountQuestionsFromCategory(id);
        connector.disconnect();
        return amountQuestions;
    }
    @Override
    public String toString() {
        if (id != -2)
            return name + " (" + amountQuesInCategory() + ")";
        else
            return name;
    }
}