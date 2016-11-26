package air.syntax.tree.expression;

import air.lib.Value;
import air.patterns.Visitor;

public class TernaryExpression implements Expression {

    public final Expression condition, trueExpression, falseExpression;

    public TernaryExpression(Expression condition, Expression trueExpression, Expression falseExpression) {
        this.condition = condition;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
    }

    @Override
    public Value count() {
        if (condition.count().asNumber() != 0) return trueExpression.count();
        else return falseExpression.count();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("%s ? %s : %s", condition, trueExpression, falseExpression);
    }
}
