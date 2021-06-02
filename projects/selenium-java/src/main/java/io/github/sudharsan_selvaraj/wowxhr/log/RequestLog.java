package io.github.sudharsan_selvaraj.wowxhr.log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sudharsan_selvaraj.wowxhr.HttpMethod;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestLog {

    private HttpMethod method;

    private String url;

    private Object body;

    private Map<String, String> headers;

    @JsonProperty("type")
    private String responseType;

    private boolean withCredentials;

}