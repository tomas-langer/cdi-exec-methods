package jakarta.example.app;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ConfigBean {
    @Inject
    @ConfigProperty(name = "app.message")
    private String message;

    public String getMessage() {
        return message;
    }
}
