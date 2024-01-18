package com.app.mowerproject.model;

import org.junit.jupiter.api.Test;

import java.io.InvalidObjectException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MowerTest {

    /**
     * move_WithValidInstructions_ShouldMoveCorrectly
     * @throws InvalidObjectException If the input is invalid.
     */
    @Test
    void move_WithValidInstructions_ShouldMoveCorrectly() throws InvalidObjectException {
        Mower mower = new Mower(0, 0, 'N');

        mower.move('R');
        assertEquals('E', mower.getOrientation());

        mower.move('L');
        assertEquals('N', mower.getOrientation());

        mower.move('F');
        assertEquals(0, mower.getX());
        assertEquals(1, mower.getY());
    }

    /**
     * moveBackward_WithValidInstructions_ShouldMoveBackwardCorrectly
     * @throws InvalidObjectException If the input is invalid.
     */
    @Test
    void moveBackward_WithValidInstructions_ShouldMoveBackwardCorrectly() throws InvalidObjectException {
        Mower mower = new Mower(0, 0, 'N');

        mower.move('F');
        assertEquals(0, mower.getX());
        assertEquals(1, mower.getY());
        assertEquals('N', mower.getOrientation());

        mower.move('R');
        assertEquals(0, mower.getX());
        assertEquals(1, mower.getY());
        assertEquals('E', mower.getOrientation());


        mower.move('F');
        assertEquals(1, mower.getX());
        assertEquals(1, mower.getY());
        assertEquals('E', mower.getOrientation());

        mower.moveBackward();
        assertEquals(0, mower.getX());
        assertEquals(1, mower.getY());
        assertEquals('E', mower.getOrientation());

        mower.setOrientation('N');
        mower.moveBackward();
        assertEquals(0, mower.getX());
        assertEquals(0, mower.getY());
    }
}

