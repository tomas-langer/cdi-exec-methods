package jakarta.cdi.sandbox;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;

import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;

import jakarta.cdi.context.ExecutableMethod;
import jakarta.cdi.inject.spi.ProcessExecutableMethod;
import jakarta.rest.GET;
import jakarta.rest.Path;

import static javax.interceptor.Interceptor.Priority.PLATFORM_AFTER;

public class RestExtension implements Extension {
    private final Map<String, MethodData> endpoints = new HashMap<>();
    private WebServer webServer;

    /**
     * This method would not be needed.
     * @param pat
     */
    void processAnnotatedType(@Observes @WithAnnotations(Path.class) ProcessAnnotatedType<?> pat) {
        // faking ProcessExecutableMethod
        AnnotatedType<?> annotatedType = pat.getAnnotatedType();
        Set<AnnotatedMethod<?>> methods = (Set<AnnotatedMethod<?>>) annotatedType.getMethods();
        for (AnnotatedMethod<?> method : methods) {
            if (method.getAnnotation(GET.class) != null) {
                processGets(new ProcessExecutableMethod<Object, Object>() {
                    @Override
                    public ExecutableMethod<Object, Object> method() {
                        return new ExecutableMethodImpl(method);
                    }
                });
            }
        }
    }

    /**
     * This is the process executable method handling to set up endpoints.
     * @param pem
     */
    @SuppressWarnings("unchecked")
    void processGets(/*@Observes @WithAnnotations(GET.class)*/ ProcessExecutableMethod<?, ?> pem) {
        ExecutableMethod<?, ?> method = pem.method();

        AnnotatedType<?> declaringType = method.getDeclaringType();
        String path = declaringType
                .getAnnotation(Path.class)
                .value();

        endpoints.put(path, new MethodData(declaringType.getJavaClass(), (ExecutableMethod<Object, Object>) method));
    }

    void start(@Observes @Priority(PLATFORM_AFTER + 42) @Initialized(ApplicationScoped.class) Object event) {
        webServer = WebServer.builder()
                .routing(createRouting())
                .port(7001)
                .build()
                .start()
                .await();
    }

    void stop(@Observes @Priority(PLATFORM_AFTER + 42) @Destroyed(ApplicationScoped.class) Object event) {
        webServer.shutdown()
                .await();
    }

    /**
     * Processes collected executable methods and exposes them through a server
     * @return
     */
    private Routing createRouting() {
        Routing.Builder builder = Routing.builder();

        endpoints.forEach((path, methodData) -> {
            builder.get(path, (req, res) -> {
                CDI<Object> cdi = CDI.current();

                // this should run in an executor, blocking for sake of example
                ScopeActivator scopeActivator = cdi.select(ScopeActivator.class)
                        .get();

                scopeActivator.startContext();

                cdi.select(RequestDataProducer.class)
                        .get()
                        .setData(req, res);

                String result = null;
                try {
                    result = (String) methodData.method.invoke(cdi.select(methodData.type).get());
                } catch (Exception e) {
                    res.send(e);
                }

                res.send(result);

                scopeActivator.stopContext();
            });
        });

        return builder.build();
    }

    private final static class MethodData {
        private final Class<?> type;
        private final ExecutableMethod<Object, Object> method;

        public MethodData(Class<?> type, ExecutableMethod<Object, Object> method) {
            this.type = type;
            this.method = method;
        }
    }
}
