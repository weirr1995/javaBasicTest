package map;

import sun.reflect.annotation.AnnotationType;

import java.lang.annotation.*;

/**
 * @author linjing
 * @date: Created in 2020/8/27
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SourceField {
    String name() ;
}
