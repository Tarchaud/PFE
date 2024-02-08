package com.pfe.wakfubuilder.model;

import java.util.List;

public class BuildRequest {
    private String name;
    private int level;
    private Build.Cost cost;
    private List<Integer> effects;

    public BuildRequest() {
    }

    public BuildRequest(String name, int level, Build.Cost cost, List<Integer> effects) {
        this.name = name;
        this.level = level;
        this.cost = cost;
        this.effects = effects;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public Build.Cost getCost() {
        return cost;
    }

    public List<Integer> getEffects() {
        return effects;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setCost(Build.Cost cost) {
        this.cost = cost;
    }

    public void setEffects(List<Integer> effects) {
        this.effects = effects;
    }
}
