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
        final Float number = Float.MIN_VALUE;
        writer.writeNumber(number);
        verify(generator).writeNumber(number);
    }

    @Test
    public void writeNumberDoubleWritesDouble() throws Exception {
        final Double number = Double.MAX_VALUE;
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