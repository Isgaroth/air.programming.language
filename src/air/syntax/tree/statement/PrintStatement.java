package air.syntax.tree.statement;

import air.patterns.Visitor;
import air.syntax.tree.expression.Expression;

public class PrintStatement implements Statement {

    public final Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void launch() {
        System.out.print(expression.count());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "print " + expression;
    }
}
