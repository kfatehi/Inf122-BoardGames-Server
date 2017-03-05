package com.jlinnell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by james on 3/4/17.
 */
public class Util {

    public static String inputStreamToString(InputStream is) {
        // I am very annoyed Java doesn't have a simple builtin
        // way of doing this.
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            System.out.println("Error turning InputStream into string");
            return "";
        }
        return builder.toString();
    }
}
