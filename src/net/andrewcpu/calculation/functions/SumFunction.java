package net.andrewcpu.calculation.functions;

import net.andrewcpu.exceptions.InvalidParameterException;

import java.util.Arrays;

public class SumFunction extends Function{
    public SumFunction() {
        super("SUM", 9999);
    }

    @Override
    public double calculate(double... parameters) throws InvalidParameterException {
        if(parameters.length > getParameterCount()){
            throw new InvalidParameterException();
        }
        return Arrays.stream(parameters).sum();
    }
}
