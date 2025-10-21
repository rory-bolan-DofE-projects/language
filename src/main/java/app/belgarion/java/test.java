package app.belgarion.java;

import java.io.File;
import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
        File file = new File("./sample.txt");


        TokenObjects.Response resp = ReadAndTokenize.tokenize(file);
    }
}
