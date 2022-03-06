package net.Andrewcpu.pythag;

import net.andrewcpu.calculation.reflectable.ReflectableFunction;
import net.andrewcpu.exceptions.InvalidParameterException;

public class PythagoreanTheorem extends ReflectableFunction {
    public PythagoreanTheorem() {
        super();
    }

    @Override
    public double calculate(double... doubles) throws InvalidParameterException {
        if(doubles.length != 4){
            throw new InvalidParameterException();
        }
        return Math.sqrt(Math.pow(doubles[0] - doubles[2], 2) + Math.pow(doubles[1] - doubles[3], 2));
    }
}
