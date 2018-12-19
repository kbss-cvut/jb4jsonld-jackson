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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.verify;

public class JacksonJsonWriterTest {

    @Mock
    private JsonGenerator generator;

    private JacksonJsonWriter writer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.writer = new JacksonJsonWriter(generator);
    }

    @Test
    public void writeNumberByteWritesByte() throws Exception {
        final byte number = 117;
        writer.writeNumber(number);
        verify(generator).writeNumber(number);
    }

    @Test
    public void writeNumberShortWritesShort() throws Exception {
        final short number = 117;
        writer.writeNumber(number);
        verify(generator).writeNumber(number);
    }

    @Test
    public void writeNumberIntegerWritesInteger() throws Exception {
        final int number = 117;
        writer.writeNumber(number);
        verify(generator).writeNumber(number);
    }

    @Test
    public void writeNumberLongWritesLong() throws Exception {
        final long number = System.currentTimeMillis();
        writer.writeNumber(number);
        verify(generator).writeNumber(number);
    }

    @Test
    public void writeNumberFloatWritesFloat() throws Exception {
        final float number = Float.MIN_VALUE;
        writer.writeNumber(number);
        verify(generator).writeNumber(number);
    }

    @Test
    public void writeNumberDoubleWritesDouble() throws Exception {
        final double number = Double.MAX_VALUE;
        writer.writeNumber(number);
        verify(generator).writeNumber(number);
    }

    @Test
    public void writeNumberBigIntegerWritesBigInteger() throws Exception {
        final BigInteger number = new BigInteger(Integer.toString(Integer.MAX_VALUE));
        writer.writeNumber(number);
        verify(generator).writeNumber(number);
    }

    @Test
    public void writeNumberBigDecimalWritesBigDecimal() throws Exception {
        final BigDecimal number = new BigDecimal(Integer.MAX_VALUE);
        writer.writeNumber(number);
        verify(generator).writeNumber(number);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeNumberInvalidThrowsIllegalArgumentException() throws Exception {
        final AtomicInteger number = new AtomicInteger(1);
        writer.writeNumber(number);
    }
}