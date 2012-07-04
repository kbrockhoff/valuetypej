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
package org.codekaizen.vtj.eclipselink;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.codekaizen.vtj.ContextHandlingStrategy;
import org.codekaizen.vtj.MapContextHandlingStrategy;
import org.codekaizen.vtj.ValueType;
import org.codekaizen.vtj.ValueTypeFactory;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;

/**
 * <p>Abstract base class for EclipseLink converters managing immutable
 * value classes implementing {@link org.codekaizen.vtj.ValueType ValueType}
 * stored in a single database column.
 * </p>
 * @author <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public abstract class AbstractValueTypeConverter<T extends ValueType<?>, 
        F extends ValueTypeFactory<?>> 
        implements Converter {

    private static final long serialVersionUID = 2956257077190588872L;
	
    private final Class<?> clazz;
    private final Class<F> factoryClazz;
    private F factory;

    protected AbstractValueTypeConverter(final Class<?> clazz,
            final Class<F> factoryClazz) {
        if (!ValueType.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(
                    "supplied class in not a subclass of "
                    + ValueType.class.getName());
        }
        this.clazz = clazz;
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
                throw new IllegalStateException(
                        "cannot instantiate ValueTypeFactory", e);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(
                        "cannot instantiate ValueTypeFactory", e);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException(
                        "cannot instantiate ValueTypeFactory", e);
            } catch (InstantiationException e) {
                throw new IllegalStateException(
                        "cannot instantiate ValueTypeFactory", e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(
                        "cannot instantiate ValueTypeFactory", e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException(
                        "cannot instantiate ValueTypeFactory", 
                        e.getTargetException());
            }
        }
        return this.factory;
    }

    protected Class<?> returnedClass() {
        return this.clazz;
    }

    public boolean isMutable() {
        return false;
    }

    public void initialize(final DatabaseMapping mapping, 
    		final Session session) {
        // normally do nothing
    }

    public Object convertDataValueToObjectValue(final Object dataValue,
            final Session session) {
        if (dataValue == null) {
            return null;
        } else {
            return convertToType(dataValue);
        }
    }
    
    @SuppressWarnings("unchecked")
    public Object convertObjectValueToDataValue(final Object objectValue,
            final Session session) {
        if (objectValue == null) {
            return null;
        } else if (!(this.clazz.isAssignableFrom(objectValue.getClass()))) {
            throw new IllegalArgumentException("supplied value must of type "
                    + this.clazz.getName());
        } else {
            return convertToDbType((T) objectValue);
        }
    }

    protected abstract T convertToType(Object dataValue);
    
    protected abstract Object convertToDbType(T value);
    
}
