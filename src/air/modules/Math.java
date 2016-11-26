package air.modules;

import air.lib.*;
import air.modules.core.Module;

import java.util.Random;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;

public class Math implements Module {

    private static final DoubleFunction<NumberValue> FUNCTION = NumberValue::new;

    @Override
    public void init() {
        FunctionSet.setKey("random", new RandomValue());

        FunctionSet.setKey("acos", singleConvert(java.lang.Math::acos));
        FunctionSet.setKey("asin", singleConvert(java.lang.Math::asin));
        FunctionSet.setKey("atan", singleConvert(java.lang.Math::atan));

        FunctionSet.setKey("cos", singleConvert(java.lang.Math::cos));
        FunctionSet.setKey("sin", singleConvert(java.lang.Math::sin));
        FunctionSet.setKey("tan", singleConvert(java.lang.Math::tan));

        FunctionSet.setKey("toDegrees", singleConvert(java.lang.Math::toDegrees));
        FunctionSet.setKey("toRadians", singleConvert(java.lang.Math::toRadians));

        FunctionSet.setKey("log", singleConvert(java.lang.Math::log));
        FunctionSet.setKey("log10", singleConvert(java.lang.Math::log10));

        FunctionSet.setKey("abs", singleConvert(java.lang.Math::abs));
        FunctionSet.setKey("sqrt", singleConvert(java.lang.Math::sqrt));
        FunctionSet.setKey("factorial", singleConvert(Math::factorial));

        FunctionSet.setKey("polar", multiConvert(java.lang.Math::atan2));
        FunctionSet.setKey("pow", multiConvert(java.lang.Math::pow));
        FunctionSet.setKey("max", multiConvert(java.lang.Math::max));
        FunctionSet.setKey("min", multiConvert(java.lang.Math::min));

        Variables.setKey("PI", new NumberValue(3.14159265358));
        Variables.setKey("GR", new NumberValue(1.61803398875));
        Variables.setKey("E", new NumberValue(2.71828182845));
    }

    private static double factorial(double value) {
        if (value == 1) return 1;
        return value * factorial(value - 1);
    }

    private static Function singleConvert(DoubleUnaryOperator operator) {
        return arguments -> {
            if (arguments.length != 1) throw new RuntimeException("One argument expected");
            return FUNCTION.apply(operator.applyAsDouble(arguments[0].asNumber()));
        };
    }

    private static Function multiConvert(DoubleBinaryOperator operator) {
        return arguments -> {
            if (arguments.length != 2) throw new RuntimeException("Two arguments expected");
            return FUNCTION.apply(operator.applyAsDouble(arguments[0].asNumber(), arguments[1].asNumber()));
        };
    }

    private static class RandomValue implements Function {

        private static final Random VALUE = new Random();

        @Override
        public Value launch(Value... arguments) {
            if (arguments.length == 0) return new NumberValue(VALUE.nextDouble());
            int leftBorder = 0, rightBorder = 100;
            if (arguments.length == 1) rightBorder = (int) arguments[0].asNumber();
            else if (arguments.length == 2) {
                leftBorder = (int) arguments[0].asNumber();
                rightBorder = (int) arguments[1].asNumber();
            }
            return new NumberValue(VALUE.nextInt(rightBorder - leftBorder) + leftBorder);
        }
    }
}
