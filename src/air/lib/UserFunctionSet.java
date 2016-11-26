package air.lib;

import air.syntax.tree.statement.ReturnStatement;
import air.syntax.tree.statement.Statement;

import java.util.List;

public class UserFunctionSet implements Function {

    private static final NumberValue ZERO = new NumberValue(0);

    private final List<String> argumentTitle;
    private final Statement body;

    public UserFunctionSet(List<String> argumentTitle, Statement body) {
        this.argumentTitle = argumentTitle;
        this.body = body;
    }

    public int getArgumentNumber() {
        return argumentTitle.size();
    }

    public String getArgumentTitle(int index) {
        if (index < 0 || index >= getArgumentNumber()) return "";
        return argumentTitle.get(index);
    }

    @Override
    public Value launch(Value... arguments) {
        try {
            body.launch();
            return ZERO;
        } catch (ReturnStatement r) {
            return r.getResult();
        }
    }
}
