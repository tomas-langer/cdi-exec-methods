package jakarta.rest;

import java.lang.annotation.Retention;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Qualifier
public @interface QueryParam {
    @Nonbinding
    String value();
}
