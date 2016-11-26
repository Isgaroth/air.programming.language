package air.syntax.tree.expression;

import air.lib.Value;
import air.patterns.util.Node;

public interface Expression extends Node {

    Value count();
}
