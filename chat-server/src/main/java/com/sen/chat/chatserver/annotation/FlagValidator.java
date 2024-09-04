package com.sen.chat.chatserver.annotation;

import com.sen.chat.chatserver.annotation.validator.FlagValidatorClass;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 用户验证状态是否在指定范围内的注解
 * <p>
 * eg: @FlagValidator(value = {"0","1"},message = "状态只能为0或1")
 * private Integer navStatus;
 *
 * @description:
 * @author: sensen
 * @date: 2023/8/26 13:30
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = FlagValidatorClass.class)
public @interface FlagValidator {
    String[] value() default {};

    String message() default "flag is not found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
