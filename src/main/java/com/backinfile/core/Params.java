package com.backinfile.core;

import java.util.HashMap;
import java.util.Map;

import com.backinfile.core.serilize.ISerializable;
import com.backinfile.core.serilize.InputStream;
import com.backinfile.core.serilize.OutputStream;
import com.backinfile.support.Log;

public class Params implements ISerializable {
    private Map<String, Object> values = new HashMap<>();
	private Object singleValue = null;

    public Params() {
    }

    public Params(Object... params) {
        if (params == null) return;
        if (params.length == 0) return;
        if (params.length == 1) {
            setValue(params[0]);
            return;
        }
        if (params.length % 2 == 0) {
            for (int i = 0; i < params.length; i += 2) {
                Object key = params[i];
                Object value = params[i + 1];
                if (key instanceof String) {
                    setValue((String) key, value);
                } else {
                    Log.Game.warn("ignoring param's arg:{0} {1}", key, value);
                }
            }
        }
    }

    public void setValue(Object value) {
        singleValue = value;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) singleValue;
    }

    public void setValue(String key, Object value) {
        values.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key) {
        return (T) values.get(key);
    }

    @Override
    public void writeTo(OutputStream out) {
        out.write(values);
        out.write(singleValue);
    }

    @Override
    public void readFrom(InputStream in) {
        values = in.read();
        singleValue = in.read();
    }
}
