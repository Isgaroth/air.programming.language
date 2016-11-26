package air.patterns.util.parser;

import air.lib.Variables;
import air.patterns.util.AbstractVisitor;
import air.syntax.tree.statement.AssignmentStatement;

public class ConstantValidator extends AbstractVisitor {

    @Override
    public void visit(AssignmentStatement statement) {
        statement.expression.accept(this);
        if (Variables.isExists(statement.variable)) throw new RuntimeException(ERROR);
    }
}
