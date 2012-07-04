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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

import org.apache.commons.lang.SerializationUtils;
import org.codekaizen.vtj.ValueType;
import org.codekaizen.vtj.enums.EnumValueType;
import org.hibernate.usertype.UserType;
import org.testng.annotations.Test;

/**
 * <p>Abstract base class for testing concrete subclasses of
 * {@link AbstractValueTypeUserType}.
 * </p>
 * @author <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public abstract class AbstractValueTypeUserTypeTest {

	private final Class<? extends UserType> userTypeClazz;
	private final Class<? extends ValueType<?>> valueClazz;
    private final Class<?>[] constructorArgClasses;
    private final Object[] constructorArgs;
    private final boolean enumType;
    private EnumValueType<?> defaultEnum;
    private Constructor<?> constructor;

	protected AbstractValueTypeUserTypeTest(
			Class<? extends UserType> userTypeClazz,
			Class<? extends ValueType<?>> valueClazz,
			Class<?>[] cnstrtCls, Object[] cnstrtArgs) {
        if (userTypeClazz == null) {
            throw new NullPointerException("userTypeClazz cannot be null");
        }
        if (!UserType.class.isAssignableFrom(userTypeClazz)) {
            throw new IllegalArgumentException("AbstractValueTypeUserTypeTest "
                    + "is only for testing implementations of UserType");
        }
        if (valueClazz == null) {
            throw new NullPointerException("valueClazz cannot be null");
        }
        if (!ValueType.class.isAssignableFrom(valueClazz)) {
            throw new IllegalArgumentException("AbstractValueTypeUserTypeTest "
                    + "is only for testing implementations of ValueType");
        }
        if (cnstrtCls == null) {
            throw new NullPointerException("cnstrtCls cannot be null");
        }
        if (cnstrtArgs == null) {
            throw new NullPointerException("cnstrtArgs cannot be null");
        }
        if (cnstrtCls.length != cnstrtArgs.length) {
            throw new IllegalArgumentException("the number of arguments"
                    + "does not match the constructor param classes count");
        }
        this.enumType = EnumValueType.class.isAssignableFrom(valueClazz);
		this.userTypeClazz = userTypeClazz;
		this.valueClazz = valueClazz;
        this.constructorArgClasses = cnstrtCls;
        this.constructorArgs = cnstrtArgs;
        validateConstruction();
	}

    private void validateConstruction() throws IllegalStateException,
			SecurityException, IllegalArgumentException {
		if (this.enumType) {
			// assume it is a Java 5 enum
			try {
				Method m = this.valueClazz
						.getMethod("values", (Class<?>[]) null);
				Enum<?>[] vals = (Enum<?>[]) m.invoke((Object) null);
				this.defaultEnum = (EnumValueType<?>) vals[0];
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		} else {
			try {
				this.constructor = this.valueClazz.getConstructor(
						this.constructorArgClasses);
			} catch (NoSuchMethodException nsme) {
				throw new IllegalArgumentException(
						"constructor parameter classes are not valid", nsme);
			}
			try {
				this.constructor.newInstance(this.constructorArgs);
			} catch (InvocationTargetException ite) {
				throw new IllegalArgumentException(
						"constructor arguments are not valid", ite.getCause());
			} catch (Exception e) {
				throw new IllegalStateException(
						"unable to construct test object", e);
			}
		}
	}

    /**
     * Returns an instance of the class being tested populated with
     * the default values.
     *
     * @return  the constructed object or <code>null</code> if problems
     * which should not happen because it is checked in the constructor
     */
    protected final ValueType<?> getDefaultTestInstance() {
        if (this.enumType) {
            return this.defaultEnum;
        } else {
            try {
                return (ValueType<?>) this.constructor.newInstance(
                        this.constructorArgs);
            } catch (Exception e) {
                return null;
            }
        }
    }

	/**
	 * Returns the class for the user type being tested.
	 *
	 * @return the class being tested
	 */
	protected Class<? extends UserType> getUserTypeClazz() {
		return userTypeClazz;
	}

	/**
	 * Returns the class for the value type the user type being tested converts.
	 *
	 * @return the value type class the class being tested produces
	 */
	protected Class<? extends ValueType<?>> getValueClazz() {
		return valueClazz;
	}

	/**
	 * Returns an instance of the user type to run tests on.
	 *
	 * @return  the user type instance
	 */
	protected UserType newUserTypeInstance() {
		try {
			return this.userTypeClazz.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

	protected TestEntity newTestEntity() {
		TestEntity entity = new TestEntity();
		entity.setValue(this.getDefaultTestInstance());
		return entity;
	}

	@Test
	public void shouldReturnOneSqlTypes() {
		UserType ut = this.newUserTypeInstance();
		assertEquals(ut.sqlTypes().length, 1);
	}

	@Test
	public void shouldReturnConfiguredReturnedClass() {
		UserType ut = this.newUserTypeInstance();
		assertEquals(ut.returnedClass(), this.getValueClazz());
	}

	@Test
	public void shouldReturnImmutable() {
		UserType ut = this.newUserTypeInstance();
		assertFalse(ut.isMutable());
	}

	@Test
	public void givenVTObjectWhenDeepCopyThenReturnSameObject() {
		UserType ut = this.newUserTypeInstance();
		ValueType<?> vt1 = this.getDefaultTestInstance();
		ValueType<?> vt2 = (ValueType<?>) ut.deepCopy(vt1);
		assertEquals(vt2, vt1);
        assertTrue(vt1 == vt2);
	}

    @Test
    public void givenNullWhenDeepCopyThenReturnNull() {
        UserType ut = this.newUserTypeInstance();
        assertNull(ut.deepCopy(null));
    }

	@Test
	public void givenVTObjectWhenDisassembleThenReturnSameObject() {
		UserType ut = this.newUserTypeInstance();
		ValueType<?> vt1 = this.getDefaultTestInstance();
		ValueType<?> vt2 = (ValueType<?>) ut.disassemble(vt1);
		assertEquals(vt2, vt1);
        assertTrue(vt1 == vt2);
	}

    @Test
    public void givenNullWhenDisassembleThenReturnNull() {
        UserType ut = this.newUserTypeInstance();
        assertNull(ut.disassemble(null));
    }

	@Test
	public void givenVTObjectWhenAssembleThenReturnSameObject() {
		UserType ut = this.newUserTypeInstance();
		ValueType<?> vt1 = this.getDefaultTestInstance();
		TestEntity entity = this.newTestEntity();
		ValueType<?> vt2 = (ValueType<?>) ut.assemble(vt1, entity);
		assertEquals(vt2, vt1);
        assertTrue(vt1 == vt2);
	}

    @Test
    public void givenNullWhenAssembleThenReturnNull() {
        UserType ut = this.newUserTypeInstance();
        TestEntity entity = this.newTestEntity();
        assertNull(ut.assemble(null, entity));
    }

	@Test
	public void givenVTObjectWhenReplaceThenReturnSameObject() {
		UserType ut = this.newUserTypeInstance();
		ValueType<?> vt1 = this.getDefaultTestInstance();
		TestEntity entity = this.newTestEntity();
		TestEntity entity2 = this.newTestEntity();
		ValueType<?> vt2 = (ValueType<?>) ut.replace(vt1, entity, entity2);
		assertEquals(vt2, vt1);
        assertTrue(vt1 == vt2);
	}

    @Test
    public void givenNullWhenReplaceThenReturnNull() {
        UserType ut = this.newUserTypeInstance();
        TestEntity entity = this.newTestEntity();
        TestEntity entity2 = this.newTestEntity();
        assertNull(ut.replace(null, entity, entity2));
    }

	@Test
	public void givenEquivalentVTObjectsWhenEqualsThenReturnTrue() {
		UserType ut = this.newUserTypeInstance();
		ValueType<?> vt1 = this.getDefaultTestInstance();
		ValueType<?> vt2 = this.getDefaultTestInstance();
		assertTrue(ut.equals(vt1, vt2));
	}

    @Test
    public void givenOneOrMoreNullWhenEqualsThenReturnFalse() {
        UserType ut = this.newUserTypeInstance();
        ValueType<?> vt1 = this.getDefaultTestInstance();
        ValueType<?> vt2 = this.getDefaultTestInstance();
        assertFalse(ut.equals(null, vt2));
        assertFalse(ut.equals(vt1, null));
        assertFalse(ut.equals(null, null));
    }

	@Test
	public void givenVTObjectWhenHashCodeThenReturnObjectHashCode() {
		UserType ut = this.newUserTypeInstance();
		ValueType<?> vt = this.getDefaultTestInstance();
		assertEquals(ut.hashCode(vt), vt.hashCode());
	}

    @Test
    public void givenNullWhenHashCodeThenReturnZero() {
        UserType ut = this.newUserTypeInstance();
        assertEquals(ut.hashCode(null), 0);
    }

	@Test
	public void shouldSerializeWithoutComplaint() {
		UserType ut1 = this.newUserTypeInstance();
		UserType ut2 = (UserType) SerializationUtils.deserialize(
				SerializationUtils.serialize((Serializable) ut1));
		assertEquals(ut2.returnedClass(), ut1.returnedClass());
	}

    /** Fake entity class that Hibernate is hydrating. */
	protected class TestEntity {

		private ValueType<?> value;

		ValueType<?> getValue() {
			return value;
		}

		void setValue(ValueType<?> value) {
			this.value = value;
		}

	}

	@Test
	public abstract void givenNonNullDBValueWhenNullSafeGetThenReturnVTObject()
            throws SQLException;

	@Test
	public abstract void givenNullDBValueWhenNullSafeGetThenReturnNull()
            throws SQLException;

	@Test
	public abstract void givenVTObjectWhenNullSafeSetThenSetSingleDBValue()
            throws SQLException;

	@Test
	public abstract void givenNullWhenNullSafeSetThenSetNull()
            throws SQLException;

}
