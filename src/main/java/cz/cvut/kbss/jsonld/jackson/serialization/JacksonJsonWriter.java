/**
 * Copyright (C) 2017 Czech Technical University in Prague
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.kbss.jsonld.jackson.serialization;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
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
    public void writeFieldName(String s) throws IOException {
        jsonGenerator.writeFieldName(s);
    }

    @Override
    public void writeObjectStart() throws IOException {
        jsonGenerator.writeStartObject();
    }

    @Override
    public void writeObjectEnd() throws IOException {
        jsonGenerator.writeEndObject();
    }

    @Override
    public void writeArrayStart() throws IOException {
        jsonGenerator.writeStartArray();
    }

    @Override
    public void writeArrayEnd() throws IOException {
        jsonGenerator.writeEndArray();
    }

    @Override
    public void writeNumber(Number number) throws IOException {
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
    public void writeBoolean(boolean b) throws IOException {
        jsonGenerator.writeBoolean(b);
    }

    @Override
    public void writeNull() throws IOException {
        jsonGenerator.writeNull();
    }

    @Override
    public void writeString(String s) throws IOException {
        jsonGenerator.writeString(s);
    }
}
