package com.cherry.cp.common.annota;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target({TYPE, FIELD})
@Retention(RUNTIME)
public @interface BeanReso {
	
	public String name() default "";
}
