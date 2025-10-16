package app.belgarion.java;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadAndTokenize {
    static Response tokenize(File file) throws IOException {
        ArrayList<Token> tokens = new ArrayList<>();
        ArrayList<Function> functions = new ArrayList<>();
        HashMap<String, Value> variables = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("//")) {
                    line = line.substring(0, line.indexOf("//")-1);

                }
                line = line.replace(" ", "");
                char[] chars = line.toCharArray();
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
                // FUNCTION CREATION
                {
                if (chars.length > 3 && chars[0] == 'f' && chars[1] == 'u' && chars[2] == 'n') {
                    // get name
                    int index = 4;
                    StringBuilder function_name = new StringBuilder();
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

                }
                }
            }
        }

        return new Response(tokens, variables);
    }
}
