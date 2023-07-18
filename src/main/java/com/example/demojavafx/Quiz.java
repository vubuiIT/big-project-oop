package com.example.demojavafx;

public class Quiz {
    private int id;
    private String name;
    private String description;
    private int displayDescription;
    private int enableTimeLimit;
    private int timeLimit;
    private int shuffle;

    public Quiz(int id, String name, String description, int displayDescription, int enableTimeLimit, int timeLimit, int shuffle) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.displayDescription = displayDescription;
        this.enableTimeLimit = enableTimeLimit;
        this.timeLimit = timeLimit;
        this.shuffle = shuffle;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDisplayDescription() {
        return displayDescription;
    }

    public int getEnableTimeLimit() {
        return enableTimeLimit;
    }

    public int getTimeLimit() {
        return timeLimit;
    }
    public int getShuffle() {
        return shuffle;
    }
}

