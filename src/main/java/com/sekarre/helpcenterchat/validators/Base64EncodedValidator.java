package com.sekarre.helpcenterchat.validators;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Base64EncodedValidator implements ConstraintValidator<Base64Encoded, String> {

    private boolean nullAllowed;
    private static final Pattern DATA_URL_PATTERN = Pattern.compile("^data:image/(.+?);base64,\\s*", Pattern.CASE_INSENSITIVE);

    @Override
    public void initialize(Base64Encoded base64Encoded) {
        this.nullAllowed = base64Encoded.nullAllowed();
    }

    @Override
    public boolean isValid(String fieldValue, ConstraintValidatorContext context) {
        if (Objects.isNull(fieldValue) && nullAllowed) {
            return true;
        }
        if (fieldValue.startsWith("data:")) {
            final Matcher m = DATA_URL_PATTERN.matcher(fieldValue);
            return m.find();
        }
        return false;
    }
}
