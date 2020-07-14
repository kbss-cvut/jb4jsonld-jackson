/**
 * Copyright (C) 2017 Czech Technical University in Prague
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package cz.cvut.kbss.jsonld.jackson.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdProcessor;
import cz.cvut.kbss.jsonld.ConfigParam;
import cz.cvut.kbss.jsonld.Configuration;
import cz.cvut.kbss.jsonld.deserialization.JsonLdDeserializer;
import cz.cvut.kbss.jsonld.exception.JsonLdDeserializationException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JacksonJsonLdDeserializer extends DelegatingDeserializer {

    private final ObjectMapper mapper = new ObjectMapper();

    private final Configuration configuration;

    private final Class<?> resultType;

    public JacksonJsonLdDeserializer(JsonDeserializer<?> delegatee, Class<?> resultType, Configuration configuration) {
        super(delegatee);
        this.resultType = resultType;
        this.configuration = configuration;
    }

    @Override
    protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> newDelegatee) {
        return new JacksonJsonLdDeserializer(newDelegatee, resultType, configuration);
    }

    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
        try {
            final Object input = parseJsonObject(jp);
            final List<Object> expanded = JsonLdProcessor.expand(input);
            final JsonLdDeserializer deserializer = JsonLdDeserializer.createExpandedDeserializer(configure(ctx));
            return deserializer.deserialize(expanded, resultType);
        } catch (JsonLdError e) {
            throw new JsonLdDeserializationException("Unable to expand the input JSON.", e);
        }
    }

    private Object parseJsonObject(JsonParser parser) throws IOException {
        Object value = null;
        final JsonToken initialToken = parser.getCurrentToken();
        parser.setCodec(mapper);
        if (initialToken == JsonToken.START_ARRAY) {
            value = parser.readValueAs(new TypeReference<List<?>>() {
            });
        } else if (initialToken == JsonToken.START_OBJECT) {
            value = parser.readValueAs(new TypeReference<Map<?, ?>>() {
            });
        } else if (initialToken == JsonToken.VALUE_STRING) {
            value = parser.readValueAs(String.class);
        } else if (initialToken == JsonToken.VALUE_FALSE || initialToken == JsonToken.VALUE_TRUE) {
            value = parser.readValueAs(Boolean.class);
        } else if (initialToken == JsonToken.VALUE_NUMBER_FLOAT || initialToken == JsonToken.VALUE_NUMBER_INT) {
            value = parser.readValueAs(Number.class);
        }
        return value;
    }

    private Configuration configure(DeserializationContext context) {
        final Configuration config = new Configuration(configuration);
        if (!context.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)) {
            config.set(ConfigParam.IGNORE_UNKNOWN_PROPERTIES, Boolean.TRUE.toString());
        }
        return config;
    }

    @Override
    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt,
                                      TypeDeserializer typeDeserializer) throws IOException {
        return deserialize(jp, ctxt);
    }
}
