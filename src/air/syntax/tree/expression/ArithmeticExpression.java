package air.syntax.tree.expression;

import air.lib.ArrayValue;
import air.lib.NumberValue;
import air.lib.StringValue;
import air.lib.Value;
import air.patterns.Visitor;

public class ArithmeticExpression implements Expression {

    public final Expression fExpression, sExpression;
    public final char operator;

    public ArithmeticExpression(char operator, Expression fExpression, Expression sExpression) {
        this.operator = operator;
        this.fExpression = fExpression;
        this.sExpression = sExpression;
    }

    @Override
    public Value count() {
        Value fValue = fExpression.count(), sValue = sExpression.count();
        if ((fValue instanceof StringValue) || (fValue instanceof ArrayValue)) {
            String fString = fValue.asString();
            switch (operator) {
                case '*': {
                    int iteration = (int) sValue.asNumber();
                    StringBuilder buffer = new StringBuilder();
                    for (int i = 0; i < iteration; i++) {
                        buffer.append(fString);
                    }
                    return new StringValue(buffer.toString());
                }
                case '+': default: return new StringValue(fString + sValue.asString());
            }
        }
        double fNumber = fValue.asNumber(), sNumber = sValue.asNumber();
        switch (operator) {
            case '-': {
                return new NumberValue(fNumber - sNumber);
            }
            case '*': {
                return new NumberValue(fNumber * sNumber);
            }
            case '/': {
                return new NumberValue(fNumber / sNumber);
            }
            case '%': {
                return new NumberValue(fNumber % sNumber);
            }
            case '+': default: {
                return new NumberValue(fNumber + sNumber);
            }
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("(%s %c %s)", fExpression, operator, sExpression);
    }
}
