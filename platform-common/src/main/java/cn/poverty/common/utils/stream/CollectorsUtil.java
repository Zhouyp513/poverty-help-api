package cn.poverty.common.utils.stream;

import org.apache.commons.lang.ArrayUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Collectors求和功能的自定义扩展
 
 * @date 创建时间 2018/6/6
 */
public class CollectorsUtil {


    static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

    private CollectorsUtil() {
    }

    /**
     *
     * @author qijiang @Email jiang1211@foxmail.com
     * @date 2018/10/15 下午4:55
     * @Param [arr]
     * @return java.util.List<T>
     * @Description 数组转List
     */
    public static final <T> List<T> arrayToList(T[] arr) {
        if (ArrayUtils.isEmpty(arr)) {
            return null;
        }
        return Arrays.asList(arr);
    }

    /**
     *
     * @author qijiang @Email jiang1211@foxmail.com
     * @date 2018/10/15 下午4:55
     * @Param [arr]
     * @return java.util.List<T>
     * @Description 数组转Set
     */
    public static final <T> Set<T> array2Set(T[] arr) {
         if (ArrayUtils.isEmpty(arr)) {
                 return null;
             }
         return new LinkedHashSet<T>(Arrays.asList(arr));
     }

    /**
     *
     * @author qijiang @Email jiang1211@foxmail.com
     * @date 2018/10/15 下午4:55
     * @Param [arr]
     * @return java.util.List<T>
     * @Description 集合转数组
     */
    public static final <T> T[] listtoArray(final Collection<T> collection, final Class<T> clazz) {
        if (collection == null) {
            return null;
        }
        final T[] arr = (T[]) Array.newInstance(clazz, collection.size());
        return collection.toArray(arr);
    }

    @SuppressWarnings("unchecked")
    private static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }

    /**
     * Simple implementation class for {@code Collector}.
     *
     * @param <T>
     *            the type of elements to be collected
     * @param <R>
     *            the type of the result
     */
    static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Characteristics> characteristics;

        CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner,
                      Function<A, R> finisher, Set<Characteristics> characteristics) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }

        CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner,
                      Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, castingIdentity(), characteristics);
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return accumulator;
        }

        @Override
        public Supplier<A> supplier() {
            return supplier;
        }

        @Override
        public BinaryOperator<A> combiner() {
            return combiner;
        }

        @Override
        public Function<A, R> finisher() {
            return finisher;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }
    }

    public static <T> Collector<T, ?, BigDecimal> summingBigDecimal(ToBigDecimalFunction<? super T> mapper) {
        return new CollectorImpl<>(() -> new BigDecimal[1], (a, t) -> {
            if (a[0] == null) {
                a[0] = BigDecimal.ZERO;
            }
            a[0] = a[0].add(mapper.applyAsBigDecimal(t));
        }, (a, b) -> {
            a[0] = a[0].add(b[0]);
            return a;
        }, a -> a[0], CH_NOID);
    }

}
