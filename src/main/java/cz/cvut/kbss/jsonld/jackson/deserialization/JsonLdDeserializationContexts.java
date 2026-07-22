/*
 * JB4JSON-LD Jackson
 * Copyright (C) 2026 Czech Technical University in Prague
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
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
