package ru.min.coursework3.entity;

public enum Color {
    GREEN("green"),
    RED("red"),
    BLUE("blue"),
    BLACK("black"),
    ORANGE("orange"),
    YELLOW("yellow"),
    CYAN("cyan"),
    VIOLENT("violent");

    private String description;

    Color(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
