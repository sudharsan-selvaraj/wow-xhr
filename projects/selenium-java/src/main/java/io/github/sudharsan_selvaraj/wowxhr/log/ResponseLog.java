package io.github.sudharsan_selvaraj.wowxhr.log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseLog {

    private Integer status;

    private String statusText;

    private String finalUrl;

    private String text;

    private Map<String, String> headers;

    @JsonProperty("data")
    private Object body;

}