package com.patriclee.utils;

/**
 * @Author lyt
 * @Date 2025/9/18 下午4:00
 **/
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionUtils {
    public CollectionUtils() {
    }

    public static boolean containsAny(Object source, Object... targets) {
        return Arrays.asList(targets).contains(source);
    }

    public static boolean isAnyEmpty(Collection<?>... collections) {
        return Arrays.stream(collections).anyMatch(CollUtil::isEmpty);
    }

    public static <T> boolean anyMatch(Collection<T> from, Predicate<T> predicate) {
        return from.stream().anyMatch(predicate);
    }

    public static <T> List<T> filterList(Collection<T> from, Predicate<T> predicate) {
        return (List)(CollUtil.isEmpty(from) ? new ArrayList() : (List)from.stream().filter(predicate).collect(Collectors.toList()));
    }

    public static <T, R> List<T> distinct(Collection<T> from, Function<T, R> keyMapper) {
        return (List)(CollUtil.isEmpty(from) ? new ArrayList() : distinct(from, keyMapper, (t1, t2) -> {
            return t1;
        }));
    }

    public static <T, R> List<T> distinct(Collection<T> from, Function<T, R> keyMapper, BinaryOperator<T> cover) {
        return CollUtil.isEmpty(from) ? new ArrayList() : new ArrayList(convertMap(from, keyMapper, Function.identity(), cover).values());
    }

    public static <T, U> List<U> convertList(T[] from, Function<T, U> func) {
        return (List)(ArrayUtil.isEmpty(from) ? new ArrayList() : convertList((Collection)Arrays.asList(from), func));
    }

    public static <T, U> List<U> convertList(Collection<T> from, Function<T, U> func) {
        return (List)(CollUtil.isEmpty(from) ? new ArrayList() : (List)from.stream().map(func).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public static <T, U> List<U> convertList(Collection<T> from, Function<T, U> func, Predicate<T> filter) {
        return (List)(CollUtil.isEmpty(from) ? new ArrayList() : (List)from.stream().filter(filter).map(func).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public static <T, U> Page<U> convertPage(Page<T> from, Function<T, U> func) {
        if (from == null) {
            return new Page<>();
        }
        Page<U> to = new Page<>();
        to.setRecords(convertList(from.getRecords(), func));
        to.setTotal(from.getTotal());
        to.setSize(from.getSize());
        to.setCurrent(from.getCurrent());
        return to;
    }

    public static <T, U> List<U> convertListByFlatMap(Collection<T> from, Function<T, ? extends Stream<? extends U>> func) {
        return (List)(CollUtil.isEmpty(from) ? new ArrayList() : (List)from.stream().filter(Objects::nonNull).flatMap(func).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public static <T, U, R> List<R> convertListByFlatMap(Collection<T> from, Function<? super T, ? extends U> mapper, Function<U, ? extends Stream<? extends R>> func) {
        return (List)(CollUtil.isEmpty(from) ? new ArrayList() : (List)from.stream().map(mapper).filter(Objects::nonNull).flatMap(func).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public static <K, V> List<V> mergeValuesFromMap(Map<K, List<V>> map) {
        return (List)map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static <T> Set<T> convertSet(Collection<T> from) {
        return convertSet(from, (v) -> {
            return v;
        });
    }

    public static <T, U> Set<U> convertSet(Collection<T> from, Function<T, U> func) {
        return (Set)(CollUtil.isEmpty(from) ? new HashSet() : (Set)from.stream().map(func).filter(Objects::nonNull).collect(Collectors.toSet()));
    }

    public static <T, U> Set<U> convertSet(Collection<T> from, Function<T, U> func, Predicate<T> filter) {
        return (Set)(CollUtil.isEmpty(from) ? new HashSet() : (Set)from.stream().filter(filter).map(func).filter(Objects::nonNull).collect(Collectors.toSet()));
    }

    public static <T, K> Map<K, T> convertMapByFilter(Collection<T> from, Predicate<T> filter, Function<T, K> keyFunc) {
        return (Map)(CollUtil.isEmpty(from) ? new HashMap() : (Map)from.stream().filter(filter).collect(Collectors.toMap(keyFunc, (v) -> {
            return v;
        })));
    }

    public static <T, U> Set<U> convertSetByFlatMap(Collection<T> from, Function<T, ? extends Stream<? extends U>> func) {
        return (Set)(CollUtil.isEmpty(from) ? new HashSet() : (Set)from.stream().filter(Objects::nonNull).flatMap(func).filter(Objects::nonNull).collect(Collectors.toSet()));
    }

    public static <T, U, R> Set<R> convertSetByFlatMap(Collection<T> from, Function<? super T, ? extends U> mapper, Function<U, ? extends Stream<? extends R>> func) {
        return (Set)(CollUtil.isEmpty(from) ? new HashSet() : (Set)from.stream().map(mapper).filter(Objects::nonNull).flatMap(func).filter(Objects::nonNull).collect(Collectors.toSet()));
    }

    public static <T, K> Map<K, T> convertMap(Collection<T> from, Function<T, K> keyFunc) {
        return (Map)(CollUtil.isEmpty(from) ? new HashMap() : convertMap(from, keyFunc, Function.identity()));
    }

    public static <T, K> Map<K, T> convertMap(Collection<T> from, Function<T, K> keyFunc, Supplier<? extends Map<K, T>> supplier) {
        return CollUtil.isEmpty(from) ? (Map)supplier.get() : convertMap(from, keyFunc, Function.identity(), supplier);
    }

    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        return (Map)(CollUtil.isEmpty(from) ? new HashMap() : convertMap(from, keyFunc, valueFunc, (v1, v2) -> {
            return v1;
        }));
    }

    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc, BinaryOperator<V> mergeFunction) {
        return (Map)(CollUtil.isEmpty(from) ? new HashMap() : convertMap(from, keyFunc, valueFunc, mergeFunction, HashMap::new));
    }

    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc, Supplier<? extends Map<K, V>> supplier) {
        return CollUtil.isEmpty(from) ? (Map)supplier.get() : convertMap(from, keyFunc, valueFunc, (v1, v2) -> {
            return v1;
        }, supplier);
    }

    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc, BinaryOperator<V> mergeFunction, Supplier<? extends Map<K, V>> supplier) {
        return (Map)(CollUtil.isEmpty(from) ? new HashMap() : (Map)from.stream().collect(Collectors.toMap(keyFunc, valueFunc, mergeFunction, supplier)));
    }

    public static <T, K> Map<K, List<T>> convertMultiMap(Collection<T> from, Function<T, K> keyFunc) {
        return (Map)(CollUtil.isEmpty(from) ? new HashMap() : (Map)from.stream().collect(Collectors.groupingBy(keyFunc, Collectors.mapping((t) -> {
            return t;
        }, Collectors.toList()))));
    }

    public static <T, K, V> Map<K, List<V>> convertMultiMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        return (Map)(CollUtil.isEmpty(from) ? new HashMap() : (Map)from.stream().collect(Collectors.groupingBy(keyFunc, Collectors.mapping(valueFunc, Collectors.toList()))));
    }

    public static <T, K, V> Map<K, Set<V>> convertMultiMap2(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        return (Map)(CollUtil.isEmpty(from) ? new HashMap() : (Map)from.stream().collect(Collectors.groupingBy(keyFunc, Collectors.mapping(valueFunc, Collectors.toSet()))));
    }
//
//    public static <T, K> Map<K, T> convertImmutableMap(Collection<T> from, Function<T, K> keyFunc) {
//        if (CollUtil.isEmpty(from)) {
//            return Collections.emptyMap();
//        } else {
//            ImmutableMap.Builder<K, T> builder = ImmutableMap.builder();
//            from.forEach((item) -> {
//                builder.put(keyFunc.apply(item), item);
//            });
//            return builder.build();
//        }
//    }
//
//    public static String convertMap(Collection<String> from, CharSequence delimiter) {
//        return CollUtil.isEmpty(from) ? null : String.join(delimiter, from);
//    }
//
//    public static <T> List<List<T>> diffList(Collection<T> oldList, Collection<T> newList, BiFunction<T, T, Boolean> sameFunc) {
//        List<T> createList = new LinkedList(newList);
//        List<T> updateList = new ArrayList();
//        List<T> deleteList = new ArrayList();
//        Iterator var6 = oldList.iterator();
//
//        while(var6.hasNext()) {
//            T oldObj = var6.next();
//            T foundObj = null;
//            Iterator<T> iterator = createList.iterator();
//
//            while(iterator.hasNext()) {
//                T newObj = iterator.next();
//                if ((Boolean)sameFunc.apply(oldObj, newObj)) {
//                    iterator.remove();
//                    foundObj = newObj;
//                    break;
//                }
//            }
//
//            if (foundObj != null) {
//                updateList.add(foundObj);
//            } else {
//                deleteList.add(oldObj);
//            }
//        }
//
//        return Arrays.asList(createList, updateList, deleteList);
//    }
//
//    public static boolean containsAny(Collection<?> source, Collection<?> candidates) {
//        return org.springframework.util.CollectionUtils.containsAny(source, candidates);
//    }
//
//    public static <T> T getFirst(List<T> from) {
//        return !CollectionUtil.isEmpty(from) ? from.get(0) : null;
//    }
//
//    public static <T> T findFirst(Collection<T> from, Predicate<T> predicate) {
//        return findFirst(from, predicate, Function.identity());
//    }
//
//    public static <T, U> U findFirst(Collection<T> from, Predicate<T> predicate, Function<T, U> func) {
//        return CollUtil.isEmpty(from) ? null : from.stream().filter(predicate).findFirst().map(func).orElse((Object)null);
//    }
//
//    public static <T, V extends Comparable<? super V>> V getMaxValue(Collection<T> from, Function<T, V> valueFunc) {
//        if (CollUtil.isEmpty(from)) {
//            return null;
//        } else {
//            assert !from.isEmpty();
//
//            T t = from.stream().max(Comparator.comparing(valueFunc)).get();
//            return (Comparable)valueFunc.apply(t);
//        }
//    }
//
//    public static <T, V extends Comparable<? super V>> V getMinValue(List<T> from, Function<T, V> valueFunc) {
//        if (CollUtil.isEmpty(from)) {
//            return null;
//        } else {
//            assert from.size() > 0;
//
//            T t = from.stream().min(Comparator.comparing(valueFunc)).get();
//            return (Comparable)valueFunc.apply(t);
//        }
//    }
//
//    public static <T, V extends Comparable<? super V>> T getMinObject(List<T> from, Function<T, V> valueFunc) {
//        if (CollUtil.isEmpty(from)) {
//            return null;
//        } else {
//            assert from.size() > 0;
//
//            return from.stream().min(Comparator.comparing(valueFunc)).get();
//        }
//    }
//
//    public static <T, V extends Comparable<? super V>> V getSumValue(Collection<T> from, Function<T, V> valueFunc, BinaryOperator<V> accumulator) {
//        return getSumValue(from, valueFunc, accumulator, (Comparable)null);
//    }
//
//    public static <T, V extends Comparable<? super V>> V getSumValue(Collection<T> from, Function<T, V> valueFunc, BinaryOperator<V> accumulator, V defaultValue) {
//        if (CollUtil.isEmpty(from)) {
//            return defaultValue;
//        } else {
//            assert !from.isEmpty();
//
//            return (Comparable)from.stream().map(valueFunc).filter(Objects::nonNull).reduce(accumulator).orElse(defaultValue);
//        }
//    }
//
//    public static <T> void addIfNotNull(Collection<T> coll, T item) {
//        if (item != null) {
//            coll.add(item);
//        }
//    }

    public static <T> Collection<T> singleton(T obj) {
        return (Collection)(obj == null ? Collections.emptyList() : Collections.singleton(obj));
    }

    public static <T> List<T> newArrayList(List<List<T>> list) {
        return (List)list.stream().filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toList());
    }
}

