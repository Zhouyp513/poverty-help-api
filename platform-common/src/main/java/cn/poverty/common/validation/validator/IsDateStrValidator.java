package cn.poverty.common.validation.validator;


import cn.poverty.common.utils.common.CheckParam;
import cn.poverty.common.validation.IsDateStr;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 
 * @time 2017/11/20
 * @description
 */
public class IsDateStrValidator implements ConstraintValidator<IsDateStr,String> {


    @Override
    public void initialize(IsDateStr isDateStr) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return CheckParam.isDatetime(value);
    }
}
