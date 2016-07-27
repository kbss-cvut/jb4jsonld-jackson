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
