package io.quarkus.test.services;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DevModeQuarkusApplication {
    // By default, it will load all the classes in the classpath.
    Class<?>[] classes() default {};

    /**
     * @return the properties file to use to configure the Quarkus application.
     */
    String properties() default "application.properties";

    /**
     * Enable GRPC configuration. This property will map the gPRC service to a random port.
     */
    boolean grpc() default false;

    /**
     * Enable SSL configuration. This property needs `quarkus.http.ssl.certificate.key-store-file` and
     * `quarkus.http.ssl.certificate.key-store-password` to be set.
     */
    boolean ssl() default false;
}
