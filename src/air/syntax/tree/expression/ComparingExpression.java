package air.syntax.tree.expression;

import air.lib.NumberValue;
import air.lib.StringValue;
import air.lib.Value;
import air.patterns.Visitor;

public class ComparingExpression implements Expression {

    public enum Operator {
        ADD("+"), SUB("-"), MUL("*"), DIV("/"),
        EQUAL("=="), LNOT_EQUAL("!="), LT("<"),
        LT_EQUAL("<="), GT(">"), GT_EQUAL(">="),
        LAND("&"), LOR("|");

        private final String title;

        Operator(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public final Expression fExpression, sExpression;
    public final Operator operator;

    public ComparingExpression(Operator operator, Expression fExpression, Expression sExpression) {
        this.operator = operator;
        this.fExpression = fExpression;
        this.sExpression = sExpression;
    }

    @Override
    public Value count() {
        Value fValue = fExpression.count();
        Value sValue = sExpression.count();

        double fNumber, sNumber;
        if (fValue instanceof  StringValue) {
            fNumber = fValue.asString().compareTo(sValue.asString());
            sNumber = 0;
        } else {
            fNumber = fValue.asNumber();
            sNumber = sValue.asNumber();
        }

        boolean output;
        switch (operator) {
            case LT:
                output = fNumber < sNumber;
                break;
            case LT_EQUAL:
                output = fNumber <= sNumber;
                break;
            case GT:
                output = fNumber > sNumber;
                break;
            case GT_EQUAL:
                output = fNumber >= sNumber;
                break;
            case LNOT_EQUAL:
                output = fNumber != sNumber;
                break;
            case LAND:
                output = (fNumber != 0) && (sNumber != 0);
                break;
            case LOR:
                output = (fNumber != 0) || (sNumber != 0);
                break;
            case EQUAL: default:
                output = fNumber == sNumber;
        }
        return new NumberValue(output);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", fExpression, operator.getTitle(), sExpression);
    }
}
