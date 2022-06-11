package cn.poverty.common.utils.stream;

import java.math.BigDecimal;

/**
 * 函数式抽象接口
 
 * @date 创建时间 2018/6/6
 */
@FunctionalInterface
public interface ToBigDecimalFunction<T> {

    /**
     * applyAsBigDecimal
     * 
     * @date 2021/3/29
     * @param value 值
     * @return BigDecimal
     */
    BigDecimal applyAsBigDecimal(T value);
}
