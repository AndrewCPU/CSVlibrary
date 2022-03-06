package net.andrewcpu.exceptions;

public class InvalidParameterException extends Exception{
    public InvalidParameterException() {
        super("Invalid parameter in calculation");
    }
}
