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
package cz.cvut.kbss.jsonld.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import cz.cvut.kbss.jsonld.ConfigParam;
import cz.cvut.kbss.jsonld.Configuration;
import cz.cvut.kbss.jsonld.common.PropertyAccessResolver;
import cz.cvut.kbss.jsonld.deserialization.ValueDeserializer;
import cz.cvut.kbss.jsonld.jackson.common.JsonPropertyAccessResolver;
import cz.cvut.kbss.jsonld.jackson.deserialization.JsonLdDeserializerModifier;
import cz.cvut.kbss.jsonld.jackson.serialization.JsonLdSerializerModifier;
import cz.cvut.kbss.jsonld.serialization.serializer.ValueSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Simple module with pre-configured serializer and deserializer modifier.
 */
public class JsonLdModule extends SimpleModule {

    private final Configuration configuration = new Configuration();

    private final Map<Class<?>, ValueSerializer<?>> commonSerializers = new HashMap<>();
    private final Map<Class<?>, ValueDeserializer<?>> commonDeserializers = new HashMap<>();

    public JsonLdModule() {
        init();
    }

    private void init() {
        final PropertyAccessResolver accessResolver = new JsonPropertyAccessResolver();
        setSerializerModifier(new JsonLdSerializerModifier(configuration, accessResolver, commonSerializers));
        setDeserializerModifier(new JsonLdDeserializerModifier(configuration, accessResolver, commonDeserializers));
    }

    /**
     * Configure this module with additional parameters.
     *
     * @param param Parameter to set
     * @param value New value of the parameter
     * @return This instance
     */
    public JsonLdModule configure(ConfigParam param, String value) {
        Objects.requireNonNull(param);
        configuration.set(param, value);
        return this;
    }

    /**
     * Configure this module with additional parameters.
     *
     * @param param Parameter to set
     * @param value New value of the parameter
     * @return This instance
     */
    public JsonLdModule configure(String param, String value) {
        Objects.requireNonNull(param);
        configuration.set(param, value);
        return this;
    }

    /**
     * Registers the specified serializer for the specified type.
     *
     * @param forType    Type to register the serializer for
     * @param serializer Value serializer being registered
     * @param <T>        Type
     * @return This instance
     */
    public <T> JsonLdModule registerSerializer(Class<T> forType, ValueSerializer<T> serializer) {
        Objects.requireNonNull(forType);
        Objects.requireNonNull(serializer);
        commonSerializers.put(forType, serializer);
        return this;
    }

    /**
     * Registers the specified deserializer for the specified type.
     *
     * @param forType      Type to register the deserializer for
     * @param deserializer Value deserializer being registered
     * @param <T>          Type
     * @return This instance
     */
    public <T> JsonLdModule registerDeserializer(Class<T> forType, ValueDeserializer<T> deserializer) {
        Objects.requireNonNull(forType);
        Objects.requireNonNull(deserializer);
        commonDeserializers.put(forType, deserializer);
        return this;
    }
}
