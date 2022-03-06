package net.andrewcpu.calculation.functions;

import net.andrewcpu.exceptions.InvalidParameterException;

public class AbsoluteValueFunction extends Function{

    public AbsoluteValueFunction() {
        super("ABS", 1);
    }

    @Override
    public double calculate(double... parameters) throws InvalidParameterException {
        if(parameters.length != 1){
            throw new InvalidParameterException();
        }
        return Math.abs(parameters[0]);
    }
}
