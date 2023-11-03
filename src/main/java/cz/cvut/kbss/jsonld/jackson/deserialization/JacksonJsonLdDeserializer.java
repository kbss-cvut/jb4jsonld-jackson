/*
 * JB4JSON-LD Jackson
 * Copyright (C) 2023 Czech Technical University in Prague
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

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.datatype.jsonp.JSONPModule;
import cz.cvut.kbss.jsonld.ConfigParam;
import cz.cvut.kbss.jsonld.Configuration;
import cz.cvut.kbss.jsonld.deserialization.JsonLdDeserializer;
import cz.cvut.kbss.jsonld.deserialization.ValueDeserializer;
import cz.cvut.kbss.jsonld.exception.JsonLdDeserializationException;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;

import java.io.IOException;
import java.util.Map;

public class JacksonJsonLdDeserializer extends DelegatingDeserializer {

    private final ObjectMapper mapper;

    private final Configuration configuration;

    private final Class<?> resultType;

    private final Map<Class<?>, ValueDeserializer<?>> commonDeserializers;

    public JacksonJsonLdDeserializer(JsonDeserializer<?> delegatee, Class<?> resultType, Configuration configuration,
                                     Map<Class<?>, ValueDeserializer<?>> commonDeserializers) {
        super(delegatee);
        this.resultType = resultType;
        this.configuration = configuration;
        this.commonDeserializers = commonDeserializers;
        this.mapper = JsonMapper.builder()
                                .addModule(new JSONPModule())
                                .build();
    }

    @Override
    protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> newDelegatee) {
        return new JacksonJsonLdDeserializer(newDelegatee, resultType, configuration, commonDeserializers);
    }

    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
        try {

            JsonValue input = parseJsonObject(jp);
            if (input.getValueType() == JsonValue.ValueType.ARRAY || input.getValueType() == JsonValue.ValueType.OBJECT) {
                final JsonDocument doc = JsonDocument.of((JsonStructure) input);
                input = JsonLd.expand(doc).get();
            }
            final JsonLdDeserializer deserializer = JsonLdDeserializer.createExpandedDeserializer(configure(ctx));
            commonDeserializers.forEach((t, d) -> deserializer.registerDeserializer((Class) t, (ValueDeserializer) d));
            return deserializer.deserialize(input, resultType);
        } catch (JsonLdError e) {
            throw new JsonLdDeserializationException("Unable to expand the input JSON.", e);
        }
    }

    private JsonValue parseJsonObject(JsonParser parser) throws IOException {
        JsonValue value = null;
        final JsonToken initialToken = parser.getCurrentToken();
        parser.setCodec(mapper);
        if (initialToken == JsonToken.START_ARRAY) {
            value = parser.readValueAs(JsonArray.class);
        } else if (initialToken == JsonToken.START_OBJECT) {
            value = parser.readValueAs(JsonObject.class);
        } else if (initialToken == JsonToken.VALUE_STRING) {
            value = parser.readValueAs(JsonString.class);
        } else if (initialToken == JsonToken.VALUE_FALSE) {
            value = parser.readValueAs(JsonValue.class);
        } else if (initialToken == JsonToken.VALUE_NUMBER_FLOAT || initialToken == JsonToken.VALUE_NUMBER_INT) {
            value = parser.readValueAs(JsonNumber.class);
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
    public Object deserializeWithType(JsonParser jp, DeserializationContext ctx,
                                      TypeDeserializer typeDeserializer) throws IOException {
        return deserialize(jp, ctx);
    }
}
