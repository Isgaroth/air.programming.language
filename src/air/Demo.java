package air;

import air.analyzer.Lexer;
import air.analyzer.Parser;
import air.patterns.util.parser.FunctionAdder;
import air.patterns.util.parser.ConstantValidator;
import air.syntax.tree.statement.Statement;
import air.util.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Demo {

    private static final String LAB_PATH = "D:/&projects/ide/Intellij IDEA/air.programming.language/src/air/example/ted/lab3";
    private static final String ART_PATH = "D:/&projects/ide/Intellij IDEA/air.programming.language/src/air/example/art/agario";
    private static final String DEFAULT_PATH = "D:/&projects/ide/Intellij IDEA/air.programming.language/src/air/example/demo.air";

    public static void main(String[] arguments) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get(LAB_PATH)), "UTF-8");
        List<Token> tokenList = new Lexer(input).createToken();
        //tokenList.forEach(System.out::println); // print tree

        Statement statement = new Parser(tokenList).parse();
        //System.out.println(statement.toString()); // print statement
        statement.accept(new FunctionAdder());
        statement.accept(new ConstantValidator());
        statement.launch();
    }
}
