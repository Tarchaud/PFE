package com.pfe.wakfubuilder.model;

public class Build {

    public enum Cost {
        low,
        medium,
        high
    }

    private long id;
    private String name;
    private int level;
    private Cost cost;

    public Build () {

    }
    public Build(long id, String name, int level, Cost cost) {
        super();
        this.id = id;
        this.name = name;
        this.level = level;
        this.cost = cost;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Cost getCost() {
        return cost;
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }
}