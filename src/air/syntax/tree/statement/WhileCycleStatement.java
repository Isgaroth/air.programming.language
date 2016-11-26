package air.syntax.tree.statement;

import air.patterns.Visitor;
import air.syntax.tree.expression.Expression;

public class WhileCycleStatement implements Statement {

    public final Expression expression;
    public final Statement condition;

    public WhileCycleStatement(Expression expression, Statement condition) {
        this.expression = expression;
        this.condition = condition;
    }

    @Override
    public void launch() {
        while (expression.count().asNumber() != 0) {
            try {
                condition.launch();
            } catch (BreakStatement b) {
                break;
            } catch (ContinueStatement c) {
                continue;
            }
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "while " + expression + " " + condition;
    }
}
