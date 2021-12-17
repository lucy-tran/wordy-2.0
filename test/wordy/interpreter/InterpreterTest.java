package wordy.interpreter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static wordy.parser.WordyParser.parseExpression;
import static wordy.parser.WordyParser.parseProgram;
import static wordy.parser.WordyParser.parseStatement;

import wordy.ast.values.WordyDouble;
import wordy.ast.values.WordyValue;

public class InterpreterTest {
    private final EvaluationContext context = new EvaluationContext();

    @Test
    void evaluateConstant() {
        WordyDouble a = new WordyDouble(2001);
        assertEvaluationEquals(a, "2001");
        WordyDouble b = new WordyDouble(-3.5);
        assertEvaluationEquals(b, "-3.5");
    }

    @Test
    void evaluateVariable() {
        WordyDouble a = new WordyDouble(54);
        WordyDouble b = new WordyDouble(42);
        context.set("question", a);
        context.set("answer", b);
        assertEvaluationEquals(a, "question");
        assertEvaluationEquals(b, "answer");
        assertEvaluationEquals(null, "fish");
    }

    @Test
    void evaluateBinaryExpression() {
        assertEvaluationEquals(new WordyDouble(5), "2 plus 3");
        assertEvaluationEquals(new WordyDouble(-1), "2 minus 3");
        assertEvaluationEquals(new WordyDouble(6), "2 times 3");
        double a = 2.0 / 3;
        assertEvaluationEquals(new WordyDouble(a), "2 divided by 3");
        assertEvaluationEquals(new WordyDouble(4), "2 squared");
        assertEvaluationEquals(new WordyDouble(8), "2 to the power of 3");
        assertEvaluationEquals(new WordyDouble(511.5), "2 to the power of 3 squared minus 1 divided by 2");
    }

    @Test
    void executeAssignment() {
        runStatement("set x to 17");
        assertVariableEquals("x", new WordyDouble(17));
        runStatement("set y to x squared");
        assertVariableEquals("y", new WordyDouble(289));
    }

    @Test
    void executeBlock() {
        runProgram("set x to 17. set y to x squared.");
        assertVariableEquals("x", new WordyDouble(17));
        assertVariableEquals("y", new WordyDouble(289));
    }

    @Test
    void executeConditional() {
        String program = "if x is less than 12 then set lt to x else set lt to lt minus 1."
            + "if x equals 12 then set eq to x else set eq to eq minus 1."
            + "if x is greater than 12 then set gt to x else set gt to gt minus 1.";

        context.set("x", new WordyDouble(11));
        runProgram(program);
        assertVariableEquals("lt", new WordyDouble(11));
        assertVariableEquals("eq", new WordyDouble(-1));
        assertVariableEquals("gt", new WordyDouble(-1));

        context.set("x", new WordyDouble(12));
        runProgram(program);
        assertVariableEquals("lt", new WordyDouble(10));
        assertVariableEquals("eq", new WordyDouble(12));
        assertVariableEquals("gt", new WordyDouble(-2));

        context.set("x", new WordyDouble(13));
        runProgram(program);
        assertVariableEquals("lt", new WordyDouble(9));
        assertVariableEquals("eq", new WordyDouble(11));
        assertVariableEquals("gt", new WordyDouble(13));
    }

    @Test
    void executeLoopExit() {
        assertThrows(LoopExited.class, () -> runStatement("exit loop"));
    }

    @Test
    void executeLoop() {
        runProgram("loop: set x to x plus 1. if x equals 10 then exit loop. set y to y plus x squared. end of loop.");
        assertVariableEquals("x", new WordyDouble(10));
        assertVariableEquals("y", new WordyDouble(285));
    }

    // ––––––– Helpers –––––––

    private void assertEvaluationEquals(WordyValue expected, String expression) {
        assertEquals(expected, parseExpression(expression).evaluate(context));
    }

    private void runStatement(String statement) {
        parseStatement(statement).run(context);
    }

    private void runProgram(String program) {
        parseProgram(program).run(context);
    }

    private void assertVariableEquals(String name, WordyValue expectedValue) {
        assertEquals(expectedValue, context.get(name));
    }
}
