package io.github.sudharsan_selvaraj.wowxhr.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sudharsan_selvaraj.wowxhr.DriverStateChangeListener;
import io.github.sudharsan_selvaraj.wowxhr.ScriptExecutor;

import java.util.*;
import java.util.stream.Collectors;

public class WowXHRMock {

    private ScriptExecutor scriptExecutor;
    private List<Mockable> mockableRequest;

    public WowXHRMock(ScriptExecutor scriptExecutor) {
        this.scriptExecutor = scriptExecutor;
        mockableRequest = new ArrayList<>();

        scriptExecutor.addStateChangeListener(getListener());
    }

    public void add(Mockable mockable) {
        mockableRequest.add(mockable);
        injectRequest(mockable);
    }

    private void injectRequest(Mockable mockableRequest) {
        injectRequest(Arrays.asList(mockableRequest));
    }

    private void injectRequest(List<Mockable> mockableRequests) {
        if (!mockableRequests.isEmpty()) {
            List<String> args = mockableRequests.stream().map(r -> convertMockableToJSON(r)).collect(Collectors.toList());
            scriptExecutor.executeScript("arguments[0].map(arg => wowXhr.mock.registerMock(JSON.parse(arg)))", args);
        }
    }

    private void onDriverStateChanged() {
        injectRequest(this.mockableRequest);
    }

    private String convertMockableToJSON(Mockable request) {
        Map<String, Object> mockParams = new HashMap<>();
        Map<String, Object> mockRequest = new HashMap<>();
        Map<String, Object> mockResponse = new HashMap<>();

        mockParams.put("url", request.getUrl());
        mockParams.put("method", request.getMethod());
        mockParams.put("queryParams", request.getQueryParam());

        if (request.getMockHttpRequest() != null) {
            mockRequest.put("headers", request.getMockHttpRequest().getHeaders());
            mockRequest.put("body", request.getMockHttpRequest().getBody());

            mockParams.put("mockRequest", mockRequest);
        }

        if (request.getMockHttpResponse() != null) {
            mockResponse.put("headers", request.getMockHttpResponse().getHeaders());
            mockResponse.put("body", request.getMockHttpResponse().getBody());
            mockResponse.put("status", request.getMockHttpResponse().getStatus());
            mockResponse.put("delay", request.getMockHttpResponse().getDelay());

            mockParams.put("mockResponse", mockResponse);
        }

        try {
            return new ObjectMapper().writeValueAsString(mockParams);
        } catch (Exception e) {
            return "{}";
        }
    }

    private DriverStateChangeListener getListener() {
        return new DriverStateChangeListener() {
            @Override
            public void onStateChanged() {
                onDriverStateChanged();
            }
        };
    }
}
