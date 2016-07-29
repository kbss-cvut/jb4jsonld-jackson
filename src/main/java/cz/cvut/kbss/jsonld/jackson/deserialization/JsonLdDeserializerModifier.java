package cz.cvut.kbss.jsonld.jackson.deserialization;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import cz.cvut.kbss.jsonld.common.BeanAnnotationProcessor;

public class JsonLdDeserializerModifier extends BeanDeserializerModifier {

    @Override
    public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc,
                                                  JsonDeserializer<?> deserializer) {
        if (BeanAnnotationProcessor.isOwlClassEntity(beanDesc.getBeanClass())) {
            return new JacksonJsonLdDeserializer(deserializer, beanDesc.getBeanClass());
        }
        return deserializer;
    }
}
