package net.andrewcpu.calculation.functions;

import net.andrewcpu.exceptions.InvalidParameterException;

public class ExponentFunction extends Function{
    public ExponentFunction() {
        super("POW", 2);
    }

    @Override
    public double calculate(double... parameters) throws InvalidParameterException {
        if(parameters.length != 2){
            throw new InvalidParameterException();
        }
        return Math.pow(parameters[0], parameters[1]);
    }
}
