package jakarta.cdi.inject.spi;

import jakarta.cdi.context.ExecutableMethod;

public interface ProcessExecutableMethod<T, R> {
    ExecutableMethod<T, R> method();
}
