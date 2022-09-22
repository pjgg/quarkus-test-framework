package io.quarkus.test.services.containers;

import org.testcontainers.containers.GenericContainer;

import io.quarkus.test.utils.DockerUtils;

public class MssqlGenericDockerContainerManagedResource extends GenericDockerContainerManagedResource {

    private final MssqlContainerManagedResourceBuilder model;

    protected MssqlGenericDockerContainerManagedResource(MssqlContainerManagedResourceBuilder model) {
        super(model);
        this.model = model;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    protected GenericContainer<?> initContainer() {
        GenericContainer<?> container = super.initContainer();
        container.addExposedPort(model.getPort());
        container.withCreateContainerCmdModifier(cmd -> cmd.withName(DockerUtils.generateDockerContainerName()));
        return container;
    }

}
