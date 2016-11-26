package air.syntax.tree.expression;

import air.lib.ArrayValue;
import air.lib.Value;
import air.lib.Variables;
import air.patterns.Visitor;

import java.util.List;

public class ArrayAccessExpression implements Expression {

    public final String variable;
    public final List<Expression> list;

    public ArrayAccessExpression(String variable, List<Expression> list) {
        this.variable = variable;
        this.list = list;
    }

    public int getIndex() {
        return getIndex(list.size() - 1);
    }

    public ArrayValue getArray() {
        ArrayValue arrayValue = consumeArray(Variables.getKey(variable));
        int lastIndex = list.size() - 1;
        for (int i = 0; i < lastIndex; i++) {
            arrayValue = consumeArray(arrayValue.get(getIndex(i)));
        }
        return arrayValue;
    }


    @Override
    public Value count() {
        return getArray().get(getIndex());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return variable + list;
    }

    private int getIndex(int index) {
        return (int) list.get(index).count().asNumber();
    }

    private ArrayValue consumeArray(Value value) {
        if (value instanceof ArrayValue) {
            return (ArrayValue) value;
        } else {
            throw new RuntimeException("Array expected");
        }
    }
}
