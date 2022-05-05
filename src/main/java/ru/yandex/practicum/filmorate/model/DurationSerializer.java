package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.time.Duration;

public class DurationSerializer extends JsonSerializer<Duration> {
    @Override
    public void serialize(Duration duration,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) {
        try {
            if (duration == null) {
                jsonGenerator.writeNull();
            } else {
                jsonGenerator.writeNumber(duration.toSeconds());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
