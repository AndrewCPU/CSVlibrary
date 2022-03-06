package net.andrewcpu;

import net.andrewcpu.calculation.CalculationParser;
import net.andrewcpu.exceptions.InvalidParameterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class Spreadsheet {
    private String path;
    private String[][] data;
    private String deliminator = ",";
    private CalculationParser calculationParser;

    /**
     * Loads a spreadsheet from a given path with the default "," deliminator
     *
     * @param path
     * @throws IOException
     */
    public Spreadsheet(String path) throws IOException {
        this.path = path;
        calculationParser = new CalculationParser(this);
        _loadFile();
        try {
            updateCalculations();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }

    }

    /**
     * Loads a spreadsheet from a given path with the given deliminator
     *
     * @param path
     * @param deliminator
     * @throws IOException
     */
    public Spreadsheet(String path, String deliminator) throws IOException {
        this.path = path;
        this.deliminator = deliminator;
        calculationParser = new CalculationParser(this);
        _loadFile();
        try {
            updateCalculations();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }

    }

    public CalculationParser getCalculationParser() {
        return calculationParser;
    }

    public void reloadCalculationParser() {
        calculationParser = new CalculationParser(this);
    }

    /**
     * Returns an inclusive string array between row 1, column 1, and row 2, column 2
     *
     * @param row1
     * @param column1
     * @param row2
     * @param column2
     * @return
     */
    public String[][] getRange(int row1, int column1, int row2, int column2) {
        String[][] response = new String[row2 - row1 + 1][column2 - column1 + 1];
        for (int column = column1; column <= column2; column++) {
            for (int row = row1; row <= row2; row++) {
                response[row - row1][column - column1] = getString(row, column);
            }
        }
        return response;
    }

    /**
     * @param row
     * @return Returns a String array of all values in a row
     */
    public String[] getRow(int row) {
        return this.data[row];
    }

    /**
     * @param column
     * @return Returns a String array of all values in a column
     */
    public String[] getColumn(int column) {
        String[][] data = getRange(0, column, getTotalRows() - 1, column);
        String[] response = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            response[i] = data[i][0];
        }
        return response;
    }

    /**
     * @return The total rows in a sheet
     */
    public int getTotalRows() {
        return this.data.length;
    }

    /**
     * @return The total columns in a sheet
     */
    public int getTotalColumns() {
        return this.data[0].length;
    }


    public void resize(int rowAmount, int colAmount) {
        String[][] newData = new String[rowAmount][colAmount];
        for (int row = 0; row < newData.length; row++) {
            for (int col = 0; col < newData[row].length; col++) {
                if (row < this.data.length && col < this.data[row].length) {
                    newData[row][col] = this.data[row][col];
                }
                else{
                    newData[row][col] = "";
                }
            }
        }
        this.data = newData;
    }

    /**
     * Updates a cells data
     *
     * @param row
     * @param column
     * @param data
     */
    public void setData(int row, int column, String data) throws InvalidParameterException {
        if (row >= this.data.length) {
            resize(row + 1, this.data[0].length);
        }
        if (column >= this.data[row].length) {
            resize(this.data.length, column + 1);
        }
        this.data[row][column] = data;
        updateCalculations();
    }

    /**
     * Returns the string value in a given cell.
     *
     * @param row
     * @param column
     * @return Returns the string value in a given cell.
     */
    public String getString(int row, int column) {
        return this.data[row][column];
    }

    /**
     * Returns the int value in a given cell.
     *
     * @param row
     * @param column
     * @return Returns the int value in a given cell.
     */
    public int getInt(int row, int column) {
        return Integer.parseInt(getString(row, column).trim());
    }

    /**
     * Returns the double value in a given cell.
     *
     * @param row
     * @param column
     * @return Returns the double value in a given cell.
     */
    public double getDouble(int row, int column) {
        return Double.parseDouble(getString(row, column).trim());
    }

    /**
     * Returns the boolean value in a given cell.
     *
     * @param row
     * @param column
     * @return Returns the boolean value in a given cell.
     */
    public boolean getBoolean(int row, int column) {
        return Boolean.parseBoolean(getString(row, column).trim());
    }

    /**
     * Returns the long value in a given cell.
     *
     * @param row
     * @param column
     * @return Returns the long value in a given cell.
     */
    public long getLong(int row, int column) {
        return Long.parseLong(getString(row, column).trim());
    }

    /**
     * Returns the short value in a given cell.
     *
     * @param row
     * @param column
     * @return Returns the short value in a given cell.
     */
    public short getShort(int row, int column) {
        return Short.parseShort(getString(row, column).trim());
    }

    /**
     * Returns the byte value in a given cell.
     *
     * @param row
     * @param column
     * @return Returns the byte value in a given cell.
     */
    public byte getByte(int row, int column) {
        return Byte.parseByte(getString(row, column).trim());
    }

    /**
     * Prints out the spreadsheet in tabular format
     */
    public void printSpreadsheet() {
        int[] colLength = new int[this.data[0].length];
        for (int i = 0; i < colLength.length; i++) {
            colLength[i] = getLongestEntryLengthForColumn(i) + 3;
        }
        for (int row = 0; row < this.data.length; row++) {
            for (int col = 0; col < this.data[row].length; col++) {
                String point = this.data[row][col]
                        .replaceAll("\r", "")
                        .replaceAll("(\\\\)(" + deliminator + ")", deliminator);
                String spaces = String.join("", Collections.nCopies((colLength[col] - point.length()), " "));
                System.out.print(point + spaces);
            }
            System.out.println();
        }
        try {
            updateCalculations();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
    }

    public void printSpreadsheet(int row1, int col1, int row2, int col2) {
        int[] colLength = new int[this.data[0].length];
        for (int i = 0; i < colLength.length; i++) {
            colLength[i] = getLongestEntryLengthForColumn(i) + 3;
        }
        for (int row = row1; row <= row2; row++) {
            for (int col = col1; col <= col2; col++) {
                String point = this.data[row][col]
                        .replaceAll("\r", "")
                        .replaceAll("(\\\\)(" + deliminator + ")", deliminator);
                String spaces = String.join("", Collections.nCopies((colLength[col] - point.length()), " "));
                System.out.print(point + spaces);
            }
            System.out.println();
        }
    }

    public String getDeliminator() {
        return deliminator;
    }

    public String getPath() {
        return path;
    }

    /**
     * Updates the spreadsheet deliminator
     * <p>
     * Will update in file once saved.
     *
     * @param deliminator
     */
    public void setDeliminator(String deliminator) {
        this.deliminator = deliminator;
    }

    /**
     * Loads the file into a string and places the data into a multidimensional string array
     *
     * @throws IOException
     */
    private void _loadFile() throws IOException {
        File file = new File(path);
        String data = Files.readString(Path.of(file.getAbsolutePath()));
        String[] lines = data.split("\n");
        int rowsSize = lines.length;
        int columnsSize = 0;
        for (String c : lines) {
            int amountOfDeliminators = (int) (c.split("(?<!\\\\)[" + deliminator + "]").length);
            if (amountOfDeliminators > columnsSize) {
                columnsSize = amountOfDeliminators;
            }
        }
        this.data = new String[rowsSize][columnsSize + 1];
        for (int rowNumber = 0; rowNumber < rowsSize; rowNumber++) {
            String row = lines[rowNumber];
            String[] columns = new String[columnsSize];
            String[] localData = row.split("(?<!\\\\)[" + deliminator + "]");
            for(int i = 0; i<columns.length; i++){
                if(i < localData.length){
                    columns[i] = localData[i];
                }
                else{
                    columns[i] = "";
                }
            }
            for (int i = 0; i < columns.length; i++) {
                columns[i] = columns[i].replaceAll("\\\\[" + deliminator + "]", deliminator);
            }
            this.data[rowNumber] = columns;
        }
    }

    public int getLongestEntryLengthForColumn(int column) {
        int longest = 0;
        for (int i = 0; i < this.data.length; i++) {
            if (this.data[i][column].length() > longest) {
                longest = this.data[i][column].length();
            }
        }
        return longest;
    }

    /**
     * Save the CSV file to the loaded path
     *
     * @throws IOException
     */
    public void save() throws IOException {
        saveToPath(null);
    }

    /**
     * @param path Path of the file you wish to save to
     * @throws IOException
     */
    public void saveToPath(String path) throws IOException {
        if (path == null) {
            path = this.path;
        }
        else{
            this.path = path;
        }
        String outputData = "";
        for (final String[] row : this.data) {
            for (String entry : row) {
                outputData += entry.replaceAll("[" + deliminator + "]", "\\\\" + deliminator) + deliminator;
            }
            outputData = outputData.substring(0, outputData.length() - 1) + "\n";
        }
        outputData = outputData.substring(0, outputData.length() - 1);
        FileOutputStream outputStream = new FileOutputStream(path);
        byte[] strToBytes = outputData.getBytes();
        outputStream.write(strToBytes);
        outputStream.close();
    }

    public void updateCalculations() throws InvalidParameterException {
        updateCalculations(false);
    }

    public void updateCalculations(boolean print) throws InvalidParameterException {
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                String value = getString(row, col);
                if (value.startsWith("=")) {
                    String parsed = (calculationParser.parse(value));
                    double val = calculationParser.evaluate(parsed);
                    if (print) {
                        System.out.println("(" + row + ", " + col + ") " + value + " = {" + val + "}");
                    }
                }
            }
        }
    }


}
