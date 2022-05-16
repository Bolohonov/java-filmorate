package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.SneakyThrows;
import ru.yandex.practicum.filmorate.exceptions.SerializationException;

import java.time.Duration;

public class DurationSerializer extends JsonSerializer<Duration> {
    @SneakyThrows
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
            throw new SerializationException(e.getMessage());
        }
    }
}
