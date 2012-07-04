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
package org.codekaizen.vtj.ids.hibernate3;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.HashMap;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Types;
import org.codekaizen.vtj.hibernate3.AbstractValueTypeUserTypeTest;
import org.codekaizen.vtj.ids.VTUUID;
import org.codekaizen.vtj.ids.VTUUIDFactory;
import org.codekaizen.vtj.ids.UUIDVersion;
import org.codekaizen.vtj.MapContextHandlingStrategy;
import org.hibernate.usertype.UserType;

/**
 * <p>Unit tests for {@link VTUUIDStringUserType}.
 * </p>
 *
 * @author <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTUUIDStringUserTypeTest extends AbstractValueTypeUserTypeTest {

    public VTUUIDStringUserTypeTest() {
        super(VTUUIDStringUserType.class, VTUUID.class,
                new Class<?>[] { Long.TYPE, Long.TYPE, },
                new Object[] {0x8A2C084DBE5F426FL, 0xAB02E56FC73DCA80L, });
    }

    private VTUUIDFactory constructFactory() {
        VTUUIDFactory factory =
                new VTUUIDFactory(new MapContextHandlingStrategy());
        Map<String, Object> configurationValues = new HashMap<String, Object>();
        configurationValues.put("org.codekaizen.vtj.ids.VTUUIDFactory.version",
                UUIDVersion.TIME_SPACE);
        factory.setContext(configurationValues);
        return factory;
    }

    @Override
    public void givenNonNullDBValueWhenNullSafeGetThenReturnVTObject()
            throws SQLException {
        UserType ut = super.newUserTypeInstance();
        VTUUIDFactory factory = constructFactory();
        VTUUID expected = factory.nextUuid();
        ResultSet rs = mock(ResultSet.class);
        when(rs.getObject("column1"))
                .thenReturn(expected.toBase16());
        when(rs.wasNull()).thenReturn(Boolean.FALSE);
        String[] names = new String[] { "column1", };
        Object owner = super.newTestEntity();
        VTUUID result = (VTUUID) ut.nullSafeGet(rs, names, owner);
        assertEquals(result, expected);
    }

    @Override
    public void givenNullDBValueWhenNullSafeGetThenReturnNull()
            throws SQLException {
        UserType ut = super.newUserTypeInstance();
        ResultSet rs = mock(ResultSet.class);
        when(rs.getObject("column1"))
                .thenReturn(null);
        when(rs.wasNull()).thenReturn(Boolean.TRUE);
        String[] names = new String[] { "column1", };
        Object owner = super.newTestEntity();
        VTUUID result = (VTUUID) ut.nullSafeGet(rs, names, owner);
        assertNull(result);
    }

    @Override
    public void givenVTObjectWhenNullSafeSetThenSetSingleDBValue()
            throws SQLException {
        UserType ut = super.newUserTypeInstance();
        VTUUIDFactory factory = constructFactory();
        VTUUID uuid = factory.nextUuid();
        PreparedStatement ps = mock(PreparedStatement.class);
        ut.nullSafeSet(ps, uuid, 4);
    }

    @Override
    public void givenNullWhenNullSafeSetThenSetNull()
            throws SQLException {
        UserType ut = super.newUserTypeInstance();
        PreparedStatement ps = mock(PreparedStatement.class);
        ut.nullSafeSet(ps, null, 4);
    }

}
