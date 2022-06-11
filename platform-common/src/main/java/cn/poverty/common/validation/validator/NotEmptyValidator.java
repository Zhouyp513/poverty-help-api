package cn.poverty.common.validation.validator;

import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.common.validation.NotEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 
 * @time 2017/11/17
 * @description 验证器
 */
public class NotEmptyValidator implements ConstraintValidator<NotEmpty,Object>{


    @Override
    public void initialize(NotEmpty notEmpty) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {


        return !CheckParam.isNull(value);
    }

}
