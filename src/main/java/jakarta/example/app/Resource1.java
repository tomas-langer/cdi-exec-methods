package jakarta.example.app;

import jakarta.rest.GET;
import jakarta.rest.Path;
import jakarta.rest.QueryParam;
import jakarta.rest.RemoteAddress;

@Path("/resource1")
public class Resource1 {
    @GET
    public String getIt(@RemoteAddress String remoteAddress,
                        @QueryParam("message") String queryParam,
                        ConfigBean bean) {
        return "1: " +  bean.getMessage() + ", query: " + queryParam + ", remote address: " + remoteAddress;
    }
}
