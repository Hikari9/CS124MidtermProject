package me.ricotiongson.elegantsms.util;

import java.lang.reflect.Parameter;

public class TypeConverter {

    public static Object convertParameter(String arg, Parameter param) {
        return convertType(arg, param.getType());
    }

    public static Object convertType(String arg, Class<?> type) {
        if (type.equals(String.class))      return arg;
        if (type.equals(int.class))         return (int) Integer.parseInt(arg);
        if (type.equals(Integer.class))     return (Integer) Integer.parseInt(arg);
        if (type.equals(long.class))        return (long) Long.parseLong(arg);
        if (type.equals(Long.class))        return (Long) Long.parseLong(arg);
        if (type.equals(boolean.class))     return (boolean) Boolean.parseBoolean(arg);
        if (type.equals(Boolean.class))     return (Boolean) Boolean.parseBoolean(arg);
        if (type.equals(short.class))       return (short) Short.parseShort(arg);
        if (type.equals(Short.class))       return (Short) Short.parseShort(arg);
        if (type.equals(byte.class))        return (byte) Byte.parseByte(arg);
        if (type.equals(Byte.class))        return (Byte) Byte.parseByte(arg);
        if (type.equals(char.class))        return (char) arg.charAt(0);
        if (type.equals(Character.class))   return (Character) arg.charAt(0);
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
                ans[i] = (int) Integer.parseInt(arg[i]);
            return ans;
        }
        if (type.equals(Integer[].class)) {
            Integer[] ans = new Integer[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = Integer.parseInt(arg[i]);
            return ans;
        }

        if (type.equals(long[].class)){
            long[] ans = new long[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = (long) Long.parseLong(arg[i]);
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

        if (type.equals(boolean[].class))         {
            boolean[] ans = new boolean[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = (boolean) Boolean.parseBoolean(arg[i]);
            return ans;
        }

        if (type.equals(Short[].class)) {
            Short[] ans = new Short[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = Short.parseShort(arg[i]);
            return ans;
        }

        if (type.equals(short[].class))         {
            short[] ans = new short[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = (short) Short.parseShort(arg[i]);
            return ans;
        }

        if (type.equals(Byte[].class))         {
            short[] ans = new short[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = (byte) Byte.parseByte(arg[i]);
            return ans;
        }

        if (type.equals(byte[].class))         {
            short[] ans = new short[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = (byte) Byte.parseByte(arg[i]);
            return ans;
        }

        return arg;
    }

}
