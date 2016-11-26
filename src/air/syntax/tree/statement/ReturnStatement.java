package air.syntax.tree.statement;

import air.lib.Value;
import air.patterns.Visitor;
import air.syntax.tree.expression.Expression;

public class ReturnStatement extends RuntimeException implements Statement {

    private final Expression expression;
    private Value result;

    public ReturnStatement(Expression expression) {
        this.expression = expression;
    }

    public Value getResult() {
        return result;
    }

    @Override
    public void launch() {
        result = expression.count();
        throw this;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "return";
    }
}
