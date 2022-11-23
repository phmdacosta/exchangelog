package com.pedrocosta.exchangelog.batch.annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ScheduledTask {
    String value() default "";
}
