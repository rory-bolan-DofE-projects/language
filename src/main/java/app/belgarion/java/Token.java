package app.belgarion.java;



import java.util.ArrayList;
import java.util.Optional;

public record Token(Keyword keyword, Optional<String> name,  Optional<ArrayList<String>> params, Value value) {

}
