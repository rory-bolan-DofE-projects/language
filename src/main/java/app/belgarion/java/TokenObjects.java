package app.belgarion.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class TokenObjects {
    record Response(ArrayList<Token> tokens, HashMap<String, Value> variables) {

    }
    record Function(String name, ArrayList<String> params, String contents) {

    }
    static class IncorrectSyntaxException extends RuntimeException {
        public IncorrectSyntaxException(String message) {
            super(message);
        }
    }
    record Value(Type type, String contents) {
    }
    enum Keyword {
        OUT, // PRINT
        SET, // VARIABLE
        IS, // IF
        OR, // ELSE IF
        NOT, // ELSE
        WHILE, // WHILE
        FOR, // FOR
        FUN, // FUNCTION
        GIVE, // RETURN
        OBJECT, // CLASS

        EOF, // END OF FILE

    }
    enum Type {
        CHAR,
        STRING,
        INT,
        FLOAT,
        DOUBLE,
        BOOLEAN,

    }
    public record Token(Keyword keyword, Optional<String> name, Optional<ArrayList<String>> params, Value value) {

    }

}
