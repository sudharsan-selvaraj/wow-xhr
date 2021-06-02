package io.github.sudharsan_selvaraj.wowxhr;

import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

public class PageLoadManager implements DriverStateChangeListener {

    private final PageLoadStrategy strategy;
    private final RemoteWebDriver webDriver;
    private final ScriptExecutor scriptExecutor;

    public PageLoadManager(RemoteWebDriver webDriver, ScriptExecutor executor) {
        this.webDriver = webDriver;
        strategy = getPageLoadStrategy(webDriver);
        this.scriptExecutor = executor;
        executor.addStateChangeListener(this);
    }

    @Override
    public void onStateChanged(Boolean isScriptInjected) {
        try {
            if (!strategy.equals(PageLoadStrategy.NORMAL)) {
                FluentWait<WebDriver> wait = new FluentWait<WebDriver>(webDriver)
                        .pollingEvery(Duration.ofMillis(100))
                        .withTimeout(Duration.ofSeconds(60));
                wait.until(new ExpectedCondition<Boolean>() {
                    @NullableDecl
                    @Override
                    public Boolean apply(@NullableDecl WebDriver input) {
                        return (Boolean) scriptExecutor.executeScript(ScriptProvider.getFunction("waitForPageLoad"));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PageLoadStrategy getPageLoadStrategy(RemoteWebDriver driver) {
        Object strategy = driver.getCapabilities().getCapability("pageLoadStrategy");
        if (strategy != null) {
            return PageLoadStrategy.valueOf(strategy.toString().toUpperCase());
        } else {
            return PageLoadStrategy.NORMAL;
        }
    }
}
