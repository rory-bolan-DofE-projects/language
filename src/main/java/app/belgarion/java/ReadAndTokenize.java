package app.belgarion.java;


import java.io.*;
import java.util.*;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import app.belgarion.java.expr_eval.*;
class ReadAndTokenize {
    static ArrayList<TokenObjects.Token> tokens = new ArrayList<>();
    static ArrayList<TokenObjects.Function> functions = new ArrayList<>();
    static HashMap<String, TokenObjects.Value> variables = new HashMap<>();


    public static TokenObjects.Response tokenize(File file) throws IOException {
        if (!file.getName().endsWith(".uwu")) {
            throw new TokenObjects.IncorrectSyntaxException("File must end with '.uwu'");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean inFunction = false;
            StringBuilder functionContents = new StringBuilder();
            StringBuilder function_name = new StringBuilder();
            ArrayList<String> params2 = new ArrayList<>();
            boolean EOF = false;

            while ((line = reader.readLine()) != null) {
                // remove comments safely
                int commentIndex = line.indexOf("//");
                if (commentIndex != -1) {
                    line = line.substring(0, commentIndex);
                }

                line = line.trim().replace("}", "\n}");
                if (!line.endsWith(";") && !line.endsWith("{") && !line.endsWith("}") && !line.isEmpty())
                    throw new TokenObjects.IncorrectSyntaxException("Line (" + line + ") must end with ';' or '{'");
                if (line.equals("eof;")) {
                    EOF = true;
                    break;
                }
                char[] chars = line.toCharArray();
                if (chars.length == 0) continue;

                // VARIABLES
                {
                    Optional<Map.Entry<String, TokenObjects.Value>> var = getVariables(chars);
                    var.ifPresent(stringValueEntry -> variables.put(stringValueEntry.getKey(), stringValueEntry.getValue()));
                }

                // FUNCTION CREATION
                if (startsWith(chars, "fun")) {
                    // Reset everything for this new function
                    function_name = new StringBuilder();
                    params2 = new ArrayList<>();
                    functionContents.setLength(0); // clear old contents

                    // Parse function name
                    int index = 3; // after 'fun'
                    while (index < chars.length && Character.isWhitespace(chars[index])) index++;

                    // read until '('
                    while (index < chars.length && chars[index] != '(') {
                        if (!Character.isWhitespace(chars[index])) {
                            function_name.append(chars[index]);
                        }
                        index++;
                    }

                    //  Parse parameters
                    if (index >= chars.length || chars[index] != '(') {
                        throw new TokenObjects.IncorrectSyntaxException("Expected '(' after function name");
                    }
                    index++; // skip '('

                    StringBuilder currentParam = new StringBuilder();
                    while (index < chars.length && chars[index] != ')') {
                        char c = chars[index];
                        if (c == ',') {
                            if (!currentParam.isEmpty()) {
                                params2.add(currentParam.toString().trim());
                                currentParam.setLength(0);
                            }
                        } else {
                            currentParam.append(c);
                        }
                        index++;
                    }
                    if (!currentParam.isEmpty()) {
                        params2.add(currentParam.toString().trim());
                    }

                    // hopefully current char is '{'
                    while (index < chars.length && Character.isWhitespace(chars[index])) index++;
                    index += 2;
                    if (index >= chars.length || chars[index] != '{') {
                        throw new TokenObjects.IncorrectSyntaxException("Expected '{' after function definition");
                    }


                    // Debug output

                    // Mark we're now inside a function
                    inFunction = true;
                    continue; // skip appending this header line
                }

                // FUNCTION BODY
                if (inFunction) {
                    String trimmed = line.trim();
                    if (trimmed.equals("}") || trimmed.equals("};")) {
                        functions.add(new TokenObjects.Function(function_name.toString().trim(), params2, functionContents.toString().trim()));
                        inFunction = false;
                    } else {
                        // Accumulate function body
                        functionContents.append(line).append('\n');
                    }
                }
                // PRINT STATEMENTS
                print(chars);
                // CONDITIONALS
                if (startsWith(chars, "is")) {
                    if (chars[3] != '(') throw new TokenObjects.IncorrectSyntaxException("Expected '('");
                    int index = 4;

                    StringBuilder str = new StringBuilder();
                    while (index < chars.length && chars[index] != ')') {
                        str.append(chars[index]);
                        index++;
                    }
                    String string = str.toString().trim();
                    for (Map.Entry<String, TokenObjects.Value> entry : variables.entrySet()) {
                        string = string.replaceAll(entry.getKey(), entry.getValue().contents());
                    }
                    if (is(string)) {
                        System.out.println(str + " is true");
                    } else {
                        System.out.println(str + " is false");
                    }
                }
            }
            if (!EOF)
                throw new TokenObjects.IncorrectSyntaxException("\"eof;\" required to signify the end of the file");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        /*
        for (Map.Entry<String, Value> entry : variables.entrySet()) {

            System.out.println("variable: " + entry.getKey() + " = " + entry.getValue());
        }
        for (Function function : functions) {
            System.out.println("function: " + function);
        };
        */

        return new TokenObjects.Response(tokens, variables);
    }


    static Optional<Map.Entry<String, TokenObjects.Value>> getVariables(char[] chars) throws Exception {
        HashMap<String, TokenObjects.Value> variables_temp = new HashMap<>();
        if (startsWith(chars, "set")) {
            int index = 3;
            while (index < chars.length && Character.isWhitespace(chars[index])) index++;

            StringBuilder variable_name = new StringBuilder();
            while (index < chars.length && chars[index] != '=') {
                variable_name.append(chars[index]);
                index++;
            }

            // skip '='
            do index++;
            while (index < chars.length && Character.isWhitespace(chars[index]));

            StringBuilder variable_value = new StringBuilder();
            while (index < chars.length && chars[index] != ';') {
                variable_value.append(chars[index]);
                index++;
            }
            TokenObjects.Type finalType;
            String str = variable_value.toString();
            if (str.equals("true") || str.equals("false")) {
                finalType = TokenObjects.Type.BOOLEAN;
            } else {
                finalType = getType(str);
            }
            String finalAnswer = variable_value.toString().trim();
            if (finalAnswer.contains("+") ||finalAnswer.contains("-") || finalAnswer.contains("*") || finalAnswer.contains("/") || finalAnswer.contains("%")) {
                if (!finalType.equals(TokenObjects.Type.STRING) && !finalType.equals(TokenObjects.Type.BOOLEAN)) {
                   Answer ans = Evaluator.run(finalAnswer);
                    finalAnswer = String.valueOf(ans.answer);
                }
            }
            String name = variable_name.toString().trim();
            TokenObjects.Value value = new TokenObjects.Value(finalType, finalAnswer);
            variables_temp.put(name, value);
            variables.put(name, value);
            return Optional.ofNullable(variables_temp.entrySet().iterator().next());

        }
        return Optional.empty();
    }

    private static TokenObjects.Type getType(String str) {
        if (str.equals("true") || str.equals("false")) {
            return TokenObjects.Type.BOOLEAN;
        }

        try {
            Integer.parseInt(str);
            return TokenObjects.Type.INT;
        } catch (NumberFormatException ignored) {
        }

        try {
            Float.parseFloat(str);
            return TokenObjects.Type.FLOAT;
        } catch (NumberFormatException ignored) {
        }

        try {
            Double.parseDouble(str);
            return TokenObjects.Type.DOUBLE;
        } catch (NumberFormatException ignored) {
        }

        return TokenObjects.Type.STRING;
    }

    static void print(char[] chars){
        if (startsWith(chars, "out")) {
            int index = 4;
            StringBuilder intOutput = new StringBuilder();
            // Ensure printable strings are in 's or "s
            if (chars[index] != '"' && chars[index] != '\'') {
                int index3 = index;

                // Check if integers are being printed
                boolean isInteger = false;
                if (Character.isDigit(chars[index3])) {
                    while (index3 < chars.length && Character.isDigit(chars[index3])) {
                        intOutput.append(chars[index3]);
                        index3++;
                    }
                    isInteger = true;
                }
                int index2 = index;
                StringBuilder varName = new StringBuilder();
                while (chars[index2] != ';') {
                    varName.append(chars[index2]);
                    index2++;
                }
                String varNameStr = varName.toString().trim();
                boolean isFunction = false;
                for (TokenObjects.Function function : functions) {
                    if (varNameStr.trim().equals(function.name().trim())) {
                        isFunction = true;
                        break;
                    }

                }
                // add is a function though, idk why this gets evaluated to true
                if (!variables.containsKey(varNameStr) && !isInteger && !isFunction) {
                    // not a string, variable, integer, or function
                    if (variables.isEmpty()) System.out.println("empty variable box for some reason  :(");
                    System.out.println("Missing variable: " + varNameStr + ".");
                    for (Map.Entry<String, TokenObjects.Value> entry : variables.entrySet()) {
                        System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
                    }
                    System.out.println(variables);
                    throw new TokenObjects.IncorrectSyntaxException("Expected '\"', ', or variable name at line: " + new String(chars));

                } else if (!isInteger && variables.containsKey(varNameStr) && !isFunction) {
                    // variable, not integer or function
                    System.out.println(variables.get(varNameStr).contents());
                } else if (isInteger && !isFunction && !variables.containsKey(varNameStr)) {
                    // integer, not function or variable
                    System.out.println(intOutput);
                } else {
                    // Function
                    TokenObjects.Function functionToPrint = null;
                    for (TokenObjects.Function function : functions) {
                        if (function.name().equals(varNameStr)) {
                            functionToPrint = function;
                            break;
                        }
                    }
                    if (chars[index + 1] == '(' && new String(chars).contains(")") && functionToPrint != null) {
                        int param_count = functionToPrint.params().size();
                        String str = new String(chars);
                        String sub = str.substring(str.indexOf('(') + 1, str.lastIndexOf(')'));
                        String[] params = sub.split(",");
                        if (params.length != param_count)
                            throw new TokenObjects.IncorrectSyntaxException("Expected " + params.length + " params at line " + new String(chars));
                        HashMap<String, TokenObjects.Value> params_temp = new HashMap<>();

                        for (String param : params) {
                            TokenObjects.Type finalType;
                            finalType = getType(param);
                            params_temp.put(param.trim(), new TokenObjects.Value(finalType, param));  // HERE
                        }

                    }

                    System.out.println(functionToPrint == null ? "" : functionToPrint.contents());
                }

            } else {
                index++;
                StringBuilder currentOut = new StringBuilder();
                while (chars[index] != '\'' && chars[index] != '"') {
                    currentOut.append(chars[index]);
                    index++;
                }
                System.out.println(currentOut);
            }
        }
    }

    static boolean startsWith(char[] chars, String keyword) {
        if (chars.length < keyword.length()) return false;
        for (int i = 0; i < keyword.length(); i++) {
            if (chars[i] != keyword.charAt(i)) return false;
        }
        return true;
    }
    static HashMap<String, TokenObjects.Value> vars_temp = new HashMap<>();
    static void runLines(char[] line, HashMap<String, TokenObjects.Value> variables) throws Exception {
        String str = new String(line);
        for (Map.Entry<String, TokenObjects.Value> entry : variables.entrySet()) {
            if (str.contains(entry.getKey())) {
                str = str.replace(entry.getKey(), entry.getValue().contents());
            }
        }
        if (str.startsWith("out")) {
            print(line);
        }
        if (str.startsWith("set")) {
            Optional<Map.Entry<String, TokenObjects.Value>> vars = getVariables(line);
            vars.ifPresent(stringValueEntry -> vars_temp.put(stringValueEntry.getKey(), stringValueEntry.getValue()));
        }

    }
    static boolean is(String expr)  {


        try (Context context = Context.create("js")) {
            Value result = context.eval("js", expr);

            if (result.isBoolean()) {
                return result.asBoolean();
            } else {
                throw new TokenObjects.IncorrectSyntaxException("Must be boolean expression");
            }
        }
    }

}