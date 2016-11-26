package air.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public class Variables {

    private static final Stack<Map<String, Value>> STACK;

    private static Map<String, Value> variableMap;

    static {
        STACK = new Stack<>();
        variableMap = new ConcurrentHashMap<>();
        variableMap.put("true", NumberValue.ONE);
        variableMap.put("false", NumberValue.ZERO);
    }

    public static void pushIn() {
        STACK.push(new HashMap<>(variableMap));
    }

    public static void pushOut() {
        variableMap = STACK.pop();
    }

    public static boolean isExists(String key) {
        return variableMap.containsKey(key);
    }

    public static Value getKey(String key) {
        if (!isExists(key)) return NumberValue.ZERO;
        return variableMap.get(key);
    }

    public static void setKey(String key, Value value) {
        variableMap.put(key, value);
    }
}
