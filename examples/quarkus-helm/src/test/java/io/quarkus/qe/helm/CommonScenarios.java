package io.quarkus.qe.helm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import io.quarkus.test.bootstrap.QuarkusHelmClient;

public abstract class CommonScenarios {

    protected abstract QuarkusHelmClient getHelmClient();

    @Test
    public void shouldInstallQuarkusAppThroughHelm() {
        QuarkusHelmClient helmClient = getHelmClient();
        String chartName = "examples-quarkus-helm";
        String chartFolderName = helmClient.getWorkingDirectory().getAbsolutePath() + "/helm/" + chartName;
        QuarkusHelmClient.Result chartResultCmd = helmClient.installChart(chartName, chartFolderName);
        thenSucceed(chartResultCmd);

        List<QuarkusHelmClient.ChartListResult> charts = helmClient.getCharts();
        assertTrue(charts.size() > 0, "Chart " + chartName + " not found. Installation fail");
        List<String> chartNames = charts.stream()
                .map(QuarkusHelmClient.ChartListResult::getName)
                .map(String::trim)
                .collect(Collectors.toList());
        assertThat(chartNames.toArray(), hasItemInArray(chartName));
    }

    @Test
    public void shouldInstallNewEmptyHelmChartManually() {
        QuarkusHelmClient helmClient = getHelmClient();
        String chartName = "mychart-manually";
        String chartFolderName = helmClient.getWorkingDirectory().getAbsolutePath() + "/" + chartName;
        QuarkusHelmClient.Result chartResultCmd = helmClient.run("create", chartName);
        thenSucceed(chartResultCmd);

        chartResultCmd = helmClient.run("install", chartName, chartFolderName);
        thenSucceed(chartResultCmd);
    }

    @Test
    public void shouldInstallNewEmptyHelmChartWithShortcuts() {
        QuarkusHelmClient helmClient = getHelmClient();
        String chartName = "mychart-shortcuts";
        QuarkusHelmClient.NewChartResult newChartResult = helmClient.createEmptyChart(chartName);
        thenSucceed(newChartResult);

        QuarkusHelmClient.Result chartResultCmd = helmClient.installChart(chartName, newChartResult.getChartFolderPath());
        thenSucceed(chartResultCmd);

        List<QuarkusHelmClient.ChartListResult> charts = helmClient.getCharts();
        assertTrue(charts.size() > 0, "Chart " + chartName + " not found. Installation fail");
        List<String> chartNames = charts.stream()
                .map(QuarkusHelmClient.ChartListResult::getName)
                .map(String::trim)
                .collect(Collectors.toList());
        assertThat(chartNames.toArray(), hasItemInArray(chartName));
    }

    @Test
    public void shouldUninstallHelmChart() {
        QuarkusHelmClient helmClient = getHelmClient();
        String chartName = "mychart-remove";
        QuarkusHelmClient.NewChartResult newChartResult = helmClient.createEmptyChart(chartName);
        thenSucceed(newChartResult);

        QuarkusHelmClient.Result chartResultCmd = helmClient.installChart(chartName, newChartResult.getChartFolderPath());
        thenSucceed(chartResultCmd);
        List<QuarkusHelmClient.ChartListResult> charts = helmClient.getCharts();
        List<String> chartNames = charts.stream()
                .map(QuarkusHelmClient.ChartListResult::getName)
                .map(String::trim)
                .collect(Collectors.toList());
        assertThat(chartNames.toArray(), hasItemInArray(chartName));

        chartResultCmd = helmClient.uninstallChart(chartName);
        thenSucceed(chartResultCmd);
        charts = helmClient.getCharts();
        chartNames = charts.stream().map(QuarkusHelmClient.ChartListResult::getName).map(String::trim)
                .collect(Collectors.toList());
        assertThat(chartNames, Matchers.not(contains(chartName)));
    }

    private void thenSucceed(QuarkusHelmClient.Result chartResultCmd) {
        assertTrue(
                chartResultCmd.isSuccessful(),
                String.format("Command %s fails", chartResultCmd.getCommandExecuted()));
    }
}
