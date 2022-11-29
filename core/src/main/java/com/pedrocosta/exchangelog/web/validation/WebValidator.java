package com.pedrocosta.exchangelog.web.validation;

import com.pedrocosta.exchangelog.annotation.Required;
import com.pedrocosta.springutils.output.Log;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WebValidator {
    public static boolean acceptParameters(Object ... params) {
        for (Object p : params) {
            if (p == null) {
                return false;
            }

            if (ObjectUtils.isEmpty(p)) {
                return false;
            }

            Class<?> clazz = params.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                Annotation annot = field.getAnnotation(Required.class);
                if (annot != null) {
                    try {
                        Method getMethod = clazz.getMethod("get" + StringUtils.capitalize(field.getName()));
                        Object value = getMethod.invoke(p);
                        if (ObjectUtils.isEmpty(value)) {
                            return false;
                        }
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        Log.error(WebValidator.class, "Could not check required field: " + clazz.getSimpleName() + "#" + field.getName());
                    }
                }
            }
        }

        return true;
    }
}
