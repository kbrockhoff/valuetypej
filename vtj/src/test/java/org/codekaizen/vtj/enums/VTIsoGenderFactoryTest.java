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
import org.codekaizen.vtj.text.VTString;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTIsoGenderFactory}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTIsoGenderFactoryTest extends AbstractValueTypeFactoryTest {

    /**
     * Creates a new VTIsoGenderFactoryTest object.
     */
    public VTIsoGenderFactoryTest() {
        super(VTIsoGenderFactory.class);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestIsCreatable() {
        final VTIsoGenderFactory factory = (VTIsoGenderFactory) super.createFactory();
        assertTrue(factory.isCreatable(VTIsoGender.class));
        assertFalse(factory.isCreatable(null));
        assertFalse(factory.isCreatable(VTBoolean.class));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestCreateObjectArray() {
        VTIsoGender g = null;
        final VTIsoGenderFactory factory = (VTIsoGenderFactory) super.createFactory();
        g = factory.create();
        assertEquals(g, VTIsoGender.NOT_KNOWN);

        for (int i = 0; i < VTIsoGender.values().length; i++) {
            g = factory.create(VTIsoGender.values()[i].getDigitCode());
            assertEquals(g, VTIsoGender.values()[i]);
        }

        for (int i = 0; i < VTIsoGender.values().length; i++) {
            g = factory.create(VTIsoGender.values()[i].name());
            assertEquals(g, VTIsoGender.values()[i]);
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCreateInvalidObjectArray() {
        VTIsoGender g = null;
        final VTIsoGenderFactory factory = (VTIsoGenderFactory) super.createFactory();
        g = factory.create("");
        assertEquals(g, VTIsoGender.NOT_KNOWN);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestIsParsable() {
        final VTIsoGenderFactory factory = (VTIsoGenderFactory) super.createFactory();

        for (int i = 0; i < VTIsoGenderFactory.VALID_STRINGS.length; i++) {
            assertTrue(factory.isParsable(VTIsoGenderFactory.VALID_STRINGS[i]));
        }

        assertFalse(factory.isParsable(null));
        assertTrue(factory.isParsable(new VTString("F")));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestParseString() {
        VTIsoGender g = null;
        final VTIsoGenderFactory factory = (VTIsoGenderFactory) super.createFactory();

        for (int i = 0; i < VTIsoGenderFactory.VALID_STRINGS.length; i++) {
            g = factory.parse(VTIsoGenderFactory.VALID_STRINGS[i]);
            assertNotNull(g);
        }

        g = factory.parse("0");
        assertEquals(g, VTIsoGender.NOT_KNOWN);
        g = factory.parse("MALE");
        assertEquals(g, VTIsoGender.MALE);
        g = factory.parse("1");
        assertEquals(g, VTIsoGender.MALE);
        g = factory.parse("FEMALE");
        assertEquals(g, VTIsoGender.FEMALE);
        g = factory.parse("2");
        assertEquals(g, VTIsoGender.FEMALE);
        g = factory.parse("NOT_APPLICABLE");
        assertEquals(g, VTIsoGender.NOT_APPLICABLE);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testParseInvalidString() {
        VTIsoGender g = null;
        final VTIsoGenderFactory factory = (VTIsoGenderFactory) super.createFactory();
        g = factory.parse("eunuch");
        assertEquals(g, VTIsoGender.NOT_KNOWN);
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void doTestFormatValueType() {
        String s = null;
        final VTIsoGenderFactory factory = (VTIsoGenderFactory) super.createFactory();

        for (int i = 0; i < VTIsoGender.values().length; i++) {
            s = factory.format(VTIsoGender.values()[i]);
            assertEquals(s, VTIsoGender.values()[i].name());
        }
    }

}
