package io.quarkus.qe.helm;

import javax.inject.Inject;

import io.quarkus.test.bootstrap.QuarkusHelmClient;
import io.quarkus.test.scenarios.OpenShiftScenario;

@OpenShiftScenario
public class OpenShiftQuarkusHelmClientIT extends CommonScenarios {

    @Inject
    static QuarkusHelmClient helmClient;

    @Override
    protected QuarkusHelmClient getHelmClient() {
        return helmClient;
    }
}
