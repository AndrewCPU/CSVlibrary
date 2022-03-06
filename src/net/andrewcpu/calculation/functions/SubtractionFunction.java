package net.andrewcpu.calculation.functions;

import net.andrewcpu.exceptions.InvalidParameterException;

public class SubtractionFunction extends Function{
    public SubtractionFunction(){
        super("SUB", 2);
    }

    @Override
    public double calculate(double... parameters) throws InvalidParameterException {
        if(parameters.length != 2){
            throw new InvalidParameterException();
        }
        return parameters[0] - parameters[1];
    }
}
