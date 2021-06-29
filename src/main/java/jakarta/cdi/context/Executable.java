package jakarta.cdi.context;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation/meta annotation to mark a method as executable.
 * Executable method can be invoked via the container and all its parameters are injected from
 * the current context.
 */
@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
@Inherited
public @interface Executable {
    /**
     * Whether this executable should be processed at startup.
     * This is needed for specifications that expect a setup (such as configuring
     * HTTP endpoints).
     *
     * @return whether this should be processed at startup, defaults to {@code false}
     */
    boolean process() default false;
}