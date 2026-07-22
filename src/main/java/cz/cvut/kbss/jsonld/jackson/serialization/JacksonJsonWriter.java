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
package cz.cvut.kbss.jsonld.jackson.serialization;

import tools.jackson.core.JsonGenerator;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Outputs JSON using Jackson.
 */
class JacksonJsonWriter implements cz.cvut.kbss.jsonld.serialization.JsonGenerator {

    private final JsonGenerator jsonGenerator;

    JacksonJsonWriter(JsonGenerator jsonGenerator) {
        this.jsonGenerator = jsonGenerator;
    }

    @Override
    public void writeFieldName(String s) {
        jsonGenerator.writeName(s);
    }

    @Override
    public void writeObjectStart() {
        jsonGenerator.writeStartObject();
    }

    @Override
    public void writeObjectEnd() {
        jsonGenerator.writeEndObject();
    }

    @Override
    public void writeArrayStart() {
        jsonGenerator.writeStartArray();
    }

    @Override
    public void writeArrayEnd() {
        jsonGenerator.writeEndArray();
    }

    @Override
    public void writeNumber(Number number) {
        if (number instanceof Integer) {
            jsonGenerator.writeNumber(number.intValue());
        } else if (number instanceof Long) {
            jsonGenerator.writeNumber(number.longValue());
        } else if (number instanceof Float) {
            jsonGenerator.writeNumber(number.floatValue());
        } else if (number instanceof Double) {
            jsonGenerator.writeNumber(number.doubleValue());
        } else if (number instanceof BigInteger) {
            jsonGenerator.writeNumber((BigInteger) number);
        } else if (number instanceof BigDecimal) {
            jsonGenerator.writeNumber((BigDecimal) number);
        } else if (number instanceof Short) {
            jsonGenerator.writeNumber(number.shortValue());
        } else if (number instanceof Byte) {
            jsonGenerator.writeNumber(number.byteValue());
        } else {
            throw new IllegalArgumentException("Unable to write number " + number + " of type " + number.getClass());
        }
    }

    @Override
    public void writeBoolean(boolean b) {
        jsonGenerator.writeBoolean(b);
    }

    @Override
    public void writeNull() {
        jsonGenerator.writeNull();
    }

    @Override
    public void writeString(String s) {
        jsonGenerator.writeString(s);
    }
}
