package io.github.sudharsan_selvaraj.wowxhr;

import io.github.sudharsan_selvaraj.SpyDriver;
import io.github.sudharsan_selvaraj.wowxhr.exceptions.DriverNotSupportedException;
import io.github.sudharsan_selvaraj.wowxhr.log.Log;
import io.github.sudharsan_selvaraj.wowxhr.mock.Mock;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WowXHR<T extends WebDriver> {

    private T originalDriver;
    private SpyDriver<T> spyDriver;
    private final Mock mock;
    private final Log log;
    private final PageLoadManager pageLoadManager;

    public WowXHR(T driver) throws DriverNotSupportedException {
        this(new SpyDriver<T>(driver));
    }

    public WowXHR(SpyDriver<T> spyDriver) throws DriverNotSupportedException {
        initializeDriver(spyDriver.getWrappedDriver());
        this.spyDriver = spyDriver;
        WowXHRScriptExecutor<JavascriptExecutor> scriptExecutor = new WowXHRScriptExecutor<>((JavascriptExecutor) originalDriver);
        mock = new Mock(scriptExecutor);
        log = new Log(scriptExecutor);
        pageLoadManager = new PageLoadManager((RemoteWebDriver) spyDriver.getWrappedDriver(), scriptExecutor);
        spyDriver.addListener(scriptExecutor);
    }

    private void initializeDriver(T driver) throws DriverNotSupportedException {
        if (!(driver instanceof JavascriptExecutor)) {
            throw new DriverNotSupportedException(driver.getClass().getName());
        }
        originalDriver = driver;
    }

    public T getMockDriver() {
        return spyDriver.getSpyDriver();
    }

    public Mock mock() {
        return mock;
    }

    public Log log() {
        return log;
    }
}

