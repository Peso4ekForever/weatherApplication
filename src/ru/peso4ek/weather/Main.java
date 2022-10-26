package ru.peso4ek.weather;

import ru.peso4ek.weather.utils.WebParser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        WebParser webParser = new WebParser();
        webParser.getCities();
    }
}
