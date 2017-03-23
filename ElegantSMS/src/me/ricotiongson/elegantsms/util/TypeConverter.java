package me.ricotiongson.elegantsms.util;

import java.lang.reflect.Parameter;

public class TypeConverter {

    public static Object convertParameter(String arg, Parameter param) {
        return convertType(arg, param.getType());
    }

    public static Object convertType(String arg, Class<?> type) {
        if (type.equals(String.class)) return arg;
        if (type.equals(int.class)) return Integer.parseInt(arg);
        if (type.equals(Integer.class)) return Integer.parseInt(arg);
        if (type.equals(long.class)) return Long.parseLong(arg);
        if (type.equals(Long.class)) return Long.parseLong(arg);
        if (type.equals(boolean.class)) return Boolean.parseBoolean(arg);
        if (type.equals(Boolean.class)) return Boolean.parseBoolean(arg);
        if (type.equals(short.class)) return Short.parseShort(arg);
        if (type.equals(Short.class)) return Short.parseShort(arg);
        if (type.equals(byte.class)) return Byte.parseByte(arg);
        if (type.equals(Byte.class)) return Byte.parseByte(arg);
        if (type.equals(char.class)) return arg.charAt(0);
        if (type.equals(Character.class)) return arg.charAt(0);
        return arg;
    }

    public static Object convertParameter(String[] arg, Parameter param) {
        return convertType(arg, param.getType());
    }

    public static Object convertType(String[] arg, Class<?> type) {
        if (type.equals(String[].class))
            return arg;
        if (type.equals(int[].class)) {
            int[] ans = new int[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = Integer.parseInt(arg[i]);
            return ans;
        }
        if (type.equals(Integer[].class)) {
            Integer[] ans = new Integer[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = Integer.parseInt(arg[i]);
            return ans;
        }

        if (type.equals(long[].class)) {
            long[] ans = new long[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = Long.parseLong(arg[i]);
            return ans;
        }

        if (type.equals(Long[].class)) {
            Long[] ans = new Long[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = Long.parseLong(arg[i]);
            return ans;
        }

        if (type.equals(Boolean[].class)) {
            Boolean[] ans = new Boolean[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = Boolean.parseBoolean(arg[i]);
            return ans;
        }

        if (type.equals(boolean[].class)) {
            boolean[] ans = new boolean[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = Boolean.parseBoolean(arg[i]);
            return ans;
        }

        if (type.equals(Short[].class)) {
            Short[] ans = new Short[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = Short.parseShort(arg[i]);
            return ans;
        }

        if (type.equals(short[].class)) {
            short[] ans = new short[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = Short.parseShort(arg[i]);
            return ans;
        }

        if (type.equals(Byte[].class)) {
            short[] ans = new short[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = Byte.parseByte(arg[i]);
            return ans;
        }

        if (type.equals(byte[].class)) {
            short[] ans = new short[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = Byte.parseByte(arg[i]);
            return ans;
        }

        return arg;
    }

}
