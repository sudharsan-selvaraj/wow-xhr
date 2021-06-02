package io.github.sudharsan_selvaraj.wowxhr.log;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Date;

public class DateSerializer  extends StdDeserializer<Date> {

    public DateSerializer(){
        super(Date.class);
    }

    protected DateSerializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        try {
            return new Date(p.getLongValue());
        } catch (Exception e ){

        }
        return null;
    }
}
