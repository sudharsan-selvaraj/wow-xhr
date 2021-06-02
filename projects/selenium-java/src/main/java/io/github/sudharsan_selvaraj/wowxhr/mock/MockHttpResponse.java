package io.github.sudharsan_selvaraj.wowxhr.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MockHttpResponse {

    private Map<String, String> headers;
    private String body;
    private Integer delay;
    private Integer status;

    public MockHttpResponse() {
        this.headers = new HashMap<>();
    }

    public MockHttpResponse withHeader(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public MockHttpResponse withHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    public MockHttpResponse withStatus(Integer status) {
        this.status = status;
        return this;
    }

    public MockHttpResponse withBody(String body) {
        this.body = body;
        return this;
    }

    public MockHttpResponse withBody(Object body) {
        try {
            this.body = new ObjectMapper().writeValueAsString(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public MockHttpResponse withDelay(Integer delayInSeconds) {
        this.delay = delayInSeconds;
        return this;
    }

}