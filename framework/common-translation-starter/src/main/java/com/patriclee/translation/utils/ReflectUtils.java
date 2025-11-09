package com.patriclee.translation.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 */
@Slf4j
public class ReflectUtils {

    /**
     * 调用getter方法获取字段值
     *
     * @param obj       目标对象
     * @param fieldName 字段名
     * @return 字段值
     */
    public static Object invokeGetter(Object obj, String fieldName) {
        if (obj == null || StrUtil.isBlank(fieldName)) {
            return null;
        }

        try {
            // 尝试直接访问字段
            Field field = getField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(obj);
            }

            // 尝试调用getter方法
            String getterName = "get" + StrUtil.upperFirst(fieldName);
            Method method = getMethod(obj.getClass(), getterName);
            if (method != null) {
                method.setAccessible(true);
                return method.invoke(obj);
            }

            // 尝试调用boolean字段的is方法
            String isMethodName = "is" + StrUtil.upperFirst(fieldName);
            Method isMethod = getMethod(obj.getClass(), isMethodName);
            if (isMethod != null) {
                isMethod.setAccessible(true);
                return isMethod.invoke(obj);
            }

        } catch (Exception e) {
            log.warn("Failed to invoke getter for field: {}", fieldName, e);
        }

        return null;
    }

    /**
     * 获取字段（包括父类）
     */
    private static Field getField(Class<?> clazz, String fieldName) {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 获取方法（包括父类）
     */
    private static Method getMethod(Class<?> clazz, String methodName) {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            try {
                return currentClass.getDeclaredMethod(methodName);
            } catch (NoSuchMethodException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }
}