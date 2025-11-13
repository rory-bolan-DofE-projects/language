package app.belgarion.java;


import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java -jar own-lang-OwO-1.0.jar <path to file>");
        }
        String path = args[0];
        TokenObjects.Response resp = ReadAndTokenize.tokenize(new File(path));
    }
}