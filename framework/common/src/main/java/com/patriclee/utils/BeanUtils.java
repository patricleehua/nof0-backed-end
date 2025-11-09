package com.patriclee.utils;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.patriclee.req.PageResult;

import java.util.List;
import java.util.function.Consumer;

/**
 * @Author lyt
 * @Date 2025/9/18 下午3:51
 **/
public class BeanUtils {
    public BeanUtils() {
    }

    /**
     * 将源对象转换为目标类型的对象
     *
     * @param source      源对象
     * @param targetClass 目标类
     * @param <T>         目标类型
     * @return 转换后的目标对象
     */
    public static <T> T toBean(Object source, Class<T> targetClass) {
        return BeanUtil.toBean(source, targetClass);
    }

    /**
     * 将源对象转换为目标类型的对象，并执行额外操作
     *
     * @param source      源对象
     * @param targetClass 目标类
     * @param peek        额外操作
     * @param <T>         目标类型
     * @return 转换后的目标对象
     */
    public static <T> T toBean(Object source, Class<T> targetClass, Consumer<T> peek) {
        T target = toBean(source, targetClass);
        if (target != null) {
            peek.accept(target);
        }
        return target;
    }

    /**
     * 将源对象列表转换为目标类型的对象列表
     *
     * @param source     源对象列表
     * @param targetType 目标类
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 转换后的目标对象列表
     */
    public static <S, T> List<T> toBean(List<S> source, Class<T> targetType) {
        return source == null ? null : CollectionUtils.convertList(source, (s) -> {
            return toBean(s, targetType);
        });
    }

    /**
     * 将源对象列表转换为目标类型的对象列表，并对每个元素执行额外操作
     *
     * @param source     源对象列表
     * @param targetType 目标类
     * @param peek       额外操作
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 转换后的目标对象列表
     */
    public static <S, T> List<T> toBean(List<S> source, Class<T> targetType, Consumer<T> peek) {
        List<T> list = toBean(source, targetType);
        if (list != null) {
            list.forEach(peek);
        }
        return list;
    }

    /**
     * 将源对象列表转换为目标类型的对象列表（支持集合接口）
     *
     * @param source     源对象集合
     * @param targetType 目标类
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 转换后的目标对象列表
     */
    public static <S, T> List<T> toBeanList(java.util.Collection<S> source, Class<T> targetType) {
        return source == null ? null : CollectionUtils.convertList(source, (s) -> {
            return toBean(s, targetType);
        });
    }

    /**
     * 将源对象列表转换为目标类型的对象列表，并对每个元素执行额外操作（支持集合接口）
     *
     * @param source     源对象集合
     * @param targetType 目标类
     * @param peek       额外操作
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 转换后的目标对象列表
     */
    public static <S, T> List<T> toBeanList(java.util.Collection<S> source, Class<T> targetType, Consumer<T> peek) {
        List<T> list = toBeanList(source, targetType);
        if (list != null) {
            list.forEach(peek);
        }
        return list;
    }

    /**
     * 将分页结果转换为目标类型的分页结果
     *
     * @param source     源分页结果
     * @param targetType 目标类
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 转换后的分页结果
     */
    public static <S, T> PageResult<T> toBean(Page<S> source, Class<T> targetType) {
        return toBean(source, targetType, (Consumer<T>) null);
    }

    /**
     * 将分页结果转换为目标类型的分页结果，并对每个元素执行额外操作
     *
     * @param source     源分页结果
     * @param targetType 目标类
     * @param peek       额外操作
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 转换后的分页结果
     */
    public static <S, T> PageResult<T> toBean(Page<S> source, Class<T> targetType, Consumer<T> peek) {
        if (source == null) {
            return null;
        } else {
            List<T> list = toBean(source.getRecords(), targetType);
            if (peek != null && list != null) {
                list.forEach(peek);
            }
            PageResult<T> result = new PageResult<>();
            result.setRecords(list);
            result.setTotal(source.getTotal());
            result.setCurrent((int) source.getCurrent());
            result.setSize((int) source.getSize());
            result.setTotalPage((int) source.getPages());
            return result;
        }
    }

    /**
     * 复制对象属性
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) {
        if (source != null && target != null) {
            BeanUtil.copyProperties(source, target, false);
        }
    }

    /**
     * 复制对象属性（可忽略指定属性）
     *
     * @param source           源对象
     * @param target           目标对象
     * @param ignoreProperties 忽略的属性名
     */
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        if (source != null && target != null) {
            BeanUtil.copyProperties(source, target, ignoreProperties);
        }
    }

    /**
     * 批量复制对象属性
     *
     * @param sources 源对象列表
     * @param targets 目标对象列表
     * @param <S>     源类型
     * @param <T>     目标类型
     */
    public static <S, T> void copyProperties(List<S> sources, List<T> targets) {
        if (sources != null && targets != null && sources.size() == targets.size()) {
            for (int i = 0; i < sources.size(); i++) {
                copyProperties(sources.get(i), targets.get(i));
            }
        }
    }

    /**
     * 批量复制对象属性（可忽略指定属性）
     *
     * @param sources          源对象列表
     * @param targets          目标对象列表
     * @param ignoreProperties 忽略的属性名
     * @param <S>              源类型
     * @param <T>              目标类型
     */
    public static <S, T> void copyProperties(List<S> sources, List<T> targets, String... ignoreProperties) {
        if (sources != null && targets != null && sources.size() == targets.size()) {
            for (int i = 0; i < sources.size(); i++) {
                copyProperties(sources.get(i), targets.get(i), ignoreProperties);
            }
        }
    }
}
