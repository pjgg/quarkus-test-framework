package io.quarkus.qe.helm;

import javax.inject.Inject;

import io.quarkus.test.bootstrap.QuarkusHelmClient;
import io.quarkus.test.scenarios.KubernetesScenario;

@KubernetesScenario
public class KubernetesQuarkusHelmClientIT extends CommonScenarios {

    @Inject
    static QuarkusHelmClient helmClient;

    @Override
    protected QuarkusHelmClient getHelmClient() {
        return helmClient;
    }
}
