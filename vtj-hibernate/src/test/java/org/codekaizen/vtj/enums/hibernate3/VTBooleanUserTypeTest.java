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
package org.codekaizen.vtj.enums.hibernate3;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.codekaizen.vtj.enums.VTBoolean;
import org.codekaizen.vtj.hibernate3.AbstractValueTypeUserTypeTest;
import org.hibernate.usertype.UserType;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTBooleanUserType}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTBooleanUserTypeTest extends AbstractValueTypeUserTypeTest {

    /**
     * Creates a new VTBooleanUserTypeTest object.
     */
    public VTBooleanUserTypeTest() {
        super(VTBooleanUserType.class, VTBoolean.class, new Class<?>[] {}, new Object[] {});
    }

    @Override
    public void givenNonNullDBValueWhenNullSafeGetThenReturnVTObject() throws SQLException {
        final UserType ut = super.newUserTypeInstance();
        final ResultSet rs = mock(ResultSet.class);
        when(rs.getObject("column1")).thenReturn(new Integer(0));
        when(rs.wasNull()).thenReturn(Boolean.FALSE);

        final String[] names = new String[] { "column1", };
        final Object owner = super.newTestEntity();
        final VTBoolean b = (VTBoolean) ut.nullSafeGet(rs, names, owner);
        assertEquals(b, VTBoolean.FALSE);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    @Test
    public void givenNonZeroDBValueWhenNullSafeGetThenReturnTrue() throws SQLException {
        final UserType ut = super.newUserTypeInstance();
        final ResultSet rs = mock(ResultSet.class);
        when(rs.getObject("column1")).thenReturn(new Integer(1));
        when(rs.wasNull()).thenReturn(Boolean.FALSE);

        final String[] names = new String[] { "column1", };
        final Object owner = super.newTestEntity();
        final VTBoolean b = (VTBoolean) ut.nullSafeGet(rs, names, owner);
        assertEquals(b, VTBoolean.TRUE);
    }

    @Override
    public void givenNullDBValueWhenNullSafeGetThenReturnNull() throws SQLException {
        final UserType ut = super.newUserTypeInstance();
        final ResultSet rs = mock(ResultSet.class);
        when(rs.getObject("column1")).thenReturn(null);
        when(rs.wasNull()).thenReturn(Boolean.TRUE);

        final String[] names = new String[] { "column1", };
        final Object owner = super.newTestEntity();
        final VTBoolean b = (VTBoolean) ut.nullSafeGet(rs, names, owner);
        assertEquals(b, VTBoolean.UNKNOWN);
    }

    @Override
    public void givenVTObjectWhenNullSafeSetThenSetSingleDBValue() throws SQLException {
        final UserType ut = super.newUserTypeInstance();
        final PreparedStatement ps = mock(PreparedStatement.class);
        ut.nullSafeSet(ps, VTBoolean.FALSE, 4);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    @Test
    public void givenTrueWhenNullSafeSetThenSetOne() throws SQLException {
        final UserType ut = super.newUserTypeInstance();
        final PreparedStatement ps = mock(PreparedStatement.class);
        ut.nullSafeSet(ps, VTBoolean.TRUE, 4);
    }

    @Override
    public void givenNullWhenNullSafeSetThenSetNull() throws SQLException {
        final UserType ut = super.newUserTypeInstance();
        final PreparedStatement ps = mock(PreparedStatement.class);
        ut.nullSafeSet(ps, null, 4);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    @Test
    public void givenUnknownWhenNullSafeSetThenSetNull() throws SQLException {
        final UserType ut = super.newUserTypeInstance();
        final PreparedStatement ps = mock(PreparedStatement.class);
        ut.nullSafeSet(ps, VTBoolean.UNKNOWN, 4);
    }

}
