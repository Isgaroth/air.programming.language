package air.syntax.tree.statement;

import air.patterns.Visitor;
import air.syntax.tree.expression.Expression;

public class ForCycleStatement implements Statement {

    public final Statement initialization, counterChange, statementOrBlock;
    public final Expression verification;

    public ForCycleStatement(Statement initialization, Expression verification, Statement counterChange, Statement statementOrBlock) {
        this.initialization = initialization;
        this.verification = verification;
        this.counterChange = counterChange;
        this.statementOrBlock = statementOrBlock;
    }

    @Override
    public void launch() {
        for (initialization.launch(); verification.count().asNumber() != 0; counterChange.launch()) {
            try {
                statementOrBlock.launch();
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
        return "for " + initialization + "; " + verification + "; " + counterChange + " " + statementOrBlock;
    }
}
