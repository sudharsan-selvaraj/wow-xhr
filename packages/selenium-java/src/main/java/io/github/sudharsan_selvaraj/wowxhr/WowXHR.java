package io.github.sudharsan_selvaraj.wowxhr;

import io.github.sudharsan_selvaraj.SpyDriver;
import io.github.sudharsan_selvaraj.SpyDriverOptions;
import io.github.sudharsan_selvaraj.wowxhr.exceptions.DriverNotSupportedException;
import io.github.sudharsan_selvaraj.wowxhr.mock.WowXHRMock;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class WowXHR<T extends WebDriver> {

    private T originalDriver;
    private T mockDriver;
    private WowXHRScriptExecutor scriptExecutor;
    private WowXHRMock mock;


    public WowXHR(T driver) throws DriverNotSupportedException {
        initializeDriver(driver);
        scriptExecutor = new WowXHRScriptExecutor<>((JavascriptExecutor) driver);
        mockDriver = SpyDriver.spyOn(originalDriver, SpyDriverOptions.builder().listener(scriptExecutor).build());
        mock = new WowXHRMock(scriptExecutor);
    }

    private void initializeDriver(T driver) throws DriverNotSupportedException {
        if (!(driver instanceof JavascriptExecutor)) {
            throw new DriverNotSupportedException(driver.getClass().getName());
        }
        originalDriver = driver;
    }

    public T getMockDriver() {
        return mockDriver;
    }

    public WowXHRMock mock() {
        return mock;
    }
}

