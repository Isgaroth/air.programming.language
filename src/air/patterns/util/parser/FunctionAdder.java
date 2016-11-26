package air.patterns.util.parser;

import air.patterns.util.AbstractVisitor;
import air.syntax.tree.statement.UserFunctionStatement;

public class FunctionAdder extends AbstractVisitor {

    @Override
    public void visit(UserFunctionStatement statement) {
        statement.body.accept(this);
        statement.launch();
    }
}
