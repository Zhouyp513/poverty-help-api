package cn.poverty.common.validation;


import cn.poverty.common.validation.validator.IsDateStrValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


/**

 * @packageName cn.poverty.common.validation
 * @Description: 日期字符串验证注解
 * @date 2021-01-21
 */
@Documented
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsDateStrValidator.class)
public @interface IsDateStr {

    String message() default "必须为日期格式的字符串";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
