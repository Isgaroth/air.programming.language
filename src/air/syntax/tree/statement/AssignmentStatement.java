package air.syntax.tree.statement;

import air.lib.Value;
import air.lib.Variables;
import air.patterns.Visitor;
import air.syntax.tree.expression.Expression;

public class AssignmentStatement implements Statement {

    public final String variable;
    public final Expression expression;

    public AssignmentStatement(String variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public void launch() {
        Value resultExpression = expression.count();
        Variables.setKey(variable, resultExpression);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("%s = %s", variable, expression);
    }
}
