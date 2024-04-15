package com.kata.automowersimulation.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.sql.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LAHRICHI Youssef
 */

@Getter
@Setter
public class Mower {
    private static final Logger logger = LoggerFactory.getLogger(Mower.class);
    private Position position;
    private Lawn lawn;
    private String instructions;

    public Mower(Position position, Lawn lawn) {
        this.position = position;
        this.lawn = lawn;
        this.instructions = "";
    }

    public Mower(Position position, Lawn lawn, String instructions) {
        this.position = position;
        this.lawn = lawn;
        this.instructions = instructions;
    }

    public void turnRight() {
        logger.info("Mower turning right from orientation {}", position.getOrientation());
        this.position.setOrientation(this.position.getOrientation().turnRight());
    }

    public void turnLeft() {
        logger.info("Mower turning left from orientation {}", position.getOrientation());
        this.position.setOrientation(this.position.getOrientation().turnLeft());
    }

    public void move(char instruction) {
        switch (instruction) {
            case 'A' -> moveForward();
            case 'D' -> turnRight();
            case 'G' -> turnLeft();
            default -> logger.warn("Unknown statement: {}", instruction);
        }
    }

    public void moveForward() {
        Position nextPosition = calculateNextPosition();
        if (lawn.isWithinBounds(nextPosition)) {
            this.position = nextPosition;
            logger.info("Mower moving to ({}, {}) {}", position.getX(), position.getY(), position.getOrientation());
        } else {
            logger.info("Attempted move out of bounds ignored.");
        }
    }

    private Position calculateNextPosition() {
        int x = position.getX();
        int y = position.getY();
        switch (position.getOrientation()) {
            case N -> y++;
            case E -> x++;
            case S -> y--;
            case W -> x--;
        }
        return new Position(x, y, position.getOrientation());
    }

}
