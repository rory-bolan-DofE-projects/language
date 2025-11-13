package app.belgarion.java.expr_eval;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * aaaaaaaaaaaaaaaaaaaaaaaa expression evaluator
 */
public class Evaluator {

    public static Answer run(String str) throws Exception {
        /*
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter expression");
        String str = scanner.nextLine();

         */
        long start = System.nanoTime();
        ArrayList<Token> tokens = Tokenizer.Tokenize(str);
        tokens = MultiDigitInteger(tokens);
        for (Token token : tokens) {
            System.out.printf("Token type: %s value %d\n", token.TokenType, token.value);
        }
        System.out.println("------");
        while (in(new Token(Tokens.MULTIPLY, null),tokens ) || in( new Token(Tokens.DIVIDE, null), tokens)) {
            tokens = PassMultiplyDivide(tokens);
        }
        while (in(new Token(Tokens.PLUS, null),tokens ) || in( new Token(Tokens.MINUS, null), tokens)) {
            tokens = PassAddSubtract(tokens);
        }
        for (Token token : tokens) {
            System.out.printf("Token type: %s value %d\n", token.TokenType, token.value);
        }
        System.out.println();
        long end = System.nanoTime();
        return new Answer(tokens.getFirst().value, end-start);
    }
    public static Answer run() throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter expression");
        String str = scanner.nextLine();


        long start = System.nanoTime();
        ArrayList<Token> tokens = Tokenizer.Tokenize(str);
        tokens = MultiDigitInteger(tokens);
        for (Token token : tokens) {
            System.out.printf("Token type: %s value %d\n", token.TokenType, token.value);
        }
        System.out.println("------");
        while (in(new Token(Tokens.MULTIPLY, null),tokens ) || in( new Token(Tokens.DIVIDE, null), tokens)) {
            tokens = PassMultiplyDivide(tokens);
        }
        while (in(new Token(Tokens.PLUS, null),tokens ) || in( new Token(Tokens.MINUS, null), tokens)) {
            tokens = PassAddSubtract(tokens);
        }
        for (Token token : tokens) {
            System.out.printf("Token type: %s value %d\n", token.TokenType, token.value);
        }
        System.out.println();
        long end = System.nanoTime();
        return new Answer(tokens.getFirst().value, end-start);
    }
    public static @NotNull ArrayList<Token> MultiDigitInteger(@NotNull ArrayList<Token> tokens) throws InvalidTokenException {
        ArrayList<Token> newTokenList = nw();
        ArrayList<Token> number = nw();
        if (tokens.getFirst().TokenType != Tokens.INTEGER) throw new InvalidTokenException(tokens.getFirst());
        for (Token token : tokens) {
            if (token.TokenType == Tokens.INTEGER) {
                number.add(token);
            } else {
                newTokenList.add(TokenArrayToMultiDigitInt(number));
                newTokenList.add(token);
                number = nw();
            }
        }
        if (!number.isEmpty()) {
            newTokenList.add(TokenArrayToMultiDigitInt(number));
        }
        return newTokenList;
    }

    @Contract("_ -> new")
    public static @NotNull Token TokenArrayToMultiDigitInt(@NotNull ArrayList<Token> num) throws InvalidTokenException {
        int val = 0;

        for (Token digit : num) {
            if (digit.TokenType != Tokens.INTEGER)
                throw new InvalidTokenException(digit);


            val = val * 10 + digit.value;
        }

        return new Token(Tokens.INTEGER, val);
    }

    @Contract(value = " -> new", pure = true)
    public static <T> @NotNull ArrayList<T> nw() {
        return new ArrayList<>();
    }
    public static ArrayList<Token> PassMultiplyDivide(ArrayList<Token> tokens) throws InvalidTokenException {
        ArrayList<Token> finalList = new ArrayList<>(tokens);

        for (int i = 0; i < finalList.size(); i++) {
            Token token = finalList.get(i);
            if (token.TokenType == Tokens.MULTIPLY || token.TokenType == Tokens.DIVIDE || token.TokenType == Tokens.MODULO) {
                Token left = finalList.get(i - 1);
                Token right = finalList.get(i + 1);
                @Nullable Integer result = null;

                try {

                    if (token.TokenType == Tokens.MULTIPLY) {
                        result = left.value * right.value;
                    } else if (token.TokenType == Tokens.DIVIDE) { // DIVIDE
                        if (right.value == 0) throw new DivisionByZeroException(String.format("Attempted to divide %d by %d", left.value, right.value));
                        result = left.value / right.value;

                    } else {
                        if (right.value == 0) throw new DivisionByZeroException(String.format("Attempted to divide %d by %d", left.value, right.value));
                        result = left.value % right.value;

                    }
                } catch (NullPointerException e) {
                    System.out.println("Error: " + e);
                    throw new InvalidTokenException(token);
                }
                // Replace left, operator, right with result
                finalList.set(i - 1, new Token(Tokens.INTEGER, result));
                finalList.remove(i); // remove operator
                finalList.remove(i); // remove right (note: same index, because list shifted)

                i--; // step back so we don't skip tokens after modification
            }
        }

        return finalList;
    }
    public static ArrayList<Token> PassAddSubtract(ArrayList<Token> tokens) throws InvalidTokenException {
        ArrayList<Token> finalList = new ArrayList<>(tokens);

        for (int i = 0; i < finalList.size(); i++) {
            Token token = finalList.get(i);
            if (token.TokenType == Tokens.PLUS || token.TokenType == Tokens.MINUS) {
                Token left = finalList.get(i - 1);
                Token right = finalList.get(i + 1);
                @Nullable Integer result = null;
                try {

                    if (token.TokenType == Tokens.PLUS) {
                        result = left.value + right.value;
                    } else { // SUBTRACT
                        result = left.value - right.value;
                    }
                } catch (NullPointerException e) {
                    System.out.println("Error: " + e);
                    throw new InvalidTokenException(new Token(Tokens.PLUS, null));
                }
                // Replace left, operator, right with result
                finalList.set(i - 1, new Token(Tokens.INTEGER, result));
                finalList.remove(i); // remove operator
                finalList.remove(i); // remove right (note: same index, because list shifted)

                i--; // step back so we don't skip tokens after modification
            }
        }

        return finalList;
    }
    public static <T> boolean in(T object, ArrayList<T> array) {
        for (T obj : array) {
            if (obj.equals( object)) return true;
        }
        return false;
    }
}
