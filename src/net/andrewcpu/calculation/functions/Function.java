package net.andrewcpu.calculation.functions;

import net.andrewcpu.exceptions.InvalidParameterException;

public abstract class Function {
    private String name;
    private int parameterCount;

    public Function(String name, int parameterCount) {
        this.name = name;
        this.parameterCount = parameterCount;
    }

    public abstract double calculate(double... parameters) throws InvalidParameterException;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParameterCount() {
        return parameterCount;
    }

}
