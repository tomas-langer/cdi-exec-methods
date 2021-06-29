package jakarta.cdi.sandbox;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;

import jakarta.rest.QueryParam;
import jakarta.rest.RemoteAddress;

@RequestScoped
public class RequestDataProducer {
    private ServerRequest req;
    private ServerResponse res;

    void setData(ServerRequest req, ServerResponse res) {
        this.req = req;
        this.res = res;
    }

    @Dependent
    @Produces
    @RemoteAddress
    String remoteAddress() {
        return req.remoteAddress();
    }

    @Dependent
    @Produces
    @QueryParam("")
    String queryParam(InjectionPoint ip) {
        // this just returns the whole query, as I do not know
        Set<Annotation> qualifiers = ip.getQualifiers();
        for (Annotation qualifier : qualifiers) {
            if (qualifier.annotationType().equals(QueryParam.class)) {
                QueryParam param = (QueryParam) qualifier;
                return req.queryParams().first(param.value()).orElse(null);
            }
        }
        return null;
    }
}
