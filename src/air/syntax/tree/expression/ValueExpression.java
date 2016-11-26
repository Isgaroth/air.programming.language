package air.syntax.tree.expression;

import air.lib.NumberValue;
import air.lib.StringValue;
import air.lib.Value;
import air.patterns.Visitor;

public class ValueExpression implements Expression{

    public final Value value;

    public ValueExpression(double value) {
        this.value = new NumberValue(value);
    }

    public ValueExpression(String value) {
        this.value = new StringValue(value);
    }

    @Override
    public Value count() {
        return value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return value.asString();
    }
}
