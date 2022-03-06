package net.andrewcpu.exceptions;

public class SelectionOutOfBounds extends Exception{
    public SelectionOutOfBounds() {
        super("Target cell is out of bounds");
    }
}
