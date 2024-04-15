package com.kata.automowersimulation.model;

/**
 * @author LAHRICHI Youssef
 */
public record Lawn(int width, int height) {

    public boolean isWithinBounds(Position position) {
        return position.getX() >= 0 && position.getX() <= this.width
                && position.getY() >= 0 && position.getY() <= this.height;
    }
}

