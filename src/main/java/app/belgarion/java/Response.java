package app.belgarion.java;

import java.util.ArrayList;
import java.util.HashMap;

record Response(ArrayList<Token> tokens, HashMap<String, Value> variables) {

}
