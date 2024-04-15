package com.kata.automowersimulation.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author LAHRICHI Youssef
 */

@Data
@AllArgsConstructor
public class Position {

    private int x;
    private int y;
    private Orientation orientation;
}
