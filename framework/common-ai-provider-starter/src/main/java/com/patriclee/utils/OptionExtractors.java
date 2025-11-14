package com.patriclee.utils;

import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

/**
 * 工具方法：从覆写参数中提取不同类型的值.
 */
public final class OptionExtractors {

    private OptionExtractors() {
    }

    public static OptionalDouble extractDouble(Object value) {
        if (value instanceof Number number) {
            return OptionalDouble.of(number.doubleValue());
        }
        if (value instanceof String str && StringUtils.hasText(str)) {
            try {
                return OptionalDouble.of(Double.parseDouble(str));
            } catch (NumberFormatException ignored) {
            }
        }
        return OptionalDouble.empty();
    }

    public static OptionalInt extractInteger(Object value) {
        if (value instanceof Number number) {
            return OptionalInt.of(number.intValue());
        }
        if (value instanceof String str && StringUtils.hasText(str)) {
            try {
                return OptionalInt.of(Integer.parseInt(str));
            } catch (NumberFormatException ignored) {
            }
        }
        return OptionalInt.empty();
    }

    public static Optional<String> extractString(Object value) {
        if (value instanceof String str && StringUtils.hasText(str)) {
            return Optional.of(str);
        }
        return Optional.empty();
    }

    public static Optional<Boolean> extractBoolean(Object value) {
        if (value instanceof Boolean bool) {
            return Optional.of(bool);
        }
        if (value instanceof Number number) {
            return Optional.of(number.intValue() != 0);
        }
        if (value instanceof String str && StringUtils.hasText(str)) {
            if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)) {
                return Optional.of(Boolean.parseBoolean(str));
            }
            try {
                return Optional.of(Double.parseDouble(str) != 0);
            } catch (NumberFormatException ignored) {
            }
        }
        return Optional.empty();
    }
}
