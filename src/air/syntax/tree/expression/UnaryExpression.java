package air.syntax.tree.expression;

import air.lib.NumberValue;
import air.lib.Value;
import air.patterns.Visitor;

public class UnaryExpression implements Expression {

    public final Expression expression;
    public final char operator;

    public UnaryExpression(char operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }

    @Override
    public Value count() {
        switch (operator) {
            case '-': return new NumberValue(-expression.count().asNumber());
            default: return expression.count();
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("%c %s", operator, expression);
    }
}
