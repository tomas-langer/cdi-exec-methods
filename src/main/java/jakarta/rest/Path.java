package jakarta.rest;

import java.lang.annotation.Retention;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Stereotype;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Stereotype
@RequestScoped
@Retention(RUNTIME)
public @interface Path {
    String value();
}
