/*
 * JB4JSON-LD Jackson
 * Copyright (C) 2025 Czech Technical University in Prague
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

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

class JacksonJsonWriterTest {

    @Mock
    private JsonGenerator generator;

    private JacksonJsonWriter writer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.writer = new JacksonJsonWriter(generator);
    }

    @Test
    void writeNumberByteWritesByte() throws Exception {
        final byte number = 117;
        writer.writeNumber(number);
        verify(generator).writeNumber(number);
    }

    @Test
    void writeNumberShortWritesShort() throws Exception {
        final short number = 117;
        writer.writeNumber(number);
        verify(generator).writeNumber(number);
    }

    @Test
    void writeNumberIntegerWritesInteger() throws Exception {
        final int number = 117;
        writer.writeNumber(number);
        verify(generator).writeNumber(number);
    }

    @Test
    void writeNumberLongWritesLong() throws Exception {
        final long number = System.currentTimeMillis();
        writer.writeNumber(number);
        verify(generator).writeNumber(number);
    }

    @Test
    void writeNumberFloatWritesFloat() throws Exception {
        final float number = Float.MIN_VALUE;
        writer.writeNumber(number);
        verify(generator).writeNumber(number);
    }

    @Test
    void writeNumberDoubleWritesDouble() throws Exception {
        final double number = Double.MAX_VALUE;
        writer.writeNumber(number);
        verify(generator).writeNumber(number);
    }

    @Test
    void writeNumberBigIntegerWritesBigInteger() throws Exception {
        final BigInteger number = new BigInteger(Integer.toString(Integer.MAX_VALUE));
        writer.writeNumber(number);
        verify(generator).writeNumber(number);
    }

    @Test
    void writeNumberBigDecimalWritesBigDecimal() throws Exception {
        final BigDecimal number = new BigDecimal(Integer.MAX_VALUE);
        writer.writeNumber(number);
        verify(generator).writeNumber(number);
    }

    @Test
    void writeNumberInvalidThrowsIllegalArgumentException() {
        final AtomicInteger number = new AtomicInteger(1);
        assertThrows(IllegalArgumentException.class, () -> writer.writeNumber(number));
    }
}