/*
 * Copyright (c) 2009 Kevin Brockhoff
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
package org.codekaizen.vtj.ids;

import static org.testng.Assert.*;

import org.codekaizen.vtj.AbstractValueTypeFactoryTest;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTLongSerialIdFactory}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTLongSerialIdFactoryTest extends AbstractValueTypeFactoryTest {

    /**
     * Creates a new VTLongSerialIdFactoryTest object.
     */
    public VTLongSerialIdFactoryTest() {
        super(VTLongSerialIdFactory.class);
    }

    private VTLongSerialIdFactory createFactory(final long initVal) {
        super.getConfigurationValues().put("org.codekaizen.vtj.ids.VTLongSerialIdFactory.initialValue", initVal);

        return (VTLongSerialIdFactory) super.createFactory();
    }

    /**
     * DOCUMENT ME!
     */
    protected void doTestIsCreatable() {
        final VTLongSerialIdFactory factory = createFactory(0L);
        assertTrue(factory.isCreatable(VTLongSerialId.class));
        assertFalse(factory.isCreatable(VTUUID.class));
    }

    /**
     * DOCUMENT ME!
     */
    protected void doTestCreateObjectArray() {
        final VTLongSerialIdFactory factory = createFactory(0L);
        VTLongSerialId vt1 = factory.create();
        assertEquals(vt1.longValue(), 1L);

        final VTLongSerialId vt2 = factory.create(vt1);
        assertEquals(vt2, vt1);
        assertFalse(vt1 == vt2);
        vt1 = factory.create(152456.48);
        assertEquals(vt1.longValue(), 152456L);
    }

    /**
     * DOCUMENT ME!
     */
    protected void doTestIsParsable() {
        final VTLongSerialIdFactory factory = createFactory(0L);
        assertTrue(factory.isParsable("1234567L"));
        assertTrue(factory.isParsable("12345"));
        assertTrue(factory.isParsable("sid:12345432"));
        assertFalse(factory.isParsable("FID>"));
    }

    /**
     * DOCUMENT ME!
     */
    protected void doTestParseString() {
        final VTLongSerialIdFactory factory = createFactory(0L);
        VTLongSerialId serialId = factory.parse("1234567L");
        assertEquals(serialId.longValue(), 1234567L);
        serialId = factory.parse("12345");
        assertEquals(serialId.longValue(), 12345L);
        serialId = factory.parse("sid:12345432");
        assertEquals(serialId.longValue(), 12345432L);
    }

    /**
     * DOCUMENT ME!
     */
    protected void doTestFormatValueType() {
        final VTLongSerialIdFactory factory = createFactory(0L);
        final String strVal = "sid:4833";
        final VTLongSerialId serialId = factory.create(strVal);
        assertEquals(serialId.toString(), strVal);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldIncrementSuccessiveCallsToNextId() {
        final VTLongSerialIdFactory factory = createFactory(11111L);
        VTLongSerialId curId = null;
        VTLongSerialId prevId = factory.nextSerialId();

        for (int i = 0; i < 100; i++) {
            curId = factory.nextSerialId();
            assertEquals(curId.longValue(), prevId.longValue() + 1L);
            prevId = curId;
        }
    }

}
