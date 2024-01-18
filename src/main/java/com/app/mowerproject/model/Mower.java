package com.app.mowerproject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InvalidObjectException;
import java.util.Objects;

/**
 * @author josephelbacha
 * Mower represents the state of a mower.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Mower {
    private int x;
    private int y;
    private char orientation;

    public static final String ORIENTATION_IS_INCORRECT = "Orientation is incorrect";
    public static final String INSTRUCTION_IS_INCORRECT = "Instruction is incorrect";

    @Override
    public boolean equals(Object mowerObj) {
        if (this == mowerObj) return true;
        if (mowerObj == null || getClass() != mowerObj.getClass()) return false;
        Mower mower = (Mower) mowerObj;
        return x == mower.x &&
                y == mower.y &&
                orientation == mower.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, orientation);
    }

    /**
     * Move the mower based on the instruction.
     * @param instruction char to move the mower
     */
    public void move(char instruction) throws InvalidObjectException {
        switch (instruction) {
            case 'R' -> rotateRight();
            case 'L' -> rotateLeft();
            case 'F' -> moveForward();
            default -> throw new InvalidObjectException(INSTRUCTION_IS_INCORRECT);
        }
    }

    /**
     * rotateRight the mower 90 degrees to the right.
     */
    private void rotateRight() throws InvalidObjectException {
        switch (orientation) {
            case 'N' -> orientation = 'E';
            case 'E' -> orientation = 'S';
            case 'S' -> orientation = 'W';
            case 'W' -> orientation = 'N';
            default -> throw new InvalidObjectException(ORIENTATION_IS_INCORRECT);
        }
    }

    /**
     * rotateLeft the mower 90 degrees to the left.
     */
    private void rotateLeft() throws InvalidObjectException {
        switch (orientation) {
            case 'N' -> orientation = 'W';
            case 'W' -> orientation = 'S';
            case 'S' -> orientation = 'E';
            case 'E' -> orientation = 'N';
            default -> throw new InvalidObjectException(ORIENTATION_IS_INCORRECT);
        }
    }

    /**
     * moveForward the mower forward one space in the direction it faces.
     */
    private void moveForward() throws InvalidObjectException {
        switch (orientation) {
            case 'N' -> y++;
            case 'E' -> x++;
            case 'S' -> y--;
            case 'W' -> x--;
            default -> throw new InvalidObjectException(ORIENTATION_IS_INCORRECT);
        }
    }

    /**
     * moveBackward the mower backward one space to revert an invalid move.
     */
    public void moveBackward() throws InvalidObjectException {
        switch (orientation) {
            case 'N' -> y--;
            case 'E' -> x--;
            case 'S' -> y++;
            case 'W' -> x++;
            default -> throw new InvalidObjectException(ORIENTATION_IS_INCORRECT);
        }
    }
}
