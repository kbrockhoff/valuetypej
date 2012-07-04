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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.codekaizen.vtj.enums.VTIsoGender;
import org.codekaizen.vtj.enums.VTIsoGenderFactory;
import org.codekaizen.vtj.hibernate3.AbstractValueTypeUserType;
import org.hibernate.HibernateException;


/**
 * <p>Hibernate 3 user type for handling {@link VTIsoGender} values.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTIsoGenderUserType extends AbstractValueTypeUserType<VTIsoGender, VTIsoGenderFactory> {

    private static final long serialVersionUID = 4978652630222786932L;

    /**
     * Creates a new VTIsoGenderUserType object.
     */
    public VTIsoGenderUserType() {
        super(VTIsoGender.class, Types.INTEGER, VTIsoGenderFactory.class);
    }

    @Override
    protected void convertToDbType(final PreparedStatement st, final VTIsoGender value, final int index)
        throws SQLException {
        st.setInt(index, value.getDigitCode());
    }

    @Override
    public Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner) throws HibernateException,
        SQLException {
        final Number val = (Number) rs.getObject(names[0]);

        if (rs.wasNull()) {
            return VTIsoGender.NOT_KNOWN;
        } else {
            return VTIsoGender.getEnumValue(val.intValue());
        }
    }

    @Override
    protected VTIsoGender convertToType(final Object val) {
        return null;  // do nothing because overrode calling method
    }

}
