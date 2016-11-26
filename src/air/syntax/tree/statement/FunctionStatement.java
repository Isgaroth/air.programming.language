package air.syntax.tree.statement;

import air.patterns.Visitor;
import air.syntax.tree.expression.FunctionExpression;

public class FunctionStatement implements Statement {

    public final FunctionExpression function;

    public FunctionStatement(FunctionExpression function) {
        this.function = function;
    }

    @Override
    public void launch() {
        function.count();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return function.toString();
    }
}
