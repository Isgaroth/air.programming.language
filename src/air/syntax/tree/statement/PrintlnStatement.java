package air.syntax.tree.statement;

import air.patterns.Visitor;
import air.syntax.tree.expression.Expression;

public class PrintlnStatement implements Statement {

    public final Expression expression;

    public PrintlnStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void launch() {
        System.out.println(expression.count());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "println " + expression;
    }
}
