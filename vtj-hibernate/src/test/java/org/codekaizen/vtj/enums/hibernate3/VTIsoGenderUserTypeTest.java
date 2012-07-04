package org.codekaizen.vtj.enums.hibernate3;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.codekaizen.vtj.enums.VTIsoGender;
import org.codekaizen.vtj.hibernate3.AbstractValueTypeUserTypeTest;
import org.hibernate.usertype.UserType;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link VTIsoGenderUserType}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTIsoGenderUserTypeTest extends AbstractValueTypeUserTypeTest {

    /**
     * Creates a new VTIsoGenderUserTypeTest object.
     */
    public VTIsoGenderUserTypeTest() {
        super(VTIsoGenderUserType.class, VTIsoGender.class, new Class<?>[] {}, new Object[] {});
    }

    @Override
    public void givenNonNullDBValueWhenNullSafeGetThenReturnVTObject() throws SQLException {
        final UserType ut = super.newUserTypeInstance();
        final ResultSet rs = mock(ResultSet.class);
        when(rs.getObject("column1")).thenReturn(new Integer(1));
        when(rs.wasNull()).thenReturn(Boolean.FALSE);

        final String[] names = new String[] { "column1", };
        final Object owner = super.newTestEntity();
        final VTIsoGender b = (VTIsoGender) ut.nullSafeGet(rs, names, owner);
        assertEquals(b, VTIsoGender.MALE);
    }

    @Override
    public void givenNullDBValueWhenNullSafeGetThenReturnNull() throws SQLException {
        final UserType ut = super.newUserTypeInstance();
        final ResultSet rs = mock(ResultSet.class);
        when(rs.getObject("column1")).thenReturn(null);
        when(rs.wasNull()).thenReturn(Boolean.TRUE);

        final String[] names = new String[] { "column1", };
        final Object owner = super.newTestEntity();
        final VTIsoGender b = (VTIsoGender) ut.nullSafeGet(rs, names, owner);
        assertEquals(b, VTIsoGender.NOT_KNOWN);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    @Test
    public void given3DBValueWhenNullSafeGetThenReturnNotApplicable() throws SQLException {
        final UserType ut = super.newUserTypeInstance();
        final ResultSet rs = mock(ResultSet.class);
        when(rs.getObject("column1")).thenReturn(new Integer(9));
        when(rs.wasNull()).thenReturn(Boolean.FALSE);

        final String[] names = new String[] { "column1", };
        final Object owner = super.newTestEntity();
        final VTIsoGender b = (VTIsoGender) ut.nullSafeGet(rs, names, owner);
        assertEquals(b, VTIsoGender.NOT_APPLICABLE);
    }

    @Override
    public void givenVTObjectWhenNullSafeSetThenSetSingleDBValue() throws SQLException {
        final UserType ut = super.newUserTypeInstance();
        final PreparedStatement ps = mock(PreparedStatement.class);
        ut.nullSafeSet(ps, VTIsoGender.FEMALE, 4);
    }

    @Override
    public void givenNullWhenNullSafeSetThenSetNull() throws SQLException {
        final UserType ut = super.newUserTypeInstance();
        final PreparedStatement ps = mock(PreparedStatement.class);
        ut.nullSafeSet(ps, null, 4);
    }

}
