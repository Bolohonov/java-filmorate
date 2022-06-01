package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.io.IOException;

public class MpaDeSerializer extends JsonDeserializer<Mpa> {
    private final MpaDbStorage mpaDbStorage;

    public MpaDeSerializer(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    @Override
    public Mpa deserialize(JsonParser jp, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        JsonNode node = jp.getCodec().readTree(jp);
        int id = (Integer) (node.get("id")).numberValue();
        return mpaDbStorage.getNewMpaObject(id);
    }
}
