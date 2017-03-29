package me.ricotiongson.elegantsms.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TypeConverterFactory {

    private final static TypeConverterMap map = new TypeConverterMap() {{
        put(String.class, arg -> arg);
        // non-primitives
        put(Integer.class, Integer::parseInt);
        put(Long.class, Long::parseLong);
        put(Boolean.class, Boolean::parseBoolean);
        put(Short.class, Short::parseShort);
        put(Byte.class, Byte::parseByte);
        put(Character.class, arg -> (Character) arg.charAt(0));
        put(Double.class, Double::parseDouble);
        put(Float.class, Float::parseFloat);
        put(BigInteger.class, BigDecimal::new);
        put(BigDecimal.class, BigDecimal::new);
        // primitives
        put(int.class, arg -> (int) Integer.parseInt(arg));
        put(long.class,  arg -> (long) Long.parseLong(arg));
        put(boolean.class, arg -> (boolean) Boolean.parseBoolean(arg));
        put(short.class, arg -> (short) Short.parseShort(arg));
        put(byte.class, arg -> (byte) Byte.parseByte(arg));
        put(char.class, arg -> arg.charAt(0));
        put(double.class, arg -> (double) Double.parseDouble(arg));
        put(float.class, arg -> (float) Float.parseFloat(arg));
    }};

    public static TypeConverterMap createDefaultConverterMap() {
        TypeConverterMap mapClone = new TypeConverterMap();
        for (Class<?> cls : map.keySet())
            mapClone.put(cls, createConverter(cls));
        return mapClone;
    }

    public static <T> TypeConverter<T> createConverter(Class<T> type) {
         return (TypeConverter<T>) map.get(type);
    }

}
