package net.andrewcpu.exceptions;

public class RangeOutOfBoundsException extends Exception{
    public RangeOutOfBoundsException() {
        super("Target range is out of bounds.");
    }
}
