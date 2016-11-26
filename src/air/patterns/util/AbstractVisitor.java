package air.patterns.util;

import air.lib.Variables;
import air.patterns.Visitor;
import air.syntax.tree.expression.*;
import air.syntax.tree.statement.*;

public abstract class AbstractVisitor implements Visitor {

    public static final String ERROR = "Cannot assign value to constant";

    @Override
    public void visit(ArrayAssignmentStatement statement) {
        statement.array.accept(this);
        statement.expression.accept(this);
    }

    @Override
    public void visit(AssignmentStatement statement) {
        statement.expression.accept(this);
        if (Variables.isExists(statement.variable)) throw new RuntimeException(ERROR);
    }

    @Override
    public void visit(BreakStatement statement) {
    }

    @Override
    public void visit(ContinueStatement statement) {
    }

    @Override
    public void visit(ExploitStatement statement) {
        statement.expression.accept(this);
    }

    @Override
    public void visit(ForCycleStatement statement) {
        statement.initialization.accept(this);
        statement.verification.accept(this);
        statement.counterChange.accept(this);
        statement.statementOrBlock.accept(this);
    }

    @Override
    public void visit(FunctionStatement statement) {
        statement.function.accept(this);
    }

    @Override
    public void visit(IfStatement statement) {
        statement.expression.accept(this);
        statement.ifStatement.accept(this);
        if (statement.elseStatement != null) statement.elseStatement.accept(this);
    }

    @Override
    public void visit(PrintlnStatement statement) {
        statement.expression.accept(this);
    }

    @Override
    public void visit(PrintStatement statement) {
        statement.expression.accept(this);
    }

    @Override
    public void visit(ProgramBlockStatement statement) {
        for (Statement programBlockStatement : statement.list) programBlockStatement.accept(this);
    }

    @Override
    public void visit(RepeatUntilCycleStatement statement) {
        statement.expression.accept(this);
        statement.condition.accept(this);
    }

    @Override
    public void visit(ReturnStatement statement) {
    }

    @Override
    public void visit(UserFunctionStatement statement) {
        statement.body.accept(this);
    }

    @Override
    public void visit(WhileCycleStatement statement) {
        statement.expression.accept(this);
        statement.condition.accept(this);
    }

    @Override
    public void visit(ArithmeticExpression expression) {
        expression.fExpression.accept(this);
        expression.sExpression.accept(this);
    }

    @Override
    public void visit(ArrayAccessExpression expression) {
        for (Expression arrayAccessExpression : expression.list) arrayAccessExpression.accept(this);
    }

    @Override
    public void visit(ArrayExpression expression) {
        for (Expression arrayExpression: expression.list) arrayExpression.accept(this);
    }

    @Override
    public void visit(ComparingExpression expression) {
        expression.fExpression.accept(this);
        expression.sExpression.accept(this);
    }

    @Override
    public void visit(FunctionExpression expression) {
        for (Expression functionExpression : expression.arguments) functionExpression.accept(this);
    }

    @Override
    public void visit(TernaryExpression expression) {
        expression.condition.accept(this);
        expression.trueExpression.accept(this);
        expression.falseExpression.accept(this);
    }

    @Override
    public void visit(UnaryExpression expression) {
        expression.expression.accept(this);
    }

    @Override
    public void visit(ValueExpression expression) {
    }

    @Override
    public void visit(VariableExpression expression) {
    }
}
