import java.util.Scanner;

public class AlgebraBooleana {

    private static final int TRUE = 1;
    private static final int FALSE = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try {
                int[] inputs = parseInputs(line);
                String expression = parseExpression(line);
                int result = evaluateExpression(expression, inputs);
                System.out.println(result);
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static int[] parseInputs(String line) {
        String[] parts = line.split(" ");
        int n = Integer.parseInt(parts[0]);
        int[] inputs = new int[n];
        for (int i = 0; i < n; i++) {
            inputs[i] = Integer.parseInt(parts[i + 1]);
        }
        return inputs;
    }

    private static String parseExpression(String line) {
        int n = Integer.parseInt(line.split(" ")[0]);
        return line.substring(n * 2 + 2).trim(); 
    }

    public static int evaluateExpression(String expression, int[] inputs) {
        for (int i = 0; i < inputs.length; i++) {
            char variable = (char) ('A' + i);
            expression = expression.replaceAll("\\b" + variable + "\\b", Integer.toString(inputs[i]));
        }
        return evaluate(expression);
    }

    private static int evaluate(String expression) {
        expression = expression.replaceAll("\\s+", ""); 
        return evaluateExpressionRecursive(expression);
    }

    private static int evaluateExpressionRecursive(String expression) {
        if (expression.startsWith("not(")) {
            int subExprValue = evaluateExpressionRecursive(expression.substring(4, expression.length() - 1));
            return subExprValue == TRUE ? FALSE : TRUE;
        }

        if (expression.startsWith("and(")) {
            String[] operands = splitOperands(expression.substring(4, expression.length() - 1));
            for (String operand : operands) {
                if (evaluateExpressionRecursive(operand) == FALSE) {
                    return FALSE;
                }
            }
            return TRUE;
        }

        if (expression.startsWith("or(")) {
            String[] operands = splitOperands(expression.substring(3, expression.length() - 1));
            for (String operand : operands) {
                if (evaluateExpressionRecursive(operand) == TRUE) {
                    return TRUE;
                }
            }
            return FALSE;
        }

        if (expression.matches("\\d")) {
            return Integer.parseInt(expression);
        }

        throw new IllegalArgumentException("Expressão malformada ou não reconhecida: " + expression);
    }

    private static String[] splitOperands(String expression) {
        int openParentheses = 0;
        StringBuilder currentOperand = new StringBuilder();
        java.util.List<String> operands = new java.util.ArrayList<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '(') {
                openParentheses++;
            } else if (c == ')') {
                openParentheses--;
            } else if (c == ',' && openParentheses == 0) {
                operands.add(currentOperand.toString().trim());
                currentOperand.setLength(0); 
                continue;
            }
            currentOperand.append(c);
        }
        operands.add(currentOperand.toString().trim()); 
        return operands.toArray(new String[0]);
    }
}
