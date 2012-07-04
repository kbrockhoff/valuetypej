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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.codekaizen.vtj.ContextHandlingStrategy;
import org.codekaizen.vtj.MapContextHandlingStrategy;
import org.codekaizen.vtj.ValueType;
import org.codekaizen.vtj.ValueTypeFactory;
import org.hibernate.HibernateException;
import org.hibernate.TypeMismatchException;
import org.hibernate.usertype.EnhancedUserType;

/**
 * <p>
 * Abstract base class for Hibernate 3 {@link org.hiberante.usertype.UserType
 * UserType}'s managing immutable value classes implementing
 * {@link org.codekaizen.vtj.ValueType ValueType} stored in a single database
 * column.
 * </p>
 *
 * @author <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public abstract class AbstractValueTypeUserType<T extends ValueType<?>,
        F extends ValueTypeFactory<?>>
        implements EnhancedUserType, Serializable {

    private static final long serialVersionUID = -6096661481376508581L;

    private final Class<T> clazz;
    private final int[] types;
    private final Class<F> factoryClazz;
    private F factory;

    /**
     * Constructs a user type object.
     *
     * @param clazz  the class returned by <code>nullSafeGet()</code>
     * @param type  a <code>java.sql.Types</code> constant
     * @param factoryClazz  the factory used to generate the returned class
     * instances
     */
    protected AbstractValueTypeUserType(final Class<T> clazz, final int type,
            final Class<F> factoryClazz) {
        if (!ValueType.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(
                    "supplied class in not a subclass of "
                            + ValueType.class.getName());
        }
        this.clazz = clazz;
        this.types = new int[1];
        this.types[0] = type;
        this.factoryClazz = factoryClazz;
    }

    protected F getFactory() {
        if (this.factory == null) {
            try {
                Constructor<?> c = this.factoryClazz
                        .getConstructor(ContextHandlingStrategy.class);
                this.factory = this.factoryClazz.cast(c
                        .newInstance(new MapContextHandlingStrategy()));
            } catch (SecurityException e) {
                throw new HibernateException(
                        "cannot instantiate ValueTypeFactory", e);
            } catch (NoSuchMethodException e) {
                throw new HibernateException(
                        "cannot instantiate ValueTypeFactory", e);
            } catch (IllegalArgumentException e) {
                throw new HibernateException(
                        "cannot instantiate ValueTypeFactory", e);
            } catch (InstantiationException e) {
                throw new HibernateException(
                        "cannot instantiate ValueTypeFactory", e);
            } catch (IllegalAccessException e) {
                throw new HibernateException(
                        "cannot instantiate ValueTypeFactory", e);
            } catch (InvocationTargetException e) {
                throw new HibernateException(
                        "cannot instantiate ValueTypeFactory",
                        e.getTargetException());
            }
        }
        return this.factory;
    }

    public final int[] sqlTypes() {
        return this.types;
    }

    public final Class<?> returnedClass() {
        return this.clazz;
    }

    public final boolean isMutable() {
        return false;
    }

    public final Object deepCopy(final Object value) {
        return value;
    }

    public final Serializable disassemble(Object value) {
        return (Serializable) value;
    }

    public final Object assemble(final Serializable cached,
                                 final Object owner) {
        return cached;
    }

    public final Object replace(final Object original, final Object target,
                                final Object owner) {
        return original;
    }

    public final boolean equals(final Object x, final Object y) {
        return !(x == null || y == null) && x.equals(y);
    }

    public final int hashCode(final Object x) {
        if (x == null) {
            return 0;
        } else {
            return x.hashCode();
        }
    }

    public Object nullSafeGet(final ResultSet rs, final String[] names,
                              final Object owner)
            throws HibernateException, SQLException {
        Object val = rs.getObject(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        try {
            return this.convertToType(val);
        } catch (Exception e) {
            throw new HibernateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public void nullSafeSet(final PreparedStatement st, final Object value,
                            final int index)
            throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, this.types[0]);
        } else if (!(this.clazz.isAssignableFrom(value.getClass()))) {
            throw new TypeMismatchException("Supplied object must be of type "
                    + this.clazz.getName());
        } else {
            this.convertToDbType(st, (T) value, index);
        }
    }

    protected abstract T convertToType(Object val);

    protected abstract void convertToDbType(PreparedStatement st, T value,
            int index) throws SQLException;

    public Object fromXMLString(final String xmlValue) {
        if (!this.getFactory().isParsable(xmlValue)) {
            throw new TypeMismatchException("Supplied object must be of type "
                    + this.clazz.getName());
        }
        return this.getFactory().parse(xmlValue);
    }

    public String objectToSQLString(final Object value) {
        if (value == null) {
            return "IS NULL";
        } else if (!(this.clazz.isAssignableFrom(value.getClass()))) {
            throw new TypeMismatchException("Supplied object must be of type "
                    + this.clazz.getName());
        } else {
            return null;
        }
    }

    public String toXMLString(final Object value) {
        if (value == null) {
            return "";
        } else if (!(this.clazz.isAssignableFrom(value.getClass()))) {
            throw new TypeMismatchException("Supplied object must be of type "
                    + this.clazz.getName());
        } else {
            return null;
        }
    }

}
