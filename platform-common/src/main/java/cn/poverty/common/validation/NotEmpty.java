package cn.poverty.common.validation;


import cn.poverty.common.validation.validator.NotEmptyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 为空验证注解
 
 * @time 2017/11/17
 * @description 为空验证注解
 */
@Documented
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotEmptyValidator.class)
public @interface NotEmpty {



    String message() default "not_null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
