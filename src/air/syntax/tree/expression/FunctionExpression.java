package air.syntax.tree.expression;

import air.lib.*;
import air.patterns.Visitor;

import java.util.ArrayList;
import java.util.List;

public class FunctionExpression implements Expression {

    public final String title;
    public final List<Expression> arguments;

    public FunctionExpression(String title) {
        this.title = title;
        arguments = new ArrayList<>();
    }

    public FunctionExpression(String title, List<Expression> arguments) {
        this.title = title;
        this.arguments = arguments;
    }

    public void addArgument(Expression content) {
        arguments.add(content);
    }

    @Override
    public Value count() {
        int size = arguments.size();
        Value[] values = new Value[size];
        for (int i = 0; i < size; i++) values[i] = arguments.get(i).count();
        Function function = FunctionSet.getKey(title);
        if (function instanceof UserFunctionSet) {
            UserFunctionSet set = (UserFunctionSet) function;
            if (size != set.getArgumentNumber()) throw new RuntimeException("Arguments count mismatch");
            Variables.pushIn();
            for (int i = 0; i < size; i++) Variables.setKey(set.getArgumentTitle(i), values[i]);
            Value result = set.launch(values);
            Variables.pushOut();
            return result;
        }
        return function.launch(values);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return title + "(" + arguments.toString() + ")";
    }
}
