package wordy.parser;

import org.junit.jupiter.api.Test;

import wordy.ast.ASTNode;
import wordy.ast.AssignmentNode;
import wordy.ast.BinaryExpressionNode;
import wordy.ast.BlockNode;
import wordy.ast.ConditionalNode;
import wordy.ast.ConstantNode;
import wordy.ast.RecordRowNode;
import wordy.ast.FunctionCallNode;
import wordy.ast.FunctionDeclarationNode;
import wordy.ast.FunctionReturnNode;
import wordy.ast.LoopExitNode;
import wordy.ast.LoopNode;
import wordy.ast.RecordNode;
import wordy.ast.VariableNode;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static wordy.parser.WordyParser.parseExpression;
import static wordy.parser.WordyParser.parseProgram;
import static wordy.parser.WordyParser.parseStatement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordyParserTest {

    @Test
    void testNumbers() {
        assertEquals(new ConstantNode(37), parseExpression("37"));
        assertEquals(new ConstantNode(37.2), parseExpression("37.2"));
        assertEquals(new ConstantNode(-37.2), parseExpression("-37.2"));
        assertParseError(() -> parseExpression("-37..2"));
    }

    @Test
    void testOperators() {
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.ADDITION,
                new ConstantNode(1),
                new ConstantNode(2)),
            parseExpression("1 plus 2"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.SUBTRACTION,
                new ConstantNode(1),
                new ConstantNode(2)),
            parseExpression("1 minus 2"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.MULTIPLICATION,
                new ConstantNode(1),
                new ConstantNode(2)),
            parseExpression("1 times 2"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.DIVISION,
                new ConstantNode(1),
                new ConstantNode(2)),
            parseExpression("1 divided by 2"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.DIVISION,
                new ConstantNode(1),
                new ConstantNode(2)),
            parseExpression("1\ndivided    by\n 2"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.EXPONENTIATION,
                new ConstantNode(2),
                new ConstantNode(8)),
            parseExpression("2 to the power of 8"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.EXPONENTIATION,
                new ConstantNode(16),
                new ConstantNode(2)),
            parseExpression("16 squared"));
    }

    @Test
    void testPrecedence() {
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.ADDITION,
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.SUBTRACTION,
                    new ConstantNode(1),
                    new ConstantNode(2)),
                new ConstantNode(3)),
            parseExpression("1 minus 2 plus 3"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.SUBTRACTION,
                new ConstantNode(1),
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.DIVISION,
                    new ConstantNode(2),
                    new ConstantNode(3))),
            parseExpression("1 minus 2 divided by 3"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.DIVISION,
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.SUBTRACTION,
                    new ConstantNode(1),
                    new ConstantNode(2)),
                new ConstantNode(3)),
            parseExpression("(1 minus 2) divided by 3"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.EXPONENTIATION,
                new VariableNode("x"),
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.EXPONENTIATION,
                    new VariableNode("y"),
                    new VariableNode("z"))),
            parseExpression("x to the power of y to the power of z"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.EXPONENTIATION,
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.EXPONENTIATION,
                    new VariableNode("x"),
                    new ConstantNode(2)),
                new ConstantNode(2)),
            parseExpression("x squared squared"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.EXPONENTIATION,
                new VariableNode("y"),
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.EXPONENTIATION,
                    new VariableNode("z"),
                    new ConstantNode(2.0))),
            parseExpression("y to the power of z squared"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.SUBTRACTION,
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.ADDITION,
                    new BinaryExpressionNode(
                        BinaryExpressionNode.Operator.EXPONENTIATION,
                        new VariableNode("x"),
                        new ConstantNode(2.0)),
                    new BinaryExpressionNode(
                        BinaryExpressionNode.Operator.MULTIPLICATION,
                        new ConstantNode(2.0),
                        new BinaryExpressionNode(
                            BinaryExpressionNode.Operator.EXPONENTIATION,
                            new VariableNode("y"),
                            new BinaryExpressionNode(
                                BinaryExpressionNode.Operator.EXPONENTIATION,
                                new VariableNode("z"),
                                new ConstantNode(2.0))))),
                new ConstantNode(1.0)),
            parseExpression("x squared plus 2 times y to the power of z squared minus 1"));
    }

    @Test
    void testVariables() {
        assertEquals(new VariableNode("x"), parseExpression("x"));
        assertEquals(new VariableNode("snake_and_camelcase_ok"), parseExpression("snake_and_camelCase_OK"));
        assertEquals(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.ADDITION,
                new VariableNode("x"),
                new VariableNode("y")),
            parseExpression("x plus y"));
    }

    @Test
    void testAssignmentStatement() {
        assertEquals(
            new AssignmentNode(
                new VariableNode("sprongle"),
                new ConstantNode(-3)),
            parseStatement("set sprongle to -3"));
        assertEquals(
            new AssignmentNode(
                new VariableNode("sprongle"),
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.ADDITION,
                    new VariableNode("zoink"),
                    new ConstantNode(7))),
            parseStatement("set sprongle to zoink plus 7"));
    }

    @Test
    void testConditional() {
        assertEquals(
            new ConditionalNode(
                ConditionalNode.Operator.EQUALS,
                new VariableNode("x"),
                new ConstantNode(2),
                parseStatement("set y to 1"),
                parseStatement("set z to 3")),
            parseStatement("if x is equal to 2 then set y to 1 else set z to 3"));
        assertEquals(
            new ConditionalNode(
                ConditionalNode.Operator.EQUALS,
                new VariableNode("x"),
                new ConstantNode(2),
                parseStatement("set y to 1"),
                BlockNode.EMPTY),
            parseStatement("if x equals 2 then set y to 1"));
        assertEquals(
            new ConditionalNode(
                ConditionalNode.Operator.EQUALS,
                new VariableNode("x"),
                new ConstantNode(2),
                new BlockNode(
                    parseStatement("set y to 1"),
                    parseStatement("set z to 3")),
                new BlockNode(
                    parseStatement("set a to 1"),
                    parseStatement("set b to 3"))),
            parseStatement("if x equals 2 then:set y to 1.set z to 3.else:set a to 1.set b to 3.end of conditional"));
        assertEquals(
            new ConditionalNode(
                ConditionalNode.Operator.EQUALS,
                new VariableNode("x"),
                new ConstantNode(2),
                new BlockNode(
                    parseStatement("set y to 1"),
                    parseStatement("set z to 3")),
                BlockNode.EMPTY),
            parseStatement("if x is equal to 2 then  :  \nset y to 1.\nset z to 3.\n\nend  of conditional"));
    }

    @Test
    void testLoop() {
        assertEquals(
            new LoopNode(
                new BlockNode(
                    parseStatement("set x to x plus 1"),
                    new ConditionalNode(
                        ConditionalNode.Operator.EQUALS,
                        new VariableNode("x"),
                        new ConstantNode(3),
                        new LoopExitNode(),
                        BlockNode.EMPTY))),
            parseStatement("loop: set x to x plus 1. if x equals 3 then exit loop. end of loop"));
    }

    @Test
    void testFunctionDeclaration() {
        assertEquals(
            new FunctionDeclarationNode(
                new FunctionReturnNode()),
            parseExpression("function of () in: return"));
        assertEquals(
            new FunctionDeclarationNode(
                new FunctionReturnNode(
                    new BinaryExpressionNode(
                        BinaryExpressionNode.Operator.MULTIPLICATION,
                        new VariableNode("a"),
                        new ConstantNode(10))),
                new VariableNode("a")),
            parseExpression("function of (a) in: return a times 10"));
        assertEquals(
            new FunctionDeclarationNode(
                new FunctionReturnNode(
                    new BinaryExpressionNode(
                        BinaryExpressionNode.Operator.DIVISION,
                        new BinaryExpressionNode(
                            BinaryExpressionNode.Operator.ADDITION,
                            new VariableNode("a"),
                            new VariableNode("b")),
                        new ConstantNode(2))),
                new VariableNode("a"),
                new VariableNode("b")),
            parseExpression("function of (a, b) in: return (a plus b) divided by 2"));
    }

    @Test
    void testFunctionCall() {
        assertEquals(
            new FunctionCallNode(
                new VariableNode("println")),
            parseStatement("println executed"));
        assertEquals(
            new FunctionCallNode(
                new VariableNode("area"),
                new VariableNode("a"),
                new VariableNode("b")),
            parseStatement("area of (a, b) executed"));
        assertEquals(
            new FunctionCallNode(
                new VariableNode("area"),
                new ConstantNode(10),
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.ADDITION,
                    new ConstantNode(5),
                    new VariableNode("a"))),
            parseStatement("area of (10, 5 plus a) executed"));
    }

    @Test
    void testFunctionReturn() {
        assertEquals(new FunctionReturnNode(),
            parseStatement("return"));
        assertEquals(new FunctionReturnNode(
            new BinaryExpressionNode(
                BinaryExpressionNode.Operator.DIVISION,
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.ADDITION,
                    new VariableNode("a"),
                    new VariableNode("b")),
                new ConstantNode(2))),
            parseStatement("return (a plus b) divided by 2"));
        assertEquals(new FunctionReturnNode(
            new FunctionCallNode(
                new VariableNode("func"))),
            parseStatement("return func executed"));
        assertEquals(new FunctionReturnNode(
            new FunctionCallNode(
                new VariableNode("func"),
                List.of(new VariableNode("a"),
                    new ConstantNode(3),
                    new BinaryExpressionNode(
                        BinaryExpressionNode.Operator.DIVISION,
                        new ConstantNode(10),
                        new ConstantNode(2))))),
            parseStatement("return func of (a, 3, 10 divided by 2) executed"));
    }

    @Test
    void testRecordRow() {
        assertEquals(
            new RecordRowNode(
                new VariableNode("sprongle"),
                new ConstantNode(-3)),
            parseStatement("sprongle is -3"));
        assertEquals(
            new RecordRowNode(
                new VariableNode("sprongle"),
                new BinaryExpressionNode(
                    BinaryExpressionNode.Operator.ADDITION,
                    new VariableNode("zoink"),
                    new ConstantNode(7))),
            parseStatement("sprongle is zoink plus 7"));
    }

    @Test
    void testRecord() {
        assertEquals(
            new RecordNode(
                new RecordRowNode(
                    new VariableNode("a"),
                    new ConstantNode(3))),
            parseExpression("record where: (a is 3)"));
        assertEquals(
            new RecordNode(
                new RecordRowNode(
                    new VariableNode("a"),
                    new ConstantNode(3)),
                new RecordRowNode(
                    new VariableNode("b"),
                    new BinaryExpressionNode(
                        BinaryExpressionNode.Operator.MULTIPLICATION,
                        new ConstantNode(4),
                        new ConstantNode(2)))),
            parseExpression("record where: (a is 3, b is 4 times 2)"));
        assertEquals(
            new RecordNode(
                new RecordRowNode(
                    new VariableNode("a"),
                    new FunctionCallNode(
                        new VariableNode("x"),
                        new ConstantNode(2))),
                new RecordRowNode(
                    new VariableNode("c"),
                    new FunctionDeclarationNode(
                        new FunctionReturnNode(
                            new VariableNode("x")),
                        new VariableNode("x")))),
            parseExpression("record where: (a is x of (2) executed, c is function of (x) in: return x)"));
        assertEquals(
            new RecordNode(
                new RecordRowNode(
                    new VariableNode("x"),
                    new RecordNode(
                        new RecordRowNode(
                            new VariableNode("y"),
                            new ConstantNode(3)),
                        new RecordRowNode(
                            new VariableNode("z"),
                            new ConstantNode(4)))),
                new RecordRowNode(
                    new VariableNode("a"),
                    new ConstantNode(3))),
            parseExpression("record where: (x is record where : (y is 3, z is 4), a is 3)"));
    }

    @Test
    void testProgram() {
        assertParseError(() -> parseProgram(""));
        assertEquals(
            new BlockNode(
                new AssignmentNode(
                    new VariableNode("x"),
                    new ConstantNode(1))),
            parseProgram("set x to 1."));

        assertEquals(
            new BlockNode(
                new AssignmentNode(
                    new VariableNode("x"),
                    new ConstantNode(1)),
                new AssignmentNode(
                    new VariableNode("y"),
                    new ConstantNode(2))),
            parseProgram("set x to 1. set y to 2."));
    }

    @Test
    void testWhitespaceHandling() {
        assertEquivalentParsing(
            "set x to 1. set y to 2.",
            "set x to 1.set y to 2.",
            "\n\n    set \tx  to \n 1      \n. set \n y \n to  \n 2 \n  .  \t ");
        assertEquivalentParsing(
            "loop: if x is greater than 2 then: exit loop. else: set x to x squared. end of conditional. end of loop.",
            "loop:if x is greater than 2 then:exit loop.else:set x to x squared.end of conditional.end of loop.",
            "  loop  :  if  x  is  greater  than  2  then  :  exit  loop  .  else  :  set  x  to  x  squared  .  "
                + "   end  of  conditional  .  end  of  loop  .  ");
        assertEquivalentParsing(
            "set x to (x squared) plus (y to the power of (3 plus (z squared))).",
            "set x to  (  x  squared  )  plus  (  y  to  the  power  of  (  3  plus  (  z squared  )  )  )  .",
            "set x to(x squared)plus(y to the power of(3 plus(z squared))).");
        assertEquivalentParsing(
            "set x to function of (a, b) in: return y of (a, b, 2 plus 2) executed.",
            "set  x  to  function  of  ( a , b  )  in  :  return   y  of ( a ,  b , 2  plus  2  ) executed.",
            "set x to functionof(a,b)in:return y of(a,b,2plus2)executed.");
    }

    private void assertEquivalentParsing(String... variants) {
        ASTNode expected = parseProgram(variants[0]);
        for (var variant : variants) {
            try {
                assertEquals(expected, parseProgram(variant));
            } catch (RuntimeException e) {
                System.err.println("Failure while comparing parsing of:"
                    + "\n    " + variants[0]
                    + "\n    " + variant);
                throw e;
            }
        }
    }

    private void assertParseError(Runnable parseAction) {
        assertThrows(ParseException.class, parseAction::run);
    }
}
