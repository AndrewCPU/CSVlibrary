package net.andrewcpu.calculation.reflectable;

import net.andrewcpu.calculation.functions.Function;
import net.andrewcpu.exceptions.InvalidParameterException;

public abstract class ReflectableFunction extends Function {

    public ReflectableFunction() {
        super("", 999);
    }



    public abstract double calculate(double... parameters) throws InvalidParameterException;
}
