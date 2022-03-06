package net.andrewcpu;

import java.io.IOException;

public class CSVLibrary {
    /**
     * Loads a spreadsheet from a given path
     * @param path
     * @return
     * @throws IOException
     */
    public static Spreadsheet loadSpreadsheet(String path) throws IOException {
        return new Spreadsheet(path);
    }
    /**
     * Loads a spreadsheet from a given path with a given deliminator
     * @param path
     * @param deliminator
     * @return
     * @throws IOException
     */
    public static Spreadsheet loadSpreadsheet(String path, String deliminator) throws IOException{
        return new Spreadsheet(path, deliminator);
    }
}