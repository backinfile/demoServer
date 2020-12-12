package com.backinfile.core.serilize;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class AutoExpandStream extends OutputStream {

    private byte[] buffer;
    private int cur = 0;

    public AutoExpandStream() {
        this(8);
    }

    public AutoExpandStream(int capacity) {
        buffer = new byte[capacity];
    }


    @Override
    public void write(int b) throws IOException {
        expandCheck(cur + 1);
        buffer[cur++] = (byte) b;
    }

    private void expandCheck(int minCapacity) {
        if (buffer.length > minCapacity) return;
        outOfCheck(minCapacity);
        int newCapacity = buffer.length * 2;
        outOfCheck(newCapacity);
        if (newCapacity < minCapacity) {
            newCapacity = minCapacity + 8;
        }
        buffer = Arrays.copyOf(buffer, newCapacity);
    }

    // 溢出检查
    private void outOfCheck(int capacity) {
        if (capacity < 0) {
            throw new OutOfMemoryError();
        }
    }


    public int size() {
        return cur;
    }

    public void clear() {
        cur = 0;
    }

    public byte[] bytes() {
        return buffer;
    }
}
