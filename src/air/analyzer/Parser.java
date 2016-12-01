package air.analyzer;

import air.syntax.tree.expression.*;
import air.syntax.tree.statement.*;
import air.util.Token;
import air.util.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static final Token END = new Token(TokenType.END, "");

    private final List<Token> list;
    private final int size;

    private int currentPosition;

    public Parser(List<Token> list) {
        this.list = list;
        size = list.size();
    }

    public Statement parse() {
        ProgramBlockStatement statement = new ProgramBlockStatement();
        while (!isMatches(TokenType.END)) {
            statement.add(someStatement());
        }
        return statement;
    }

    private Statement programBlock() {
        ProgramBlockStatement block = new ProgramBlockStatement();
        consume(TokenType.BLOCK_BEGIN);
        while (!isMatches(TokenType.BLOCK_END)) {
            block.add(someStatement());
        }
        return block;
    }

    private Statement someStatementOrBlock() {
        if (getCurrentToken(0).getType() == TokenType.BLOCK_BEGIN) return programBlock();
        return someStatement();
    }

    private Statement someStatement() {
        if (isMatches(TokenType.PRINT)) {
            return new PrintStatement(someExpression());
        }
        if (isMatches(TokenType.PRINTLN)) {
            return new PrintlnStatement(someExpression());
        }
        if (isMatches(TokenType.IF)) {
            return conditionalStatement();
        }
        if (isMatches(TokenType.WHILE)) {
            return whileCycleStatement();
        }
        if (isMatches(TokenType.EXPLOIT)) {
            return new ExploitStatement(someExpression());
        }
        if (isMatches(TokenType.FOR)) {
            return forCycleStatement();
        }
        if (isMatches(TokenType.REPEAT)) {
            return repeatUntilCycleStatement();
        }
        if (isMatches(TokenType.BREAK)) {
            return new BreakStatement();
        }
        if (isMatches(TokenType.CONTINUE)) {
            return new ContinueStatement();
        }
        if (isMatches(TokenType.RETURN)) {
            return new ReturnStatement(someExpression());
        }
        if (isMatches(TokenType.DEF)) {
            return userFunction();
        }
        if (getCurrentToken(0).getType() == TokenType.WORD && getCurrentToken(1).getType() == TokenType.LEFT_BRACKET) {
            return new FunctionStatement(nativeFunction());
        }
        return assignmentStatement();
    }

    private Statement assignmentStatement() {
        if (isNextMatches(0, TokenType.WORD) && isNextMatches(1, TokenType.EQUAL)) {
            String variable = consume(TokenType.WORD).getText();
            consume(TokenType.EQUAL);
            return new AssignmentStatement(variable, someExpression());
        }
        if (isNextMatches(0, TokenType.WORD) && isNextMatches(1, TokenType.L_ARRAY_BRACKET)) {
            ArrayAccessExpression array = someElement();
            consume(TokenType.EQUAL);
            return new ArrayAssignmentStatement(array, someExpression());
        }
        throw new RuntimeException("Unknown condition");
    }

    private Statement conditionalStatement() {
        Expression expression = someExpression();
        Statement ifStatement = someStatementOrBlock(), elseStatement;
        if (isMatches(TokenType.ELSE)) elseStatement = someStatementOrBlock();
        else elseStatement = null;
        return new IfStatement(expression, ifStatement, elseStatement);
    }

    private Statement whileCycleStatement() {
        Expression expression = someExpression();
        Statement statement = someStatementOrBlock();
        return new WhileCycleStatement(expression, statement);
    }

    private Statement forCycleStatement() {
        Statement initialization = assignmentStatement();
        consume(TokenType.SEMICOLON);
        Expression verification = someExpression();
        consume(TokenType.SEMICOLON);
        Statement counterChange = assignmentStatement(), statement = someStatementOrBlock();
        return new ForCycleStatement(initialization, verification, counterChange, statement);
    }

    private Statement repeatUntilCycleStatement() {
        Statement statement = someStatementOrBlock();
        consume(TokenType.UNTIL);
        Expression expression = someExpression();
        return new RepeatUntilCycleStatement(expression, statement);
    }

    private UserFunctionStatement userFunction() {
        String title = consume(TokenType.WORD).getText();
        consume(TokenType.LEFT_BRACKET);
        List<String> argumentTitle = new ArrayList<>();
        while (!isMatches(TokenType.RIGHT_BRACKET)) {
            argumentTitle.add(consume(TokenType.WORD).getText());
            isMatches(TokenType.COMMA);
        }
        Statement body = someStatementOrBlock();
        return new UserFunctionStatement(title, argumentTitle, body);
    }

    private FunctionExpression nativeFunction() {
        String title = consume(TokenType.WORD).getText();
        consume(TokenType.LEFT_BRACKET);
        FunctionExpression function = new FunctionExpression(title);
        while (!isMatches(TokenType.RIGHT_BRACKET)) {
            function.addArgument(someExpression());
            isMatches(TokenType.COMMA);
        }
        return function;
    }

    private ArrayAccessExpression someElement() {
        String variable = consume(TokenType.WORD).getText();
        List<Expression> list = new ArrayList<>();
        do {
            consume(TokenType.L_ARRAY_BRACKET);
            list.add(someExpression());
            consume(TokenType.R_ARRAY_BRACKET);
        } while (isNextMatches(0, TokenType.L_ARRAY_BRACKET));
        return new ArrayAccessExpression(variable, list);
    }

    private Expression someArray() {
        consume(TokenType.L_ARRAY_BRACKET);
        List<Expression> list = new ArrayList<>();
        while (!isMatches(TokenType.R_ARRAY_BRACKET)) {
            list.add(someExpression());
            isMatches(TokenType.COMMA);
        }
        return new ArrayExpression(list);
    }

    private Expression someExpression() {
        return ternaryExpression();
    }

    private Expression ternaryExpression() {
        Expression output = logicalOrExpression();
        if (isMatches(TokenType.QUESTION_MARK)) {
            Expression trueExpression = someExpression();
            consume(TokenType.COLON);
            Expression falseExpression = someExpression();
            return new TernaryExpression(output, trueExpression, falseExpression);
        }
        return output;
    }

    private Expression logicalOrExpression() {
        Expression expression = logicalAndExpression();
        while (true) {
            if (isMatches(TokenType.D_LOR)) {
                expression = new ComparingExpression(ComparingExpression.Operator.LOR, expression, logicalAndExpression());
                continue;
            }
            break;
        }
        return expression;
    }

    private Expression logicalAndExpression() {
        Expression expression = equalityExpression();
        while (true) {
            if (isMatches(TokenType.D_LAND)) {
                expression = new ComparingExpression(ComparingExpression.Operator.LAND, expression, equalityExpression());
                continue;
            }
            break;
        }
        return expression;
    }

    private Expression equalityExpression() {
        Expression expression = comparingExpression();
        if (isMatches(TokenType.D_EQUAL)) {
            return new ComparingExpression(ComparingExpression.Operator.EQUAL, expression, comparingExpression());
        }
        if (isMatches(TokenType.LNOT_EQUAL)) {
            return new ComparingExpression(ComparingExpression.Operator.LNOT_EQUAL, expression, comparingExpression());
        }
        return expression;
    }

    private Expression comparingExpression() {
        Expression expression = additionExpression();
        while (true) {
            if (isMatches(TokenType.LT)) {
                expression = new ComparingExpression(ComparingExpression.Operator.LT, expression, additionExpression());
                continue;
            }
            if (isMatches(TokenType.LT_EQUAL)) {
                expression = new ComparingExpression(ComparingExpression.Operator.LT_EQUAL, expression, additionExpression());
                continue;
            }
            if (isMatches(TokenType.GT)) {
                expression = new ComparingExpression(ComparingExpression.Operator.GT, expression, additionExpression());
                continue;
            }
            if (isMatches(TokenType.GT_EQUAL)) {
                expression = new ComparingExpression(ComparingExpression.Operator.GT_EQUAL, expression, additionExpression());
                continue;
            }
            break;
        }
        return expression;
    }

    private Expression additionExpression() {
        Expression expression = multiplicationExpression();
        while (true) {
            if (isMatches(TokenType.ADD)) {
                expression = new ArithmeticExpression('+', expression, multiplicationExpression());
                continue;
            }
            if (isMatches(TokenType.SUB)) {
                expression = new ArithmeticExpression('-', expression, multiplicationExpression());
                continue;
            }
            break;
        }
        return expression;
    }

    private Expression multiplicationExpression() {
        Expression expression = unaryExpression();
        while (true) {
            if (isMatches(TokenType.MUL)) {
                expression = new ArithmeticExpression('*', expression, unaryExpression());
                continue;
            }
            if (isMatches(TokenType.DIV)) {
                expression = new ArithmeticExpression('/', expression, unaryExpression());
                continue;
            }
            if (isMatches(TokenType.MOD)) {
                expression = new ArithmeticExpression('%', expression, unaryExpression());
                continue;
            }
            break;
        }
        return expression;
    }

    private Expression unaryExpression() {
        if (isMatches(TokenType.SUB)) return new UnaryExpression('-', primaryExpression());
        if (isMatches(TokenType.ADD)) return primaryExpression();
        return primaryExpression();
    }

    private Expression primaryExpression() {
        Token currentToken = getCurrentToken(0);
        if (isMatches(TokenType.DEC_NUMBER)) {
            return new ValueExpression(Double.parseDouble(currentToken.getText()));
        }
        if (isMatches(TokenType.HEX_NUMBER)) {
            return new ValueExpression(Long.parseLong(currentToken.getText(), 16));
        }
        if (isNextMatches(0, TokenType.WORD) && isNextMatches(1, TokenType.LEFT_BRACKET)) {
            return nativeFunction();
        }
        if (isNextMatches(0, TokenType.WORD) && isNextMatches(1, TokenType.L_ARRAY_BRACKET)) {
            return someElement();
        }
        if (isNextMatches(0, TokenType.L_ARRAY_BRACKET)) {
            return someArray();
        }
        if (isMatches(TokenType.WORD)) {
            return new VariableExpression(currentToken.getText());
        }
        if (isMatches(TokenType.TEXT)) {
            return new ValueExpression(currentToken.getText());
        }
        if (isMatches(TokenType.LEFT_BRACKET)) {
            Expression expressionResult = someExpression();
            isMatches(TokenType.RIGHT_BRACKET);
            return expressionResult;
        }
        throw new RuntimeException("Unknown expression!");
    }

    private Token consume(TokenType type) {
        Token currentToken = getCurrentToken(0);
        if (type != currentToken.getType()) {
            throw new RuntimeException("Token '" + currentToken + "' doesn't match '" + type + "'");
        }
        currentPosition++;
        return currentToken;
    }

    private boolean isMatches(TokenType type) {
        Token currentToken = getCurrentToken(0);
        if (type != currentToken.getType()) return false;
        currentPosition++;
        return true;
    }

    private boolean isNextMatches(int position, TokenType type) {
        return getCurrentToken(position).getType() == type;
    }

    private Token getCurrentToken(int nextPosition) {
        int position = currentPosition + nextPosition;
        if (position >= size) return END;
        return list.get(position);
    }
}
