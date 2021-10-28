package io.quarkus.test;

import static io.quarkus.test.utils.AwaitilityUtils.untilAsserted;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.bootstrap.QuarkusCliClient;
import io.quarkus.test.bootstrap.QuarkusCliRestService;
import io.quarkus.test.scenarios.QuarkusScenario;
import io.quarkus.test.scenarios.annotations.DisabledOnQuarkusVersion;

@Tag("quarkus-cli")
@QuarkusScenario
@DisabledOnQuarkusVersion(version = "1\\..*", reason = "Quarkus CLI has been reworked in 2.x")
public class QuarkusCliClientIT {

    static final String RESTEASY_EXTENSION = "quarkus-resteasy";
    static final String SMALLRYE_HEALTH_EXTENSION = "quarkus-smallrye-health";
    static final String SMALLRYE_OPENAPI = "quarkus-smallrye-openapi";

    @Inject
    static QuarkusCliClient cliClient;

    @Test
    public void shouldCreateApplicationOnJvm() {
        // Create application
        QuarkusCliRestService app = cliClient.createApplication("app");

        // Should build on Jvm
        QuarkusCliClient.Result result = app.buildOnJvm();
        assertTrue(result.isSuccessful(), "The application didn't build on JVM. Output: " + result.getOutput());

        // Start using DEV mode
        app.start();
        app.given().get().then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldAddAndRemoveExtensions() {
        // Create application
        QuarkusCliRestService app = cliClient.createApplication("app");

        // By default, it installs only "quarkus-resteasy"
        assertInstalledExtensions(app, RESTEASY_EXTENSION);

        // Let's install Quarkus Smallrye Health
        QuarkusCliClient.Result result = app.installExtension(SMALLRYE_HEALTH_EXTENSION);
        assertTrue(result.isSuccessful(), SMALLRYE_HEALTH_EXTENSION + " was not installed. Output: " + result.getOutput());

        // Verify both extensions now
        assertInstalledExtensions(app, RESTEASY_EXTENSION, SMALLRYE_HEALTH_EXTENSION);

        // The health endpoint should be now available
        app.start();
        untilAsserted(() -> app.given().get("/q/health").then().statusCode(HttpStatus.SC_OK));
        app.stop();

        // Let's now remove the Smallrye Health extension
        result = app.removeExtension(SMALLRYE_HEALTH_EXTENSION);
        assertTrue(result.isSuccessful(), SMALLRYE_HEALTH_EXTENSION + " was not uninstalled. Output: " + result.getOutput());

        // The health endpoint should be now gone
        app.start();
        untilAsserted(() -> app.given().get("/q/health").then().statusCode(HttpStatus.SC_NOT_FOUND));
    }

    @Test
    @Disabled
    //TODO https://github.com/quarkusio/quarkus/issues/21070
    public void shouldReStartAppAfterRemoveExtension() {
        // Create application
        QuarkusCliRestService app = cliClient.createApplication("app");

        // By default, it installs only "quarkus-resteasy"
        assertInstalledExtensions(app, RESTEASY_EXTENSION);

        // Let's install Quarkus Smallrye OpenAPI
        app.installExtension(SMALLRYE_OPENAPI);

        // Verify both extensions now
        assertInstalledExtensions(app, RESTEASY_EXTENSION, SMALLRYE_OPENAPI);

        app.start();
        untilAsserted(() -> app.given().get("/q/dev").then().statusCode(HttpStatus.SC_OK));
        app.stop();

        // Let's now remove the Smallrye OpenAPI extension
        app.removeExtension(SMALLRYE_OPENAPI);

        app.start();
        untilAsserted(() -> app.given().get("/q/dev").then().statusCode(HttpStatus.SC_OK));
    }

    private void assertInstalledExtensions(QuarkusCliRestService app, String... expectedExtensions) {
        List<String> extensions = app.getInstalledExtensions();
        Stream.of(expectedExtensions).forEach(expectedExtension -> assertTrue(extensions.contains(expectedExtension),
                expectedExtension + " not found in " + extensions));
    }
}
