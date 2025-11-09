package com.patriclee.translation.core;

/**
 * 翻译接口
 *
 * @param <T> 翻译结果类型
 */
public interface TranslationInterface<T> {

    /**
     * 翻译
     *
     * @param key   需要被翻译的键(不为空)
     * @param other 其他参数
     * @return 返回键对应的值
     */
    T translation(Object key, String other);
}