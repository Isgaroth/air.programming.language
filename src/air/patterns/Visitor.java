package air.patterns;

import air.syntax.tree.expression.*;
import air.syntax.tree.statement.*;

public interface Visitor {

    // Statements
    void visit(ArrayAssignmentStatement statement);

    void visit(AssignmentStatement statement);

    void visit(BreakStatement statement);

    void visit(ContinueStatement statement);

    void visit(ExploitStatement statement);

    void visit(ForCycleStatement statement);

    void visit(FunctionStatement statement);

    void visit(IfStatement statement);

    void visit(PrintlnStatement statement);

    void visit(PrintStatement statement);

    void visit(ProgramBlockStatement statement);

    void visit(RepeatUntilCycleStatement statement);

    void visit(ReturnStatement statement);

    void visit(UserFunctionStatement statement);

    void visit(WhileCycleStatement statement);


    // Expressions
    void visit(ArithmeticExpression expression);

    void visit(ArrayAccessExpression expression);

    void visit(ArrayExpression expression);

    void visit(ComparingExpression expression);

    void visit(FunctionExpression expression);

    void visit(TernaryExpression expression);

    void visit(UnaryExpression expression);

    void visit(ValueExpression expression);

    void visit(VariableExpression expression);
}
