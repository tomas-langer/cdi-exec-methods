package jakarta.cdi.sandbox;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.RequestContextController;
import javax.inject.Inject;

@ApplicationScoped
class ScopeActivator {
    @Inject
    private RequestContextController controller;

    void startContext() {
        controller.activate();
    }

    void stopContext() {
        controller.deactivate();
    }
}
