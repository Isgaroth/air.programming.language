package air.syntax.tree.statement;

import air.patterns.Visitor;

import java.util.ArrayList;
import java.util.List;

public class ProgramBlockStatement implements  Statement{

    public final List<Statement> list;

    public ProgramBlockStatement() {
        list = new ArrayList<>();
    }

    public void add(Statement statement) {
        list.add(statement);
    }

    @Override
    public void launch() {
        list.forEach(Statement::launch);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Statement statement : list) output.append(statement.toString()).append(System.lineSeparator());
        return output.toString();
    }
}
