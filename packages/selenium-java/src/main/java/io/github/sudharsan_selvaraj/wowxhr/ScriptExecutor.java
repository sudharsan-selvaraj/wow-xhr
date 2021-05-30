package io.github.sudharsan_selvaraj.wowxhr;

public interface ScriptExecutor {

    public Object executeScript(String script, Object... args);

    public Object executeAsyncScript(String script, Object... args);

    public void addStateChangeListener(DriverStateChangeListener listener);
}
