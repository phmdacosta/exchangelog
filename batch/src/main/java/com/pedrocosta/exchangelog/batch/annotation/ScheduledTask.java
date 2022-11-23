package com.pedrocosta.exchangelog.batch.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ScheduledTask {
    String value() default "";
}
