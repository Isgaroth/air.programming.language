package air.modules;

import air.lib.FunctionSet;
import air.lib.NumberValue;
import air.modules.core.Module;

public class Thread implements Module {

    @Override
    public void init() {
        FunctionSet.setKey("sleep", args -> {
            if (args.length == 1) {
                try {
                    java.lang.Thread.sleep((long) args[0].asNumber());
                } catch (InterruptedException ex) {
                    java.lang.Thread.currentThread().interrupt();
                }
            }
            return NumberValue.ZERO;
        });
    }
}
