package io.github.sudharsan_selvaraj.wowxhr.mock;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MockHttpRequest {

    private Map<String, String> headers;
    private Map<String, Object> queryParam;
    private String body;

    public MockHttpRequest() {
        headers = new HashMap<>();
        queryParam = new HashMap<>();
    }

    public static MockHttpRequest mockRequest() {
        return new MockHttpRequest();
    }

    public MockHttpRequest setHeader(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public MockHttpRequest setHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    public MockHttpRequest setQueryParam(String name, Object value) {
        this.queryParam.put(name, value);
        return this;
    }

    public MockHttpRequest setQueryParam(Map<String, Object> queryParam) {
        this.queryParam.putAll(queryParam);
        return this;
    }

    public MockHttpRequest setBody(String body) {
        this.body = body;
        return this;
    }
}
