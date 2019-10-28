package filesio;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/*
public class StreamJSON {
    public static <T> T readObjectFromStream(InputStream input, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(input, clazz);
    }

    public static <T> void writeObjectToStream(OutputStream output, T object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(output, object);
    }
}*/

public class StreamJSON {
    public static <T> T read(String inputString, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        return mapper.readValue(inputString, clazz);
    }

    public static <T> String write(T object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        return mapper.writeValueAsString(object);
    }
}
