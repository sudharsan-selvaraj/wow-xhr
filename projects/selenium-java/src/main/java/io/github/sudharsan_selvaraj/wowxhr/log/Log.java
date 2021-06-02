package io.github.sudharsan_selvaraj.wowxhr.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sudharsan_selvaraj.wowxhr.JsExecutable;
import io.github.sudharsan_selvaraj.wowxhr.ScriptExecutor;
import io.github.sudharsan_selvaraj.wowxhr.ScriptProvider;

import java.util.ArrayList;
import java.util.List;

public class Log extends JsExecutable {

    public Log(ScriptExecutor scriptExecutor) {
        super(scriptExecutor);
    }

    public void clearLogs() {
        executeScript(ScriptProvider.getFunction("clearLogs"));
    }

    public List<XHRLog> getXHRLogs() {
        return getXHRLogs(true);
    }

    public List<XHRLog> getXHRLogs(Boolean clearExisting) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.convertValue(executeScript("return (function(clearLogs){" +
                            ScriptProvider.getFunction("getLogs")
                            + "})" + "(arguments[0])",
                    clearExisting),
                    mapper.getTypeFactory().constructCollectionType(List.class, XHRLog.class));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
