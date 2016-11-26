package air.patterns.util;

import air.patterns.Visitor;

public interface Node {

    void accept(Visitor visitor);
}
