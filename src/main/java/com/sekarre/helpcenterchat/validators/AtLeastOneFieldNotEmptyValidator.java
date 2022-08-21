package com.sekarre.helpcenterchat.validators;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

@Slf4j
public class AtLeastOneFieldNotEmptyValidator implements ConstraintValidator<AtLeastOneFieldNotEmpty, Object> {

    private String[] fields;

    @Override
    public void initialize(AtLeastOneFieldNotEmpty atLeastOneFieldNotEmpty) {
        this.fields = atLeastOneFieldNotEmpty.fields();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean isValid = false;
        for (String fieldToCheck : fields) {
            Object fieldToCheckValue = new BeanWrapperImpl(value).getPropertyValue(fieldToCheck);
            if (Objects.nonNull(fieldToCheckValue) && StringUtils.isNotBlank(fieldToCheckValue.toString())) {
                isValid = true;
            }
        }
        return isValid;
    }
}