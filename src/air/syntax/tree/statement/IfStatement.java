package air.syntax.tree.statement;

import air.patterns.Visitor;
import air.syntax.tree.expression.Expression;

public class IfStatement implements Statement {

    public final Expression expression;
    public final Statement ifStatement, elseStatement;

    public IfStatement(Expression expression, Statement ifStatement, Statement elseStatement) {
        this.expression = expression;
        this.ifStatement = ifStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public void launch() {
        double result = expression.count().asNumber();
        if (result != 0) ifStatement.launch();
        else if (elseStatement != null) elseStatement.launch();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("if ").append(expression).append(' ').append(ifStatement);
        if (elseStatement != null) output.append("\nelse ").append(elseStatement);
        return output.toString();
    }
}
