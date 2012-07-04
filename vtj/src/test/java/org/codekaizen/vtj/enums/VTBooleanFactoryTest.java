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
package org.codekaizen.vtj.enums;

import static org.testng.Assert.*;

import org.codekaizen.vtj.AbstractValueTypeFactoryTest;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTBooleanFactory}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTBooleanFactoryTest extends AbstractValueTypeFactoryTest {

    /**
     * Creates a new VTBooleanFactoryTest object.
     */
    public VTBooleanFactoryTest() {
        super(VTBooleanFactory.class);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestIsCreatable() {
        final VTBooleanFactory factory = (VTBooleanFactory) super.createFactory();
        assertTrue(factory.isCreatable(VTBoolean.class));
        assertFalse(factory.isCreatable(null));
        assertFalse(factory.isCreatable(VTIsoGender.class));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestCreateObjectArray() {
        VTBoolean b = null;
        final VTBooleanFactory factory = (VTBooleanFactory) super.createFactory();
        b = factory.create();
        assertEquals(b, VTBoolean.UNKNOWN);
        b = factory.create(true);
        assertEquals(b, VTBoolean.TRUE);
        b = factory.create(false);
        assertEquals(b, VTBoolean.FALSE);
        b = factory.create(0);
        assertEquals(b, VTBoolean.FALSE);
        b = factory.create(1);
        assertEquals(b, VTBoolean.TRUE);
        b = factory.create(2);
        assertEquals(b, VTBoolean.TRUE);

        for (int i = 0; i < VTBoolean.values().length; i++) {
            b = factory.create(VTBoolean.values()[i].name());
            assertEquals(b, VTBoolean.values()[i]);
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreateInvalidObjectArray() {
        VTBoolean b = null;
        final VTBooleanFactory factory = (VTBooleanFactory) super.createFactory();
        b = factory.create("No Way");
        assertEquals(b, VTBoolean.FALSE);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestIsParsable() {
        final VTBooleanFactory factory = (VTBooleanFactory) super.createFactory();

        for (int i = 0; i < VTBooleanFactory.VALID_STRINGS.length; i++) {
            assertTrue(factory.isParsable(VTBooleanFactory.VALID_STRINGS[0]));
        }

        assertFalse(factory.isParsable(null));
        assertFalse(factory.isParsable("never"));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestParseString() {
        VTBoolean b = null;
        final VTBooleanFactory factory = (VTBooleanFactory) super.createFactory();

        for (int i = 0; i < VTBooleanFactory.VALID_STRINGS.length; i++) {
            b = factory.parse(VTBooleanFactory.VALID_STRINGS[i]);
            assertFalse(VTBoolean.UNKNOWN.equals(b));
        }

        b = factory.parse(VTBoolean.UNKNOWN.name());
        assertEquals(b, VTBoolean.UNKNOWN);
        b = factory.parse("1");
        assertEquals(b, VTBoolean.TRUE);
        b = factory.parse("false");
        assertEquals(b, VTBoolean.FALSE);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testParseInvalidString() {
        VTBoolean b = null;
        final VTBooleanFactory factory = (VTBooleanFactory) super.createFactory();
        b = factory.parse("absolutely");
        assertEquals(b, VTBoolean.UNKNOWN);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestFormatValueType() {
        String s = null;
        final VTBooleanFactory factory = (VTBooleanFactory) super.createFactory();

        for (int i = 0; i < VTBoolean.values().length; i++) {
            s = factory.format(VTBoolean.values()[i]);
            assertEquals(s, VTBoolean.values()[i].name());
        }
    }

}
