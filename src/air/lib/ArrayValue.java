package air.lib;

import java.util.Arrays;

public class ArrayValue implements Value {

    private final Value[] array;

    public ArrayValue(int size) {
        this.array = new Value[size];
    }

    public ArrayValue(Value[] array) {
        this.array = new Value[array.length];
        System.arraycopy(array, 0, this.array, 0, array.length);
    }

    public ArrayValue(ArrayValue value) {
        this(value.array);
    }

    public Value get(int index) {
        return array[index];
    }

    public void set(int index, Value value) {
        array[index] = value;
    }

    @Override
    public double asNumber() {
        throw new RuntimeException("Cannot cast array to number");
    }

    @Override
    public String asString() {
        return Arrays.toString(array);
    }

    @Override
    public String toString() {
        return asString();
    }
}
