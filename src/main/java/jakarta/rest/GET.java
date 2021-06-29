package jakarta.rest;

import java.lang.annotation.Retention;

import jakarta.cdi.context.Executable;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Executable(process = true)
@Retention(RUNTIME)
public @interface GET {
}
