package net.serenitybdd.core.webdriver.enhancers;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.util.EnvironmentVariables;

public class DefaultWebDriverManagerEnhancer implements WebDriverManagerEnhancer {

    private final EnvironmentVariables environmentVariables;

    public DefaultWebDriverManagerEnhancer() {
        this.environmentVariables = Injectors.getInjector().getInstance(EnvironmentVariables.class);
    }

    private boolean useDocker() {
        return Boolean.parseBoolean(
                EnvironmentSpecificConfiguration.from(environmentVariables).getOptionalProperty("webdrivermanager.use.docker").orElse("false")
        );
    }

    @Override
    public WebDriverManager apply(WebDriverManager webDriverManager) {
        if (useDocker()) {
            return webDriverManager.browserInDocker();
        } else {
            return webDriverManager;
        }
    }
}
