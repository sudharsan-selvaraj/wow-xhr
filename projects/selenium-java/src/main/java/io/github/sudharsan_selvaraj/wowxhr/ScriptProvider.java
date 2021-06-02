package io.github.sudharsan_selvaraj.wowxhr;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptProvider {
    protected static String wowXhrScript, utilFunctions;
    protected static final Map<String, String> functions = new HashMap<>();

    static {
        wowXhrScript = readScriptFromResource("/js/wowxhr.js");
        utilFunctions = readScriptFromResource("/js/scripts.js");
        iterateOverJsFunctionsInSource(utilFunctions);
        functions.put("wowxhr", wowXhrScript);
    }

    public static String getFunction(String functionName) {
        return functions.get(functionName);
    }

    private static String readScriptFromResource(String fileName) {
        return new Scanner(Objects.requireNonNull(ScriptProvider.class.getResourceAsStream(fileName)), "UTF-8").useDelimiter("\\A").next();
    }

    private static void iterateOverJsFunctionsInSource(String src) {
        Pattern ps = Pattern.compile("^function.* \\{$", Pattern.MULTILINE);
        Pattern pe = Pattern.compile("^\\}", Pattern.MULTILINE);
        Matcher m = ps.matcher(src);
        boolean more = true;
        while (more && m.find()) {
            src = src.substring(m.start());
            Matcher m2 = pe.matcher(src);
            if (m2.find()) {
                String body = src.substring(0, m2.start());
                storeJavaScriptFunction(body);
                src = src.substring(body.length());
                m = ps.matcher(src);
            } else {
                more = false;
            }
        }
    }

    private static void storeJavaScriptFunction(String body) {
        Pattern regFn = Pattern.compile("^function ([a-zA-Z0-9]+)\\(", Pattern.MULTILINE);
        Matcher m = regFn.matcher(body);
        String fnName;
        if (m.find()) {
            fnName = m.group(1);
        } else {
            Pattern fnPro = Pattern.compile("^functions\\.([a-zA-Z0-9]+) ", Pattern.MULTILINE);
            Matcher m2 = fnPro.matcher(body);
            if (m2.find()) {
                fnName = m2.group(1);
            } else {
                return;
            }
        }
        functions.put(fnName, body.substring(body.indexOf("{") + 1));
    }
}
