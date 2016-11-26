package air.syntax.tree.statement;

import air.lib.FunctionSet;
import air.lib.UserFunctionSet;
import air.patterns.Visitor;

import java.util.List;

public class UserFunctionStatement implements Statement {

    public final String title;
    public final List<String> argumentTitle;
    public final Statement body;

    public UserFunctionStatement(String title, List<String> argumentTitle, Statement body) {
        this.title = title;
        this.argumentTitle = argumentTitle;
        this.body = body;
    }

    @Override
    public void launch() {
        FunctionSet.setKey(title, new UserFunctionSet(argumentTitle, body));
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "def (" + argumentTitle.toString() + ") " + body.toString();
    }
}
