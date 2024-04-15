package com.kata.automowersimulation.model;

/**
 * @author LAHRICHI Youssef
 */
public record Lawn(int width, int height) {

    // Method for checking if a position is within the lawn boundaries
    public boolean isWithinBounds(Position position) {
        return position.getX() >= 0 && position.getX() <= this.width
                && position.getY() >= 0 && position.getY() <= this.height;
    }
}

