package cz.mg.resampler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *  Annotation for resampler methods that represents configurable properties.
 *  The methods shall begin with either "get", "set" or "is".
 *  The rest of the method body is considered to be the property name.
 *  There should always be a pair of corresponing get/is and set methods in the resampler class (having the same property name).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Parameter {
    int order() default 0;
}
