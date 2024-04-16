/*
 * JB4JSON-LD Jackson
 * Copyright (C) 2024 Czech Technical University in Prague
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
package cz.cvut.kbss.jsonld.jackson.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.cvut.kbss.jsonld.common.BeanAnnotationProcessor;
import cz.cvut.kbss.jsonld.common.JsonLdPropertyAccessResolver;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.cvut.kbss.jsonld.common.BeanAnnotationProcessor.isInstanceIdentifier;
import static cz.cvut.kbss.jsonld.common.BeanAnnotationProcessor.isTypesField;

public class JsonPropertyAccessResolver extends JsonLdPropertyAccessResolver {

    // Remember fields ignored via JsonIgnoreProperties
    private final Map<Field, Class<?>> ignoredFields = new HashMap<>();
    private final Set<Class<?>> processedClasses = new HashSet<>();

    @Override
    public boolean isReadable(Field field, Class<?> objectCls) {
        Objects.requireNonNull(field);
        final JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        final JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
        return jsonIgnore == null && (jsonProperty == null || jsonProperty.access() != JsonProperty.Access.WRITE_ONLY ||
                isInstanceIdentifier(field) || isTypesField(field)) && !isJsonIgnoredPropertiesField(field, objectCls) && super.isReadable(field, objectCls);
    }

    private boolean isJsonIgnoredPropertiesField(Field field, Class<?> objectCls) {
        if (!processedClasses.contains(objectCls)) {
            synchronized (processedClasses) {
                final JsonIgnoreProperties ann = objectCls.getAnnotation(JsonIgnoreProperties.class);
                if (ann != null) {
                    final List<Class<?>> classes = BeanAnnotationProcessor.getAncestors(objectCls);
                    final Map<String, Field> fieldMap = new HashMap<>();
                    classes.stream().map(Class::getDeclaredFields).flatMap(Stream::of)
                           .forEach(f -> fieldMap.put(f.getName(), f));
                    Stream.of(ann.value()).filter(fieldMap::containsKey).map(fieldMap::get).forEach(f -> ignoredFields.put(f, objectCls));
                }
                processedClasses.add(objectCls);
            }
        }
        return ignoredFields.containsKey(field) && ignoredFields.get(field).equals(objectCls);
    }

    @Override
    public boolean isWriteable(Field field) {
        Objects.requireNonNull(field);
        final JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        final JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
        return jsonIgnore == null && (jsonProperty == null || jsonProperty.access() != JsonProperty.Access.READ_ONLY) &&
                !isJsonIgnoredPropertiesField(field, field.getDeclaringClass()) && super.isWriteable(field);
    }
}
