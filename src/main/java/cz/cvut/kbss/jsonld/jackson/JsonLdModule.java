package cz.cvut.kbss.jsonld.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import cz.cvut.kbss.jsonld.jackson.deserialization.JsonLdDeserializerModifier;
import cz.cvut.kbss.jsonld.jackson.serialization.JsonLdSerializerModifier;

/**
 * Simple module with pre-configured serializer and deserializer modifier.
 */
public class JsonLdModule extends SimpleModule {

    public JsonLdModule() {
        init();
    }

    private void init() {
        setSerializerModifier(new JsonLdSerializerModifier());
        setDeserializerModifier(new JsonLdDeserializerModifier());
    }
}
