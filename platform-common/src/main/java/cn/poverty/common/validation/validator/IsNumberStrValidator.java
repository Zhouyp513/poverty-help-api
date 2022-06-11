package cn.poverty.common.validation.validator;

import cn.poverty.common.validation.IsNumberStr;
import cn.poverty.common.utils.common.CheckParam;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 
 * @time 2017/11/20
 * @description
 */
public class IsNumberStrValidator implements ConstraintValidator<IsNumberStr,String> {


    @Override
    public void initialize(IsNumberStr isNumberStr) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return CheckParam.isNum(value);
    }
}
