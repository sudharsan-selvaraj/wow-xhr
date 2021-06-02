package io.github.sudharsan_selvaraj.wowxhr;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JsExecutable {

    private ScriptExecutor executor;

    protected Object executeScript(String string, Object... args) {
        return executor.executeScript(string, args);
    }

    protected Object executeAsyncScript(String string, Object... args) {
        return executor.executeAsyncScript(string, args);
    }
}
