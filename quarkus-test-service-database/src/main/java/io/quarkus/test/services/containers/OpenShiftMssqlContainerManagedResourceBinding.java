package io.quarkus.test.services.containers;

import io.quarkus.test.bootstrap.ManagedResource;
import io.quarkus.test.scenarios.OpenShiftScenario;

public class OpenShiftMssqlContainerManagedResourceBinding implements MssqlContainerManagedResourceBinding {
    @Override
    public boolean appliesFor(MssqlContainerManagedResourceBuilder builder) {
        return builder.getContext().getTestContext().getRequiredTestClass().isAnnotationPresent(OpenShiftScenario.class);
    }

    @Override
    public ManagedResource init(MssqlContainerManagedResourceBuilder builder) {
        return new OpenShiftMssqlContainerManagedResource(builder);
    }
}
