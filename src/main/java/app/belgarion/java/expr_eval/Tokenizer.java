package app.belgarion.java.expr_eval;

import java.util.ArrayList;

public class Tokenizer {
    public static ArrayList<Token> Tokenize(String str) throws Exception {
        str = str.replace(" ", "");
        char[] strCharArray = str.toCharArray();
        ArrayList<Token> token_list = new ArrayList<>();

        for (char character : strCharArray) {
            if (isIllegalCharacter(character)) throw new Exception("Illegal character: " + character);
            switch (character) {
                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9':
                    // Convert char digit -> int and create INTEGER token
                    int value = character - '0';
                    token_list.add(new Token(Tokens.INTEGER, value));
                    break;

                case '+':
                    token_list.add(new Token(Tokens.PLUS, null));
                    break;
                case '-':
                    token_list.add(new Token(Tokens.MINUS, null));
                    break;
                case '/':
                    token_list.add(new Token(Tokens.DIVIDE, null));
                    break;
                case '%':
                    token_list.add(new Token(Tokens.MODULO, null));
                    break;
                case '*':
                    token_list.add(new Token(Tokens.MULTIPLY, null));
                    break;

                default:
                    // Unknown character → maybe error handling later
                    break;
            }

        }
        return token_list;
    }
    public static boolean isIllegalCharacter(char ch)  {

        char[] allowed = {'0','1','2','3','4','5','6','7','8','9','+','-','/','*','%'};
        for (char cha : allowed) {
            if (ch == cha) {
                return false;
            }
        }
        return true;
    }
}
