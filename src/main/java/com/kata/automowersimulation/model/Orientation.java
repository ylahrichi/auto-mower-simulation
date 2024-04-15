package com.kata.automowersimulation.model;

/**
 * @author LAHRICHI Youssef
 */
public enum Orientation {
    N, E, S, W;

    public Orientation turnRight() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    public Orientation turnLeft() {
        return values()[(this.ordinal() + 3) % values().length];
    }
}
