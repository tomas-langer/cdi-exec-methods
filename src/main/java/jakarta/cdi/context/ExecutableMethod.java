package jakarta.cdi.context;

import java.lang.reflect.InvocationTargetException;

import javax.enterprise.inject.spi.AnnotatedMethod;

public interface ExecutableMethod<T, R> extends AnnotatedMethod<T> {
    R invoke(T instance) throws InvocationTargetException, IllegalAccessException;
}
