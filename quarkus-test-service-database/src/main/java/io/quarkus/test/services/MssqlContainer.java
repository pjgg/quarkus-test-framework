package io.quarkus.test.services;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.quarkus.test.bootstrap.ManagedResourceBuilder;
import io.quarkus.test.services.containers.MssqlContainerManagedResourceBuilder;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MssqlContainer {
    String image() default "mcr.microsoft.com/mssql/rhel/server:2019-latest";

    int port() default 1433;

    String expectedLog() default "Service Broker manager has started";

    Class<? extends ManagedResourceBuilder> builder() default MssqlContainerManagedResourceBuilder.class;
}
