package com.example.dipping_spring_batch.util;

import java.util.Arrays;
import java.util.Comparator;

public class BoundedArray<T> {
    private final T[] values;
    private final int size;
    private int count;
    private final Comparator<T> comparator;

    public BoundedArray(int size, Comparator<T> comparator) {
        this.values = (T[]) new Object[size];
        this.size = size;
        this.comparator = comparator;
    }

    public void add(T value) {
        if (count < size) {
            if (count == 0 || comparator.compare(value, values[count - 1]) > 0) {
                values[count++] = value;
            }
        } else if (comparator.compare(value, values[0]) > 0) {
            int index = Arrays.binarySearch(values, 1, count, value, comparator);
            if (index < 0) {
                index = -index - 1;
            }
            System.arraycopy(values, 1, values, 0, index - 1);
            values[index - 1] = value;
        }
    }

    public T[] getValues() {
        return Arrays.copyOf(values, count);
    }
}
