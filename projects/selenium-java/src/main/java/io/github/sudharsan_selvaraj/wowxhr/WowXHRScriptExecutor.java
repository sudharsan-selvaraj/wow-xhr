package io.github.sudharsan_selvaraj.wowxhr;

import io.github.sudharsan_selvaraj.SpyDriverListener;
import io.github.sudharsan_selvaraj.types.driver.DriverCommand;
import io.github.sudharsan_selvaraj.types.driver.DriverCommandException;
import io.github.sudharsan_selvaraj.types.driver.DriverCommandResult;
import io.github.sudharsan_selvaraj.types.element.ElementCommand;
import io.github.sudharsan_selvaraj.types.element.ElementCommandException;
import io.github.sudharsan_selvaraj.types.element.ElementCommandResult;
import org.openqa.selenium.JavascriptExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class WowXHRScriptExecutor<T extends JavascriptExecutor>
        implements ScriptExecutor, SpyDriverListener {

    private final T javaScriptExecutor;
    private final List<DriverStateChangeListener> stateChangeListener = new ArrayList<>();
    private static final String wowXhr;

    static {
        wowXhr = readScriptFromResource("/js/wowxhr.js");
    }

    public WowXHRScriptExecutor(T javaScriptExecutor) {
        this.javaScriptExecutor = javaScriptExecutor;
        injectMock();
    }

    @Override
    public void beforeDriverCommandExecuted(DriverCommand command) {
    }

    @Override
    public void afterDriverCommandExecuted(DriverCommandResult command) {
        if (command.getMethod().getName()
                .matches("get|to|refresh|forward|back|executeScript|executeScriptAsync|perform")) {
            onDriverStateChanged();
        }
    }

    @Override
    public void onException(DriverCommandException command) {
    }

    @Override
    public void beforeElementCommandExecuted(ElementCommand command) {
    }

    @Override
    public void afterElementCommandExecuted(ElementCommandResult command) {
        if (command.getMethod().getName().matches("click")) {
            onDriverStateChanged();
        }
    }

    @Override
    public void onException(ElementCommandException command) {
    }

    @Override
    public Object executeScript(String script, Object... args) {
        return javaScriptExecutor.executeScript(script, args);
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return javaScriptExecutor.executeAsyncScript(script, args);
    }

    @Override
    public void addStateChangeListener(DriverStateChangeListener listener) {
        this.stateChangeListener.add(listener);
    }

    private void onDriverStateChanged() {
        notifyStateChange(injectMock());
    }

    private void notifyStateChange(Boolean scriptInjected) {
        this.stateChangeListener.forEach(l -> l.onStateChanged(scriptInjected));
    }

    private static String readScriptFromResource(String fileName) {
        return new Scanner(Objects.requireNonNull(WowXHRScriptExecutor.class.getResourceAsStream(fileName)), "UTF-8").useDelimiter("\\A").next();
    }

    public Boolean injectMock() {
        Boolean isScriptAlreadyInjected = (Boolean) executeScript("return !!window.xhook");
        if (!isScriptAlreadyInjected) {
            executeScript(wowXhr);
            return true;
        } else {
            return false;
        }
    }
}
