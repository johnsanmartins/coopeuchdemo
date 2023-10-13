package com.example.springboot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Colum {

	String name();
	boolean defaul() default false;
	boolean updatedAt() default false;
	Type type() default Type.DEFAULT;
	boolean foreignKey() default false;
	
	public enum Type{
		DEFAULT,
		DOUBLE,
	}
	
}
