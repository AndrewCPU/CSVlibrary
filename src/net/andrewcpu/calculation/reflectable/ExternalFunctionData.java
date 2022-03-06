package net.andrewcpu.calculation.reflectable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;

public class ExternalFunctionData {
    private String name;
    private String main;

    public ExternalFunctionData(String name, String main) {
        this.name = name;
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

}
