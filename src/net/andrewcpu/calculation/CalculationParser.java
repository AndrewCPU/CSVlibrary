package net.andrewcpu.calculation;

import net.andrewcpu.Spreadsheet;
import net.andrewcpu.calculation.functions.*;
import net.andrewcpu.calculation.reflectable.Bootloader;
import net.andrewcpu.calculation.reflectable.ExternalFunctionData;
import net.andrewcpu.calculation.reflectable.ReflectableFunction;
import net.andrewcpu.exceptions.InvalidParameterException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CalculationParser {
    private Spreadsheet spreadsheet;
    private List<Function> functions = new ArrayList<>();
    public CalculationParser(Spreadsheet spreadsheet) {
        this.spreadsheet = spreadsheet;
        functions.add(new AbsoluteValueFunction());
        functions.add(new AdditionFunction());
        functions.add(new DivisionFunction());
        functions.add(new MultiplicationFunction());
        functions.add(new ExponentFunction());
        functions.add(new SumFunction());
        functions.add(new SubtractionFunction());
        if(new File("functions").exists() && new File("functions").isDirectory()){
            System.out.println("Loading functions from root directory...");
            try{
                List<ReflectableFunction> externalFunctions = Bootloader.boot();
                functions.addAll(externalFunctions);
                System.out.println("Loaded " + externalFunctions.size() + " external function(s).");
            }catch (Exception ex){
                System.out.println("An error occurred while loading an external function.");
            }
        }
        System.out.println("Loaded the following external functions: ");
        for(Function function : functions){
            if(function instanceof ReflectableFunction){
                System.out.println("- " + function.getName());
            }
        }
    }

    public Function functionLookup(String name){
        for(Function function : functions){
            if(function.getName().equalsIgnoreCase(name)){
                return function;
            }
        }
        return null;
    }




    public double evaluate(String equation) throws InvalidParameterException {
        while(equation.contains("(")){
            int firstCloseIndex = equation.indexOf(')');
            if(firstCloseIndex == -1){
                return -1;
            }
            int firstOpenIndex = firstCloseIndex;
            while(equation.charAt(firstOpenIndex) != '('){
                firstOpenIndex--;
            }
            int functionEndIndex = firstOpenIndex - 1;
            int functionStartIndex = functionEndIndex;
            while(functionStartIndex > 0 && Character.isLetter(equation.charAt(functionStartIndex))){
                functionStartIndex--;
            }
            String functionName = (equation.substring(functionStartIndex, functionEndIndex + 1)).replaceAll("\\(", "").replaceAll(",", "");
            String functionValue = (equation.substring(firstOpenIndex + 1, firstCloseIndex));
            Function f = functionLookup(functionName);
            String[] parameters = functionValue.split(",");
            double[] d = new double[parameters.length];
            for(int i = 0; i<d.length; i++){
                d[i] = Double.parseDouble(parameters[i]);
            }
            equation = equation.replaceAll(functionName + "\\(" + functionValue + "\\)", f.calculate(d) + "");
        }
        return Double.parseDouble(equation);
    }


    public String parse(String calculation) throws InvalidParameterException {
        String equation = calculation.substring(1).replaceAll(" ", "");
        String rebuilt;

        Pattern range = Pattern.compile("(\\d+)[:](\\d+)[-](\\d+)[:](\\d+)");
        Matcher rangeMatcher = range.matcher(equation);
        HashMap<String, String> replaceTo = new HashMap<>();
        while(rangeMatcher.find()){
            String data = rangeMatcher.group();
            String[] fromTo = data.split("-");
            String[] piece1 = fromTo[0].split(":");
            String[] piece2 = fromTo[1].split(":");
            int row1 = Integer.parseInt(piece1[0]);
            int col1 = Integer.parseInt(piece1[1]);
            int row2 = Integer.parseInt(piece2[0]);
            int col2 = Integer.parseInt(piece2[1]);
            String replace = "";
            for(int row = row1; row<=row2; row++){
                for(int col = col1; col<=col2; col++){
                    replace += (row + ":" + col + ",");
                }
            }
            replaceTo.put(data, replace.substring(0, replace.length() - 1));
        }

        for(String key : replaceTo.keySet()){
            equation = equation.replaceAll(key, replaceTo.get(key));
        }
        rebuilt = equation;
        Pattern pattern = Pattern.compile("(\\d+)[:](\\d+)");
        Matcher matcher = pattern.matcher(equation);
        while(matcher.find()){
            String data = matcher.group();
            String[] pos = data.split(":");
            int row = Integer.parseInt(pos[0]);
            int col = Integer.parseInt(pos[1]);
            double val;
            try{
                val = spreadsheet.getDouble(row, col);
            }catch (Exception e){
                val = evaluate(parse(spreadsheet.getString(row, col)));
            }
            rebuilt = rebuilt.replaceAll(data, val + "");
        }
        return rebuilt;
    }
}
