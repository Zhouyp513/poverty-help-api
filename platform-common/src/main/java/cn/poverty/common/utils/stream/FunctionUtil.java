package cn.poverty.common.utils.stream;

import java.util.function.Function;

/**

 * @Title: FunctionUtil
 * @ProjectName poverty-help-api
 * @Description: 函数式编程工具类
 * @date 2018/11/8 10:51
 */
public class FunctionUtil {

    public static void main(String[] args) {
        Function<Integer, Integer> name = e -> e * 2;
        Function<Integer, Integer> square = e -> e * e;
        int value = name.andThen(square).apply(3);
        System.out.println("andThen value=" + value);
        int value2 = name.compose(square).apply(3);
        System.out.println("compose value2=" + value2);
        //返回一个执行了apply()方法之后只会返回输入参数的函数对象
        Object identity = Function.identity().apply("huohuo");
        System.out.println(identity);



        Function<Integer,Integer> f1 = a1 -> a1 * 3;
        Function<Integer,Integer> f2 = a1 -> a1 * a1;

        //apply的作用是把输出参数作用到函数上面
        System.out.println(f1.apply(3));
        //先执行f1 再执行 f2 81
        System.out.println(f1.andThen(f2).apply(3));
        //先f2 再执行f1 27
        System.out.println(f1.compose(f2).apply(3));
    }
}
