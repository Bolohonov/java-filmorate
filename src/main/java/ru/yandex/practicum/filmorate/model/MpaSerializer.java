//package ru.yandex.practicum.filmorate.model;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import lombok.SneakyThrows;
//import ru.yandex.practicum.filmorate.exceptions.SerializationException;
//
//import java.io.IOException;
//
//public class MpaSerializer extends JsonSerializer<Mpa> {
//    @SneakyThrows
//    @Override
//    public void serialize(Mpa mpa, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//        try {
//            if (mpa == null) {
//                jsonGenerator.writeNull();
//            } else {
//                jsonGenerator.
//            }
//        } catch (Exception e) {
//            throw new SerializationException(e.getMessage());
//        }
//    }
//}
