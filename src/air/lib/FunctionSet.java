package air.lib;

import java.util.HashMap;
import java.util.Map;

public class FunctionSet {

    private static final Map<String, Function> FUNCTION_MAP;

    static {
        FUNCTION_MAP = new HashMap<>();
        FUNCTION_MAP.put("echo", arguments -> {
            for (Value content : arguments) {
                System.out.println(content.asString());
            }
            return NumberValue.ZERO;
        });
        FUNCTION_MAP.put("array", new Function() {
            @Override
            public Value launch(Value... arguments) {
                return create(arguments, 0);
            }

            private ArrayValue create(Value[] values, int index) {
                int size = (int) values[index].asNumber();
                ArrayValue arrayValue = new ArrayValue(size);
                if (index == values.length - 1) {
                    for (int i = 0; i < size; i++) {
                        arrayValue.set(i, NumberValue.ZERO);
                    }
                } else if (index < values.length - 1) {
                    for (int i = 0; i < size; i++) {
                        arrayValue.set(i, create(values, index + 1));
                    }
                }
                return arrayValue;
            }
        });
    }

    public static boolean isExists(String key) {
        return FUNCTION_MAP.containsKey(key);
    }

    public static Function getKey(String key) {
        if (!isExists(key)) throw new RuntimeException("Unknown function '" + key + "'");
        return FUNCTION_MAP.get(key);
    }

    public static void setKey(String key, Function function) {
        FUNCTION_MAP.put(key, function);
    }
}
