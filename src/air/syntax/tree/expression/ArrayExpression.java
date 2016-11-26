package air.syntax.tree.expression;

import air.lib.ArrayValue;
import air.lib.Value;
import air.patterns.Visitor;

import java.util.List;

public class ArrayExpression implements Expression {

    public final List<Expression> list;

    public ArrayExpression(List<Expression> list) {
        this.list = list;
    }

    @Override
    public Value count() {
        int size = list.size();
        ArrayValue arrayValue = new ArrayValue(size);
        for (int i = 0; i < size; i++) arrayValue.set(i, list.get(i).count());
        return arrayValue;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
