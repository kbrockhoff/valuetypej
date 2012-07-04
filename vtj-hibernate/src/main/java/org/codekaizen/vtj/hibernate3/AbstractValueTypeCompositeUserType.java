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
package org.codekaizen.vtj.hibernate3;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.codekaizen.vtj.VT;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

/**
 * <p>Abstract base class for Hibernate 3
 * {@link org.hiberante.usertype.CompositeUserType CompositeUserType}'s
 * managing immutable
 * value classes implementing {@link org.codekaizen.vtj.ValueType ValueType}
 * stored in multiple database columns.
 * </p>
 * @author <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public abstract class AbstractValueTypeCompositeUserType
        implements CompositeUserType, Serializable {

	private static final long serialVersionUID = -7695585524650668362L;

	private final Class<?> clazz;
    private final Type[] types;
    private final String[] names;

    /**
     * Constructs a user type object.
     *
     * @param clazz  the class returned by <code>nullSafeGet()</code>
     * @param types  the corresponding "property types"
     * @param names  the "property names" that may be used in a query
     */
    protected AbstractValueTypeCompositeUserType(Class<?> clazz, Type[] types,
            String[] names) {
    	if (!VT.class.isAssignableFrom(clazz)) {
    		throw new IllegalArgumentException(
    				"supplied class in not a subclass of "
    				+ VT.class.getName());
    	}
        this.clazz = clazz;
        this.types = types;
        this.names = names;
    }

    public Class<?> returnedClass() {
        return this.clazz;
    }

    public boolean isMutable() {
        return false;
    }

    public Object deepCopy(Object value) {
        return value;
    }

    public Serializable disassemble(Object value, SessionImplementor session) {
        return (Serializable) value;
    }

    public Object assemble(Serializable cached, SessionImplementor session,
            Object owner) {
        return cached;
    }

    public Object replace(Object original, Object target,
            SessionImplementor session, Object owner) {
        return original;
    }

    public boolean equals(Object x, Object y) {
        return !(x == null || y == null) && x.equals(y);
    }

    public int hashCode(Object x) {
        return x.hashCode();
    }

    public abstract Object nullSafeGet(ResultSet resultSet, String[] names,
            SessionImplementor session, Object owner) throws SQLException;

    public abstract void nullSafeSet(PreparedStatement statement, Object value,
            int index, SessionImplementor session) throws SQLException;

    public String[] getPropertyNames() {
        return this.names;
    }

    public Type[] getPropertyTypes() {
        return this.types;
    }

    public abstract Object getPropertyValue(Object component, int property);

    public void setPropertyValue(Object component, int property, Object value) {
        throw new UnsupportedOperationException(
                this.clazz.getName() + " is immutable");
    }

}
