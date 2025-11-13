package app.belgarion.java.expr_eval;

public class InvalidTokenException extends Exception {

    public InvalidTokenException(Token token) {
        super(createErrorMessage(token));
    }

    private static String createErrorMessage(Token token) {
        if (token.TokenType == Tokens.INTEGER) {
            return String.format("Incorrect use of token %d", token.value);
        } else {
            return switch (token.TokenType) {
                case MULTIPLY -> "Incorrect use of token *";
                case DIVIDE -> "Incorrect use of token /";
                case MODULO -> "Incorrect use of token %";
                case MINUS -> "Incorrect use of token -";
                case PLUS -> "Incorrect use of token +";
                default -> "Incorrect use of an unknown token";
            };
        }
    }
}