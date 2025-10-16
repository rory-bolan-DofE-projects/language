package app.belgarion.java;

import java.util.ArrayList;

public class Function {
    public String name;
    public ArrayList<String> params;
    public String contents;
    public Function(String name, ArrayList<String> params, String contents) {
        this.name = name;
        this.params = params;
        this.contents = contents;
    }
}
