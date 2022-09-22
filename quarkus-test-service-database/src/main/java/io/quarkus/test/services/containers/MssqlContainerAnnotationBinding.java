package io.quarkus.test.services.containers;

import java.lang.reflect.Field;

import io.quarkus.test.bootstrap.AnnotationBinding;
import io.quarkus.test.bootstrap.ManagedResourceBuilder;
import io.quarkus.test.services.MssqlContainer;

public class MssqlContainerAnnotationBinding implements AnnotationBinding {
    @Override
    public boolean isFor(Field field) {
        return field.isAnnotationPresent(MssqlContainer.class);
    }

    @Override
    public ManagedResourceBuilder createBuilder(Field field) throws Exception {
        MssqlContainer metadata = field.getAnnotation(MssqlContainer.class);

        ManagedResourceBuilder builder = metadata.builder().getDeclaredConstructor().newInstance();
        builder.init(metadata);
        return builder;
    }
}
