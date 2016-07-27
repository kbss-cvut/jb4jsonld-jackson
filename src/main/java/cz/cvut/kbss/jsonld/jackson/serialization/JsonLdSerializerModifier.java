package cz.cvut.kbss.jsonld.jackson.serialization;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import cz.cvut.kbss.jsonld.serialization.BeanAnnotationProcessor;

/**
 * Main point of integration of the JSON-LD serialization implementation into Jackson.
 */
public class JsonLdSerializerModifier extends BeanSerializerModifier {

    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc,
                                              JsonSerializer<?> serializer) {
        if (BeanAnnotationProcessor.isOwlClassEntity(beanDesc.getBeanClass())) {
            return new JacksonJsonLdSerializer<>();
        }
        return serializer;
    }
}
