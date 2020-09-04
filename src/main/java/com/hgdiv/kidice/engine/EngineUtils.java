package com.hgdiv.kidice.engine;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EngineUtils {

    public static String loadResource(String filePath) throws Exception {
        String result;
        try (InputStream in = new FileInputStream(filePath);
             Scanner input = new Scanner(in, StandardCharsets.UTF_8)) {
            result = input.useDelimiter("\\A").next();

        }
        return result;
    }

    public static List<String> readAllLines(String fileName) throws Exception {
        List<String> list = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }
}
