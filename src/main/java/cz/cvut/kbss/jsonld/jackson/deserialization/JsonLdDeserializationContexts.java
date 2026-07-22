package cz.cvut.kbss.jsonld.jackson.deserialization;

import tools.jackson.core.FormatSchema;
import tools.jackson.core.TokenStreamFactory;
import tools.jackson.databind.DeserializationConfig;
import tools.jackson.databind.InjectableValues;
import tools.jackson.databind.cfg.DeserializationContexts;
import tools.jackson.databind.deser.DeserializationContextExt;
import tools.jackson.databind.deser.DeserializerCache;
import tools.jackson.databind.deser.DeserializerFactory;

public class JsonLdDeserializationContexts extends DeserializationContexts {

    public JsonLdDeserializationContexts() {
        super();
    }

    public JsonLdDeserializationContexts(TokenStreamFactory tsf, DeserializerFactory deserializerFactory,
                                         DeserializerCache cache) {
        super(tsf, deserializerFactory, cache);
    }

    @Override
    protected DeserializationContexts forMapper(Object mapper, TokenStreamFactory tsf,
                                                DeserializerFactory deserializerFactory, DeserializerCache cache) {
        return new JsonLdDeserializationContexts(tsf, deserializerFactory, cache);
    }

    @Override
    public DeserializationContextExt createContext(DeserializationConfig config, FormatSchema schema,
                                                   InjectableValues injectables) {
        return new JsonLdDeserializationContext(_streamFactory, _deserializerFactory, _cache, config, schema,
                                                injectables);
    }
}
