package me.ricotiongson.elegantsms.dispatch;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.regex.Matcher;

import me.ricotiongson.elegantsms.annotations.BindByName;
import me.ricotiongson.elegantsms.annotations.BindByOrder;
import me.ricotiongson.elegantsms.annotations.BindParam;
import me.ricotiongson.elegantsms.framework.SmsModule;

public class DispatchMethodByName extends DispatchMethod {

    static {
        // register this dispatcher
        DispatchMethod.registerDispatcher(BindByName.class, DispatchMethodByName.class);
    }

    protected Integer[] identifierPositions;

    public DispatchMethodByName(SmsModule module, Method method) {
        super(module, method);
        identifierPositions = new Integer[identifierParams.length];
        Arrays.fill(identifierPositions, null);
        for (int i = 0; i < identifierPositions.length; ++i) {
            String paramName = identifierParams[i].getName();
            BindParam bindParam = identifierParams[i].getDeclaredAnnotation(BindParam.class);
            if (bindParam != null)
                paramName = bindParam.value();
            for (int j = 0; j < identifiers.length; ++j) {
                if (paramName.equals(identifiers[j])) {
                    identifierPositions[i] = j;
                    break;
                }
            }
        }
    }

    public String dispatch(String message) throws SmsPatternMismatchError {

        // run through the pattern, otherwise throw an error if Pattern does not match
        Matcher matcher = pattern.matcher(message);
        if (!matcher.matches())
            throw new SmsPatternMismatchError("message does not match Pattern");

        // dynamically construct arguments
        Object[] args = new Object[identifierParams.length];
        Arrays.fill(args, null);

        // iterate by name
        for (int i = 0; i < args.length; ++i) {
            if (identifierPositions[i] != null) {
                args[i] = matcher.group(identifierPositions[i] + 1);
            }
        }

        // invoke method
        try {
            return (String) method.invoke(module, args);
        } catch (Exception e) {
            throw new SmsPatternMismatchError(e.getMessage());
        }

    }
}
