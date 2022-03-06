package net.andrewcpu.calculation.functions;

import net.andrewcpu.exceptions.InvalidParameterException;

public class MultiplicationFunction extends Function{
    public MultiplicationFunction(){
        super("MULT", 9999);
    }

    @Override
    public double calculate(double... parameters) throws InvalidParameterException {
        if(parameters.length <= 1){
            throw new InvalidParameterException();
        }
        double d = parameters[0];
        for(int i = 1; i<parameters.length; i++){
            d *= parameters[i];
        }
        return d;
    }
}
