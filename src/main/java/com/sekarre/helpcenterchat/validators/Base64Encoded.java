package com.sekarre.helpcenterchat.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = Base64EncodedValidator.class)
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Base64Encoded {

    String message() default "Field is not properly encoded!";

    boolean nullAllowed() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
