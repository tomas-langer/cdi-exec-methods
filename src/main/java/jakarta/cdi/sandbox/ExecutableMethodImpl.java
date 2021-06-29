package jakarta.cdi.sandbox;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.CDI;

import jakarta.cdi.context.ExecutableMethod;

class ExecutableMethodImpl implements ExecutableMethod<Object, Object> {
    private final AnnotatedMethod<Object> delegate;

    ExecutableMethodImpl(AnnotatedMethod<?> method) {
        this.delegate = (AnnotatedMethod<Object>) method;
    }

    @Override
    public Object invoke(Object instance) throws InvocationTargetException, IllegalAccessException {
        Method javaMember = delegate.getJavaMember();
        List<AnnotatedParameter<Object>> paramDefs = delegate.getParameters();
        List<Object> parameters = findParameters(paramDefs);
        return javaMember.invoke(instance, parameters.toArray(new Object[0]));
    }

    @Override
    public <T extends Annotation> Set<T> getAnnotations(Class<T> annotationType) {
        return delegate.getAnnotations(annotationType);
    }

    @Override
    public Method getJavaMember() {
        return delegate.getJavaMember();
    }

    @Override
    public List<AnnotatedParameter<Object>> getParameters() {
        return delegate.getParameters();
    }

    @Override
    public boolean isStatic() {
        return delegate.isStatic();
    }

    @Override
    public AnnotatedType<Object> getDeclaringType() {
        return delegate.getDeclaringType();
    }

    @Override
    public Type getBaseType() {
        return delegate.getBaseType();
    }

    @Override
    public Set<Type> getTypeClosure() {
        return delegate.getTypeClosure();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return delegate.getAnnotation(annotationType);
    }

    @Override
    public Set<Annotation> getAnnotations() {
        return delegate.getAnnotations();
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        return delegate.isAnnotationPresent(annotationType);
    }

    private List<Object> findParameters(List<AnnotatedParameter<Object>> paramDefs) {
        CDI<Object> cdi = CDI.current();
        List<Object> result = new ArrayList<>(paramDefs.size());

        for (AnnotatedParameter<Object> paramDef : paramDefs) {
            Type type = paramDef.getBaseType();
            // just hardcoded to class for the sake of simplicity of this poc
            Object instance = cdi.select((Class)type, qualifiers(paramDef)).get();
            result.add(instance);
        }

        return result;
    }

    private Annotation[] qualifiers(AnnotatedParameter<Object> paramDef) {
        // hardcoded to all annotations
        return paramDef.getAnnotations()
                .toArray(new Annotation[0]);
    }
}
