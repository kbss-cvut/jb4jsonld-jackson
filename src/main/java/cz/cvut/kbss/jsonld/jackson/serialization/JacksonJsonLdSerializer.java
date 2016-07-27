package cz.cvut.kbss.jsonld.jackson.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import cz.cvut.kbss.jsonld.serialization.JsonLdSerializer;

import java.io.IOException;

class JacksonJsonLdSerializer<T> extends JsonSerializer<T> {

    @Override
    public void serialize(T value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        final cz.cvut.kbss.jsonld.serialization.JsonGenerator writer = new JacksonJsonWriter(jsonGenerator);
        final JsonLdSerializer serializer = JsonLdSerializer.createCompactedJsonLdSerializer(writer);
        serializer.serialize(value);
    }
}
