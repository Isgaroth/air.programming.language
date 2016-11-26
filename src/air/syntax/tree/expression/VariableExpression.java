package air.syntax.tree.expression;

import air.lib.Value;
import air.lib.Variables;
import air.patterns.Visitor;

public class VariableExpression implements Expression {

    private final String title;

    public VariableExpression(String title) {
        this.title = title;
    }

    @Override
    public Value count() {
        if (!Variables.isExists(title)) throw new RuntimeException("Constant does not exists");
        return Variables.getKey(title);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return title;
    }
}
