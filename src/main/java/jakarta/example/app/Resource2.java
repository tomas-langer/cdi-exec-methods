package jakarta.example.app;

import jakarta.rest.GET;
import jakarta.rest.Path;
import jakarta.rest.QueryParam;
import jakarta.rest.RemoteAddress;

@Path("/resource2")
public class Resource2 {
    @GET
    public String getIt(@RemoteAddress String remoteAddress,
                        @QueryParam("message") String queryParam,
                        ConfigBean bean) {
        return "2: " + bean.getMessage() + ", query: " + queryParam + ", remote address: " + remoteAddress;
    }
}
