package com.chrisgruber.thinmatrixgame.graphics.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {

    private FileUtils() {
    }

    public static String loadAsString(String file) {
        StringBuilder result = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String buffer = "";

            while ((buffer = reader.readLine()) != null) {
                result.append(buffer).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Could not read shader source file: " + file);
            e.printStackTrace();
            System.exit(-1);
        }

        return result.toString();
    }

}
