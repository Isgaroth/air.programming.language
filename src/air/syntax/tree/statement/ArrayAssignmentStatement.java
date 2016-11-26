package air.syntax.tree.statement;

import air.patterns.Visitor;
import air.syntax.tree.expression.ArrayAccessExpression;
import air.syntax.tree.expression.Expression;

public class ArrayAssignmentStatement implements Statement {

    public final ArrayAccessExpression array;
    public final Expression expression;

    public ArrayAssignmentStatement(ArrayAccessExpression array, Expression expression) {
        this.array = array;
        this.expression = expression;
    }

    @Override
    public void launch() {
        array.getArray().set(array.getIndex(), expression.count());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("%s = %s", array, expression);
    }
}
