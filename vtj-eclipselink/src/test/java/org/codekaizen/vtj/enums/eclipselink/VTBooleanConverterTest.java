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
package org.codekaizen.vtj.enums.eclipselink;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import org.codekaizen.vtj.enums.VTBoolean;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.sessions.Session;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTBooleanConverter}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTBooleanConverterTest {

    /**
     * Creates a new VTBooleanConverterTest object.
     */
    public VTBooleanConverterTest() {
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testIsMutable() {
        final VTBooleanConverter converter = new VTBooleanConverter();
        assertFalse(converter.isMutable());
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testInitialize() {
        final VTBooleanConverter converter = new VTBooleanConverter();
        final DatabaseMapping mapping = null;
        final Session session = mock(Session.class);
        converter.initialize(mapping, session);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testConvertDataValueToObjectValue() {
        final VTBooleanConverter converter = new VTBooleanConverter();
        final Session session = mock(Session.class);
        VTBoolean vtb = null;
        vtb = (VTBoolean) converter.convertDataValueToObjectValue(null, session);
        assertEquals(vtb, VTBoolean.UNKNOWN);
        vtb = (VTBoolean) converter.convertDataValueToObjectValue(new Integer(0), session);
        assertEquals(vtb, VTBoolean.FALSE);
        vtb = (VTBoolean) converter.convertDataValueToObjectValue(new Integer(1), session);
        assertEquals(vtb, VTBoolean.TRUE);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testConvertObjectValueToDataValue() {
        final VTBooleanConverter converter = new VTBooleanConverter();
        Object dataValue = null;
        final Session session = mock(Session.class);
        dataValue = converter.convertObjectValueToDataValue(VTBoolean.UNKNOWN, session);
        assertNull(dataValue);
        dataValue = converter.convertObjectValueToDataValue(VTBoolean.FALSE, session);
        assertEquals(dataValue, new Integer(0));
        dataValue = converter.convertObjectValueToDataValue(VTBoolean.TRUE, session);
        assertEquals(dataValue, new Integer(1));
    }

}
