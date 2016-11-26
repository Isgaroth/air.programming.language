package air.syntax.tree.statement;

import air.patterns.Visitor;

public class BreakStatement extends RuntimeException implements Statement {

    @Override
    public void launch() {
        throw this;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "break";
    }
}
