package io.quarkus.test.services.containers;

import java.lang.annotation.Annotation;
import java.util.ServiceLoader;

import io.quarkus.test.bootstrap.ManagedResource;
import io.quarkus.test.bootstrap.ServiceContext;
import io.quarkus.test.services.MssqlContainer;

public class MssqlContainerManagedResourceBuilder extends ContainerManagedResourceBuilder {
    private final ServiceLoader<MssqlContainerManagedResourceBinding> managedResourceBindingsRegistry = ServiceLoader
            .load(MssqlContainerManagedResourceBinding.class);

    private ServiceContext context;
    private String image;
    private int port;
    private String expectedLog;

    @Override
    protected String getImage() {
        return image;
    }

    @Override
    protected Integer getPort() {
        return port;
    }

    @Override
    protected ServiceContext getContext() {
        return context;
    }

    @Override
    protected String getExpectedLog() {
        return expectedLog;
    }

    @Override
    public void init(Annotation annotation) {
        MssqlContainer metadata = (MssqlContainer) annotation;
        this.image = metadata.image();
        this.port = metadata.port();
        this.expectedLog = metadata.expectedLog();
    }

    @Override
    public ManagedResource build(ServiceContext context) {
        this.context = context;
        for (MssqlContainerManagedResourceBinding binding : managedResourceBindingsRegistry) {
            if (binding.appliesFor(this)) {
                return binding.init(this);
            }
        }

        return new MssqlGenericDockerContainerManagedResource(this);
    }
}
