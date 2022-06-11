package cn.poverty.common.validation;



import cn.poverty.common.validation.validator.LengthValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 
 * @Title: Length
 * @ProjectName poverty-help-api
 * @Description: 长度注解
 * @date 2018/11/14 13:44
 */
@Documented
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LengthValidator.class)
public @interface Length {

    int max() default 1;

    int min() default 1;

    String message() default "not_null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
