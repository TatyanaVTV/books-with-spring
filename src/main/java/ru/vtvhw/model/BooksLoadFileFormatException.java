package ru.vtvhw.model;

public class BooksLoadFileFormatException extends RuntimeException {
    private final static String FORMAT_MSG = "Wrong format of file for books loading. " +
            "Correct format - 4 columns divided by ';', first three - String, fourth - Integer.";

    public BooksLoadFileFormatException(Exception e) {
        super(String.format("%s%n%s", FORMAT_MSG, e.getLocalizedMessage()));
    }

    public BooksLoadFileFormatException() {
        super(FORMAT_MSG);
    }
}