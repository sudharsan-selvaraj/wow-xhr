package io.github.sudharsan_selvaraj.wowxhr.log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class XHRLog {

    @JsonProperty("_id")
    private String id;

    @JsonDeserialize(using = DateSerializer.class)
    private Date initiatedTime;

    @JsonDeserialize(using = DateSerializer.class)
    private Date completedTime;

    private RequestLog request;

    protected ResponseLog response;
}
