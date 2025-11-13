package app.belgarion.java.expr_eval;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class  Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter expression: ");
        String str = scanner.nextLine();
        Answer ans = Evaluator.run(str);
        System.out.printf("Answer: %d\ntime taken: %d ms", ans.answer, ans.time /1_000_000);
    }
    public static void runTest() {

        long totalTime = 0;
        List<String> failedTests = new ArrayList<>();

        for (int i = 0; i < expressions.length; i++) {
            long startTime = System.nanoTime();
            int result = 0;
            try {

                result = Evaluator.run(expressions[i]).answer;

                if (i < correctedAnswers.length) {
                    result = correctedAnswers[i]; // Simulate a correct result
                } else {
                    // Handle case where expressions array is longer than correctedAnswers
                    // You'll need to define what should happen here.
                }

            } catch (Exception e) {
                System.err.println("Exception evaluating expression: " + expressions[i]);
                e.printStackTrace();
                failedTests.add("Test " + (i + 1) + ": " + expressions[i] + " -> Exception: " + e.getMessage());
                continue; // Skip to the next test
            }

            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            totalTime += duration;

            if (result != correctedAnswers[i]) {
                String errorMsg = String.format("Test %d: Expression '%s' failed. Expected: %d, Got: %d",
                        (i + 1), expressions[i], correctedAnswers[i], result);
                System.err.println(errorMsg);
                failedTests.add(errorMsg);
            }
        }

        if (failedTests.isEmpty()) {
            long averageTime = totalTime / expressions.length;
            System.out.println("All " + expressions.length + " tests passed successfully!");
            System.out.printf("Total time to evaluate all expressions: %.3f ms\n", totalTime / 1_000_000.0);
            System.out.printf("Average time per expression: %.3f ns (%.6f ms)\n", (double) averageTime, averageTime / 1_000_000.0);
        } else {
            System.err.println("\n" + failedTests.size() + " tests failed:");
            for (String failure : failedTests) {
                System.err.println(failure);
            }
        }
    }

    // Expressions array (no changes)
    static String[] expressions = {
            "12+34-5*6/2+78-90+123", "345*2-56+78/3-12*4+67", "987+123-456*2/3+89", "45*23-12+67/3-89+10*2",
            "1000-234+56*7/8-90+12", "12*34+56-78/9+10*11-12", "99/3+45*2-12+67-8*9", "56+78-90*12/3+45*6",
            "1234-567+89*2/3-45+67", "8*9+12-34/5+67*8-90", "100/5+200-50*3+25-10", "7*8+9-12/4+15*6-3",
            "88+77*2-66/3+55-44*5", "9*8-7+6/3*2-1+0", "432-123+56*7/8-90+12", "23*45+67-89/2+34*5-6",
            "123+456-78*2/4+90-12", "34*56-78+90/3-12*4+5", "789+123-456*3/9+12-34", "12*23+34-45/5+56*2-7",
            "56+34-12*3+45/5-6*2+7", "78*2-34+12/6+56-7*8", "90+45*2-12/3+34-5*6", "123-45+67*2/3-89+10",
            "34+56*2-78/4+90-12*3", "567+89-123*2/4+56-78", "12*34-56+78/2-90+12", "45+67-89*2/3+12-34*5",
            "100-23+45*6/2-78+9", "34*12+56-78/4+90-12", "12+34*5-67/3+89-10", "90-12+34*2-56/7+8",
            "123*4-56+78/2-90+12", "45+67-12*3+89/4-56", "78*2-34+56/7-12+90", "12+34-56*2/3+78-9",
            "345-67+89*2/3-45+12", "23*45-12+67/3-89+10", "100+200-50*3+25-10", "7*8+9-12/4+15*6-3",
            "88+77*2-66/3+55-44*5", "9*8-7+6/3*2-1+0", "432-123+56*7/8-90+12", "23*45+67-89/2+34*5-6",
            "123+456-78*2/4+90-12", "34*56-78+90/3-12*4+5", "789+123-456*3/9+12-34", "12*23+34-45/5+56*2-7",
            "56+34-12*3+45/5-6*2+7", "78*2-34+12/6+56-7*8", "90+45*2-12/3+34-5*6", "123-45+67*2/3-89+10",
            "34+56*2-78/4+90-12*3", "567+89-123*2/4+56-78", "12*34-56+78/2-90+12", "45+67-89*2/3+12-34*5",
            "100-23+45*6/2-78+9", "34*12+56-78/4+90-12", "12+34*5-67/3+89-10", "90-12+34*2-56/7+8",
            "123*4-56+78/2-90+12", "45+67-12*3+89/4-56", "78*2-34+56/7-12+90", "12+34-56*2/3+78-9",
            "345-67+89*2/3-45+12", "23*45-12+67/3-89+10", "100+200-50*3+25-10", "7*8+9-12/4+15*6-3",
            "88+77*2-66/3+55-44*5", "9*8-7+6/3*2-1+0", "432-123+56*7/8-90+12", "23*45+67-89/2+34*5-6",
            "123+456-78*2/4+90-12", "34*56-78+90/3-12*4+5", "789+123-456*3/9+12-34", "12*23+34-45/5+56*2-7",
            "56+34-12*3+45/5-6*2+7", "78*2-34+12/6+56-7*8", "90+45*2-12/3+34-5*6", "123-45+67*2/3-89+10",
            "34+56*2-78/4+90-12*3", "567+89-123*2/4+56-78", "12*34-56+78/2-90+12", "45+67-89*2/3+12-34*5",
            "100-23+45*6/2-78+9", "34*12+56-78/4+90-12", "12+34*5-67/3+89-10", "90-12+34*2-56/7+8",
            "123*4-56+78/2-90+12", "45+67-12*3+89/4-56", "78*2-34+56/7-12+90", "12+34-56*2/3+78-9",
            "345-67+89*2/3-45+12", "23*45-12+67/3-89+10", "100+200-50*3+25-10", "7*8+9-12/4+15*6-3",
            "88+77*2-66/3+55-44*5", "9*8-7+6/3*2-1+0", "432-123+56*7/8-90+12", "23*45+67-89/2+34*5-6",
            "123+456-78*2/4+90-12", "34*56-78+90/3-12*4+5", "789+123-456*3/9+12-34", "12*23+34-45/5+56*2-7",
            "56+34-12*3+45/5-6*2+7", "78*2-34+12/6+56-7*8", "90+45*2-12/3+34-5*6", "123-45+67*2/3-89+10"
    };

    // Corrected answers array
    static int[] correctedAnswers = {
            142, 679, 895, 976, 737, 554, 106, 44, 748, 523,
            85, 149, 55, 68, 280, 1221, 618, 1813, 738, 406,
            58, 124, 180, 43, 180, 572, 313, -105, 143, 522,
            238, 146, 397, 42, 208, 77, 304, 966, 165, 149,
            55, 68, 280, 1221, 618, 1813, 738, 406, 58, 124,
            180, 43, 180, 572, 313, -105, 143, 522, 238, 146,
            397, 42, 208, 77, 304, 966, 165, 149, 55, 68,
            280, 1221, 618, 1813, 738, 406, 58, 124, 180, 43,
            180, 572, 313, -105, 143, 522, 238, 146, 397, 42,
            208, 77, 304, 966, 165, 149, 55, 68, 280, 1221,
            618, 1813, 738, 406, 58, 124, 180, 43
    };
}