package app.belgarion.java;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ReadAndTokenize {
    /**
     *
     * @param file the file to run
     * @return Response {@inheritDoc} the Response
     * @throws IOException {@inheritDoc} if something broke
     */
    static Response tokenize(File file) throws IOException {
        ArrayList<Token> tokens = new ArrayList<>();
        ArrayList<Function> functions = new ArrayList<>();
        HashMap<String, Value> variables = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean inFunction = false;
            StringBuilder functionContents  = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.contains("//")) {
                    line = line.substring(0, line.indexOf("//")-1);

                }

                line = line.replace(" ", "").replace("}", "\n}");


                char[] chars = line.toCharArray();
                if (chars.length == 0) {
                    continue;
                }

                if (inFunction && startsWith(chars, "fun")) throw new IncorrectSyntaxException("functions may not be declared within function body");
                if (inFunction) {functionContents.append(line).append("\n");}
                // VARIABLES
                {

                    if (chars[0] == 's' && chars[1] == 'e' && chars[2] == 't') {
                        int index = 4;
                        StringBuilder variable_name = new StringBuilder();
                        while (chars[index] != '=') {
                            variable_name.append(chars[index]);
                            index++;
                        }
                        // Currently on '='

                        index++;
                        StringBuilder variable_value = new StringBuilder();
                        while (chars[index] != ';') {
                            variable_value.append(chars[index]);
                            index++;
                        }
                        // now on semicolon
                        Type finalType;

                        String str = variable_value.toString();

                        if (str.equals("true") || str.equals("false")) {
                            finalType = Type.BOOLEAN;
                        } else {
                            try {
                                Integer.parseInt(str);
                                finalType = Type.INT;
                            } catch (NumberFormatException e1) {
                                try {
                                    Float.parseFloat(str);
                                    finalType = Type.FLOAT;
                                } catch (NumberFormatException e2) {
                                    try {
                                        Double.parseDouble(str);
                                        finalType = Type.DOUBLE;
                                    } catch (NumberFormatException e3) {
                                        finalType = Type.STRING;
                                    }
                                }
                            }
                        }
                        variables.put(variable_name.toString(), new Value(finalType, variable_value.toString()));
                    }
                }
                // FUNCTION CREATION - HEADER
                StringBuilder function_name = new StringBuilder();
                ArrayList<String> params2 = new ArrayList<>();
                if (startsWith(chars, "fun")) {
                    // get name
                    int index = 4;
                    // amount of curly braces in function body
                    int amount = 0;
                    while (chars[index] != '(') {
                        function_name.append(chars[index]);
                        index++;
                    }

                    // At '('
                    // Parameters
                    index++;
                    int index2=0;
                    ArrayList<StringBuilder> params = new ArrayList<>();
                    params.add(new StringBuilder());
                    params.add(new StringBuilder());
                    params.add(new StringBuilder());
                    params.add(new StringBuilder());
                    while (chars[index] != ')') {
                        if (chars[index] == ',') {
                            index++;
                            index2++;
                            continue;
                        }
                        params.get(index2).append(chars[index]);
                        index++;
                    }
                    // TEMPORARY LOGGING
                    for (StringBuilder param : params) {
                        System.out.println("parameter: "+param.toString());
                    }
                    for (StringBuilder param : params) {
                        params2.add(param.toString());
                    }
                    index++;
                    // now at '{' hopefully
                    if (chars[index] != '{') {throw new IncorrectSyntaxException("Expected '{' after function definition");}
                    inFunction = true;

                }

                // FUNCTION CREATION - BODY
                if (inFunction) {
                    if (Arrays.equals(chars, new char[]{'}'})) {
                        functions.add(new Function(function_name.toString(), params2, functionContents.toString()));
                        inFunction = false;
                    }
                }
            }
        }

        return new Response(tokens, variables);
    }
    static boolean startsWith(char[] chars, String keyword) {
        if (chars.length < keyword.length()) return false;
        for (int i = 0; i < keyword.length(); i++) {
            if (chars[i] != keyword.charAt(i)) return false;
        }
        return true;
    }

}
