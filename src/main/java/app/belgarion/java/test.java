package app.belgarion.java;

import java.io.File;
import java.io.IOException;
import app.belgarion.java.TokenObjects;

import static app.belgarion.java.ReadAndTokenize.*;

public class test {
    public static void main(String[] args) throws IOException {
        File file = new File("./sample.txt");


        TokenObjects.Response resp = tokenize(file);
    }
}
