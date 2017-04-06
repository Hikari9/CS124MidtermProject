package com.elegantsms.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.IntFunction;

public class TypeConverterMap extends HashMap<Class<?>, TypeConverter<?>> {

    public <T> T convert(String arg, Class<T> type) {
        return (T) get(type).convert(arg);
    }

}
