package io.quarkus.test.services.containers;

public class OpenShiftMssqlContainerManagedResource extends OpenShiftContainerManagedResource {

    private static final String DEPLOYMENT_TEMPLATE_PROPERTY_DEFAULT = "/mssql-deployment-template.yml";
    private final MssqlContainerManagedResourceBuilder model;

    protected OpenShiftMssqlContainerManagedResource(MssqlContainerManagedResourceBuilder model) {
        super(model);
        this.model = model;
    }

    @Override
    protected String getTemplateByDefault() {
        return DEPLOYMENT_TEMPLATE_PROPERTY_DEFAULT;
    }

    @Override
    protected boolean useInternalServiceByDefault() {
        return true;
    }

    @Override
    protected String getInternalServiceName() {
        return model.getContext().getName();
    }

    @Override
    protected void exposeService() {
        super.exposeService();
    }
}
