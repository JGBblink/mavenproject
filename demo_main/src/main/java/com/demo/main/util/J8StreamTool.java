package com.demo.main.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 列表工具类。
 */
public final class J8StreamTool {

    public static void main(String[] args) {
        System.out.println(J8StreamTool.extract(Arrays.asList(1, 2, "a", 3, "c", "d", 4), e -> "object_" + e));
        System.out.println(J8StreamTool.extract(Arrays.asList(1, 2, "a", 3, "c", "d", 4), e -> e instanceof Number, e -> "number_" + e));

        System.out.println(J8StreamTool.toMap(Arrays.asList(1, 2, "a", 3, "c", "d", 4), e -> "M" + e));
        System.out.println(J8StreamTool.toMap(Arrays.asList(1, 2, "a", 3, "c", "d", 4), e -> "M" + e, e -> e));

        System.out.println(J8StreamTool.groupingBy(Arrays.asList(1, 2, "a", 3, "c", "d", 4), e -> "M" + e));
        System.out.println(J8StreamTool.groupingBy(Arrays.asList(1, 2, "a", 3, "c", "d", 4), e -> "M" + e, e -> "v_" + e));

        // List<SharedSessionDto> sessionsDto = sharedSessionProxy.findSessionsByGroupingIds(context, seasonIds);
        // Map<Long, List<Long>> ids = sessionsDto.stream().collect(Collectors.groupingBy(SharedSessionDto::getGroupingId, Collectors.mapping(SharedSessionDto::getId, Collectors.toList())));

    }

    //---------- list tools -------------

    /**
     * 提取
     *
     * @param source
     * @param extractor
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> extract(List<T> source, Function<T, R> extractor) {
        return Optional.ofNullable(source).orElseGet(Collections::emptyList).stream()
                .map(extractor).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    /**
     * 提取并过滤
     *
     * @param source
     * @param filter
     * @param extractor
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> extract(List<T> source, Predicate<T> filter, Function<T, R> extractor) {
        return Optional.ofNullable(source).orElseGet(Collections::emptyList).stream().filter(filter)
                .map(extractor).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    /**
     * 转换成map
     *
     * @param list
     * @param key
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> toMap(List<V> list, Function<V, K> key) {
        return Optional.ofNullable(list).orElseGet(Collections::emptyList).stream()
                .collect(Collectors.toMap(key, Function.identity(), (v, v2) -> v2));
    }

    /**
     * 转换成map
     *
     * @param list
     * @param key
     * @param value
     * @param <K>
     * @param <V>
     * @param <T>
     * @return
     */
    public static <K, V, T> Map<K, V> toMap(List<T> list, Function<T, K> key, Function<T, V> value) {
        return Optional.ofNullable(list).orElseGet(Collections::emptyList).stream()
                .collect(Collectors.toMap(key, value, (v, v2) -> v2));
    }

    /**
     * 分组
     *
     * @param list
     * @param key
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, List<V>> groupingBy(List<V> list, Function<V, K> key) {
        return Optional.ofNullable(list).orElseGet(Collections::emptyList).stream()
                .collect(Collectors.groupingBy(key));
    }

    /**
     * 分组并转换
     *
     * @param list
     * @param key
     * @param value
     * @param <K>
     * @param <V>
     * @param <T>
     * @return
     */
    public static <K, V, T> Map<K, List<V>> groupingBy(List<T> list, Function<T, K> key, Function<T, V> value) {
        return Optional.ofNullable(list).orElseGet(Collections::emptyList).stream()
                .collect(Collectors.groupingBy(key, Collectors.mapping(value, Collectors.toList())));
    }

    public static <T> List<T> toList(Iterator<T> iterator) {
        List<T> list = new ArrayList<>();
        if (iterator == null) {
            return list;
        }
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public static <T, R extends Comparable> Optional<R> min(Collection<T> list, Function<T, R> function) {
        return list.stream().filter(Objects::nonNull).map(function).filter(Objects::nonNull)
                .min(Comparator.comparing(Function.identity()));
    }

    public static <T, R extends Comparable> Optional<R> max(Collection<T> list, Function<T, R> function) {
        return list.stream().filter(Objects::nonNull).map(function).filter(Objects::nonNull)
                .max(Comparator.comparing(Function.identity()));
    }

    /**
     * 合并
     *
     * @param fromList
     * @param function
     * @param <F>
     * @param <R>
     * @return
     */
    public static <F, R> List<R> merge(List<F> fromList, Function<? super F, List<? extends R>> function) {
        return fromList.stream().flatMap(item -> function.apply(item).stream()).collect(Collectors.toList());
    }

    //---------- list tools end-------------

}
