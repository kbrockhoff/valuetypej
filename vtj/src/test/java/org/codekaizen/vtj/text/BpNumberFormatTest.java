/*
 * Copyright (c) 2008 Kevin Brockhoff
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.codekaizen.vtj.text;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import org.apache.commons.lang.SerializationUtils;
import org.testng.annotations.Test;


/**
 * <p>Class description.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class BpNumberFormatTest {

    /**
     * Creates a new BpNumberFormatTest object.
     */
    public BpNumberFormatTest() {
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testConstruction() {
        BpNumberFormat fmt1 = null;
        BpNumberFormat fmt2 = null;

        fmt1 = new BpNumberFormat();
        fmt2 = new BpNumberFormat(BpNumberFormat.JVM_NUMBER, Locale.getDefault());
        assertEquals(fmt1, fmt2);

    }

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Test
    public void testSerialization() throws Exception {
        BpNumberFormat fmt1 = null;
        BpNumberFormat fmt2 = null;

        fmt1 = new BpNumberFormat(BpNumberFormat.JVM_CURRENCY, null);
        fmt2 = (BpNumberFormat) SerializationUtils.deserialize(SerializationUtils.serialize(fmt1));
        assertEquals(fmt1, fmt2);

    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testFormatting() {
        NumberFormat fmt1 = null;
        NumberFormat fmt2 = null;
        String s1 = null;
        String s2 = null;
        Number num1 = null;

        fmt1 = new BpNumberFormat(BpNumberFormat.JVM_NUMBER, null);
        fmt2 = NumberFormat.getNumberInstance();
        num1 = new Integer(25436);
        s1 = fmt1.format(num1);
        s2 = fmt2.format(num1);
        assertEquals(s2, s1);

        num1 = new Double(1228.744);
        s1 = fmt1.format(num1);
        s2 = fmt2.format(num1);
        assertEquals(s2, s1);

        fmt1 = new BpNumberFormat(BpNumberFormat.JVM_CURRENCY, null);
        fmt2 = NumberFormat.getCurrencyInstance();
        num1 = new Integer(25436);
        s1 = fmt1.format(num1);
        s2 = fmt2.format(num1);
        assertEquals(s2, s1);

        num1 = new Double(1228.744);
        s1 = fmt1.format(num1);
        s2 = fmt2.format(num1);
        assertEquals(s2, s1);

        fmt1 = new BpNumberFormat(BpNumberFormat.JVM_CURRENCY, Locale.CANADA);
        fmt2 = NumberFormat.getCurrencyInstance(Locale.CANADA);
        num1 = new Integer(25436);
        s1 = fmt1.format(num1);
        s2 = fmt2.format(num1);
        assertEquals(s2, s1);

        num1 = new Double(1228.744);
        s1 = fmt1.format(num1);
        s2 = fmt2.format(num1);
        assertEquals(s2, s1);

        fmt1 = new BpNumberFormat(BpNumberFormat.JVM_PERCENT, null);
        fmt2 = NumberFormat.getPercentInstance();
        num1 = new Double(0.7444);
        s1 = fmt1.format(num1);
        s2 = fmt2.format(num1);
        assertEquals(s2, s1);

    }

    /**
     * DOCUMENT ME!
     *
     * @throws  ParseException  DOCUMENT ME!
     */
    @Test
    public void testParsing() throws ParseException {
        BpNumberFormat fmt1 = null;
        Number num1 = null;
        Number num2 = null;
        String s1 = null;

        fmt1 = new BpNumberFormat();
        s1 = "323,374";
        num1 = new Integer(323374);
        num2 = fmt1.parse(s1);
        assertEquals(num1, num2);

        s1 = "95.00";
        num1 = new BigDecimal(s1);
        num2 = fmt1.parse(s1);
        assertEquals(num1, num2);

        s1 = "-108.50";
        num1 = new BigDecimal(s1);
        num2 = fmt1.parse(s1);
        assertEquals(num1, num2);

        s1 = "80%";
        num1 = new Double(0.8);
        num2 = fmt1.parse(s1);
        assertEquals(num1, num2);

        s1 = "-971";
        num1 = new Integer(-971);
        num2 = fmt1.parse(s1);
        assertEquals(num1, num2);

        s1 = "(13.05)";
        num1 = new BigDecimal("-13.05");
        num2 = fmt1.parse(s1);
        assertEquals(num1, num2);

        s1 = ".10";
        num1 = new Double(0.1);
        num2 = fmt1.parse(s1);
        assertEquals(num1, num2);

        s1 = "-3.5";
        num1 = new Double(-3.5);
        num2 = fmt1.parse(s1);
        assertEquals(num1, num2);

        s1 = "($1.00)";
        num1 = new BigDecimal("-1.00");
        num2 = fmt1.parse(s1);
        assertEquals(num1, num2);

        s1 = "$0.00";
        num1 = new BigDecimal("0.00");
        num2 = fmt1.parse(s1);
        assertEquals(num1, num2);

        s1 = "1,108.00";
        num1 = new BigDecimal("1108.00");
        num2 = fmt1.parse(s1);
        assertEquals(num1, num2);

        s1 = "1/2";
        num1 = new Double(0.5);
        num2 = fmt1.parse(s1);
        assertEquals(num1, num2);

        s1 = "1/4";
        num1 = new Double(0.25);
        num2 = fmt1.parse(s1);
        assertEquals(num1, num2);

    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testCloning() {
        BpNumberFormat fmt1 = null;
        BpNumberFormat fmt2 = null;

        fmt1 = new BpNumberFormat(BpNumberFormat.JVM_INTEGER, null);
        fmt2 = (BpNumberFormat) fmt1.clone();
        assertEquals(fmt1, fmt2);

    }

}
