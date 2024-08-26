package com.dev.zine.utils;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class NullAwareBeanUtilsBean extends BeanUtilsBean {

    @Override
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {

        if (value != null && isPrimitiveOrString(getPropertyType(dest, name))) {
            super.copyProperty(dest, name, value);
        }
    }

    private static boolean isPrimitiveOrString(Type type) {
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            return clazz.isPrimitive() || clazz.equals(String.class) || clazz.equals(Integer.class);
        }
        return false;
    }

    private static Type getPropertyType(Object bean, String propertyName) {
        try {
            return PropertyUtils.getPropertyType(bean, propertyName);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            // Handle potential exceptions gracefully (e.g., log the error)
            return null;
        }
    }
}
