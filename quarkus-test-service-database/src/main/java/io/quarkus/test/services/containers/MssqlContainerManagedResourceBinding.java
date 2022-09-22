package io.quarkus.test.services.containers;

import io.quarkus.test.bootstrap.ManagedResource;

public interface MssqlContainerManagedResourceBinding {
    boolean appliesFor(MssqlContainerManagedResourceBuilder builder);

    ManagedResource init(MssqlContainerManagedResourceBuilder builder);
}
