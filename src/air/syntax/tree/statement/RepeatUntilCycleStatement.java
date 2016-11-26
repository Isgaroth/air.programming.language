package air.syntax.tree.statement;

import air.patterns.Visitor;
import air.syntax.tree.expression.Expression;

public class RepeatUntilCycleStatement implements Statement {

    public final Expression expression;
    public final Statement condition;

    public RepeatUntilCycleStatement(Expression expression, Statement condition) {
        this.expression = expression;
        this.condition = condition;
    }

    @Override
    public void launch() {
        do {
            try {
                condition.launch();
            } catch (BreakStatement b) {
                break;
            } catch (ContinueStatement c) {
                continue;
            }
        } while (expression.count().asNumber() != 0);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "repeat " + condition + " until " + expression;
    }
}
