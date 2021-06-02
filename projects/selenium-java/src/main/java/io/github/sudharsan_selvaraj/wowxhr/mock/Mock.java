package io.github.sudharsan_selvaraj.wowxhr.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sudharsan_selvaraj.wowxhr.*;

import java.util.*;
import java.util.stream.Collectors;

public class Mock extends JsExecutable {

    private final List<Mockable> mockableRequest;

    public Mock(ScriptExecutor scriptExecutor) {
        super(scriptExecutor);
        mockableRequest = new ArrayList<>();

        scriptExecutor.addStateChangeListener(getListener());
    }

    public Mock add(Mockable mockable) {
        mockableRequest.add(mockable);
        injectRequest(mockable);
        return this;
    }

    public void pause() {
        executeScript("(function(){"+ ScriptProvider.getFunction("pauseMocking") +"})()");
    }

    public void resume() {
        executeScript("(function(){"+ ScriptProvider.getFunction("resumeMocking") +"})()");
    }

    private void injectRequest(Mockable mockableRequest) {
        injectRequest(Collections.singletonList(mockableRequest));
    }

    private void injectRequest(List<Mockable> mockableRequests) {
        if (!mockableRequests.isEmpty()) {
            System.out.println("Injecting mock requests");
            List<String> args = mockableRequests.stream().map(this::convertMockableToJSON).collect(Collectors.toList());
            executeScript("(function(newMocks){"+ ScriptProvider.getFunction("injectMock") +"})(arguments[0])", args);
        }
    }

    private void onDriverStateChanged() {
        injectRequest(this.mockableRequest);
    }

    private String convertMockableToJSON(Mockable request) {

        Map<String, Object> mockRequest = new HashMap(){{
            put("headers", request.getMockHttpRequest().getHeaders());
            put("body", request.getMockHttpRequest().getBody());
            put("queryParams", request.getMockHttpRequest().getQueryParam());
        }};

        Map<String, Object> mockResponse = new HashMap() {{
            put("headers", request.getMockHttpResponse().getHeaders());
            put("body", request.getMockHttpResponse().getBody());
            put("status", request.getMockHttpResponse().getStatus());
            put("delay", request.getMockHttpResponse().getDelay());
        }};

        Map<String, Object> payload = new HashMap<String, Object>() {{
            put("id", request.getId());
            put("url", request.getUrl());
            put("method", request.getMethod());
            put("queryParams", request.getQueryParam());
            put("mockRequest", mockRequest);
            put("mockResponse", mockResponse);
        }};

        try {
            return new ObjectMapper().writeValueAsString(payload);
        } catch (Exception e) {
            return "{}";
        }
    }

    private DriverStateChangeListener getListener() {
        return new DriverStateChangeListener() {
            @Override
            public void onStateChanged(Boolean scriptInjected) {
                if(scriptInjected) {
                    onDriverStateChanged();
                }
            }
        };
    }
}
