package com.sekarre.helpcenterchat.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AtLeastOneFieldNotEmptyValidator.class)
@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneFieldNotEmpty {

    String message() default "Fields values are empty!";

    String[] fields() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}