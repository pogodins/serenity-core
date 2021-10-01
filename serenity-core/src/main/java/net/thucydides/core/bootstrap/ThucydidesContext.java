package net.thucydides.core.bootstrap;

import net.serenitybdd.core.SerenityListeners;
import net.serenitybdd.core.environment.ConfiguredEnvironment;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.reports.ReportService;
import net.thucydides.core.steps.*;
import net.thucydides.core.webdriver.Configuration;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import net.thucydides.core.webdriver.WebdriverManager;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * Container holding thread-local data related to a Thucydides test run.
 * This includes the StepFactory and the associated step listeners. These need to be thread-local so that,
 * if tests are run in parallel in different threads, the step listeners will still build up correct result trees
 * and report data.
 */
class ThucydidesContext {
    private static final ThreadLocal<ThucydidesContext> contextThreadLocal = new ThreadLocal<ThucydidesContext>();

    /**
     * Instruments step libraries to that any @Step-annotated methods called will appear in the test reports.
     */
    private final StepFactory stepFactory;

    /**
     * The main step listener used to record test results and outcomes.
     */
    private BaseStepListener stepListener;

    /**
     * Generates reports once the test outcomes have been recorded and built.
     */
    private final ReportService reportService;

    /**
     * Where are the Thucydides reports written to.
     * Normally defined in the system properties.
     */
    private File outputDirectory;

    private String defaultDriver;

    private Pages pages;

    /**
     * Thucydides configuration data
     */
    private Configuration configuration;

    private WebdriverManager webdriverManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(ThucydidesContext.class);

    private ThucydidesContext(StepListener... additionalListeners) {
        this(null, additionalListeners);
    }

    private ThucydidesContext(String defaultDriver, StepListener... additionalListeners) {
        configuration = ConfiguredEnvironment.getConfiguration();
        LOGGER.info("System configuration: " + configuration);
        webdriverManager = ThucydidesWebDriverSupport.getWebdriverManager();
        outputDirectory = configuration.getOutputDirectory();
        LOGGER.info("Output directory: " + outputDirectory);
        this.defaultDriver = defaultDriver;
        if (defaultDriver != null) {
            pages =  new Pages(getDriver());
            stepFactory = StepFactory.getFactory().usingPages(pages);
        } else {
            stepFactory = StepFactory.getFactory();
        }
        LOGGER.info("Additional listeners: " + additionalListeners);
        registerStepListeners(additionalListeners);
        reportService = new ReportService(outputDirectory,
                ReportService.getDefaultReporters());
    }

    protected WebDriver getDriver() {
        return webdriverManager.getWebdriver(defaultDriver);
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    private void registerStepListeners(StepListener... additionalListeners) {
        stepListener = buildBaseStepListener();
        StepEventBus.getEventBus().registerListener(stepListener);
        for (StepListener listener : additionalListeners) {
            StepEventBus.getEventBus().registerListener(listener);
        }
    }

    public static ThucydidesContext newContext() {
        return new ThucydidesContext();
    }

    public static ThucydidesContext newContext(Optional<String> driver, StepListener... listeners) {
        ThucydidesContext context = null;
        context = (driver.isPresent()) ? new ThucydidesContext(driver.get(), listeners) : new ThucydidesContext(listeners);
        contextThreadLocal.set(context);
        return context;
    }

    public static ThucydidesContext getCurrentContext() {
        return contextThreadLocal.get();
    }

    /**
     * Injects instrumented step classes into any @Step annotated fields of the specified class.
     *
     * @param testCase
     */
    public void initialize(Object testCase) {
        StepAnnotations.injector().injectScenarioStepsInto(testCase, stepFactory);
    }


    public void generateReports() {
        reportService.generateReportsFor(latestTestOutcomes());
        reportService.generateConfigurationsReport();
    }

    private List<TestOutcome> latestTestOutcomes() {
        return stepListener.getTestOutcomes();
    }

    public void dropListeners() {
        StepEventBus.getEventBus().dropAllListeners();
    }


    private BaseStepListener buildBaseStepListener() {
        return Listeners.getBaseStepListener().withOutputDirectory(outputDirectory);
    }
}
