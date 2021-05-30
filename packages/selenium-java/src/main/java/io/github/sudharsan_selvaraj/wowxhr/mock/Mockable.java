package io.github.sudharsan_selvaraj.wowxhr.mock;

import io.github.sudharsan_selvaraj.wowxhr.HttpMethod;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Mockable {

    private HttpMethod method;
    private String url;
    private Map<String, String> queryParam;
    private MockHttpRequest mockHttpRequest;
    private MockHttpResponse mockHttpResponse;

    private Mockable(HttpMethod method, String url) {
        this.method = method;
        this.url = url;
        this.queryParam = new HashMap<>();
    }

    public static Mockable whenGET(String url) {
        return new Mockable(HttpMethod.GET, url);
    }

    public static Mockable whenPOST(String url) {
        return new Mockable(HttpMethod.POST, url);
    }

    public static Mockable whenPATCH(String url) {
        return new Mockable(HttpMethod.PATCH, url);
    }

    public static Mockable whenPUT(String url) {
        return new Mockable(HttpMethod.PUT, url);
    }

    public static Mockable whenDELETE(String url) {
        return new Mockable(HttpMethod.DELETE, url);
    }

    public static MockHttpRequest mockRequest() {
        return new MockHttpRequest();
    }

    public static MockHttpResponse mockResponse() {
        return new MockHttpResponse();
    }

    public Mockable withQueryParam(Map<String, String> params) {
        this.queryParam = params;
        return this;
    }

    public Mockable withQueryParam(String name, String value) {
        this.queryParam.put(name, value);
        return this;
    }

    public Mockable modifyRequest(MockHttpRequest mockHttpRequest) {
        this.mockHttpRequest = mockHttpRequest;
        return this;
    }

    public Mockable respond(MockHttpResponse mockHttpResponse) {
        this.mockHttpResponse = mockHttpResponse;
        return this;
    }
}
