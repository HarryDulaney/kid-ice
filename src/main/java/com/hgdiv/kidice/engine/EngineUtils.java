package com.hgdiv.kidice.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class EngineUtils {

    public static String loadResource(File filePath) throws Exception {
        String result;
        try (InputStream in = new FileInputStream(filePath);
             Scanner input = new Scanner(in, StandardCharsets.UTF_8)) {
            result = input.useDelimiter("\\A").next();

        }
        return result;
    }


}
