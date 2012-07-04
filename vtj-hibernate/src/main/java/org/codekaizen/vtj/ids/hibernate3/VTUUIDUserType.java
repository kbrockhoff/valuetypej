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

import org.codekaizen.vtj.ValueType;
import org.codekaizen.vtj.hibernate3.AbstractValueTypeUserType;
import org.codekaizen.vtj.ids.VTUUID;
import org.codekaizen.vtj.ids.VTUUIDFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;


/**
 * <p>Hibernate 3 user type for handling {@link VTUUID} values stored as
 * <code>VARBINARY(16)</code>;s in the database.
 * </p>
 *
 * @author <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTUUIDUserType
        extends AbstractValueTypeUserType<VTUUID, VTUUIDFactory> {

    public VTUUIDUserType() {
        super(VTUUID.class, Types.VARBINARY, VTUUIDFactory.class);
    }

    @Override
    protected VTUUID convertToType(final Object val) {
        return super.getFactory().create((Class<? extends ValueType<?>>)
                super.returnedClass(), val);
    }

    @Override
    protected void convertToDbType(final PreparedStatement st,
            final VTUUID value, final int index) throws SQLException {
        st.setBytes(index, value.toByteArray());
    }

}
