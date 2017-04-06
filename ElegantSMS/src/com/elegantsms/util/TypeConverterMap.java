package com.elegantsms.util;

import java.util.Arrays;
import java.util.HashMap;

public class TypeConverterMap extends HashMap<Class<?>, TypeConverter<?>> {

    public <T> T convert(String arg, Class<T> type) {
        return (T) get(type).convert(arg);
    }

    public <T> Object convertArray(String[] arg, Class<T> type) {
        return Arrays.stream(arg).map(item -> convert(item, type.getComponentType())).toArray();
    }

}
