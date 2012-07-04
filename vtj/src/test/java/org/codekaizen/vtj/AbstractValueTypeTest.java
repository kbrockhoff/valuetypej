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
package org.codekaizen.vtj;

import static org.testng.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.SerializationUtils;
import org.codekaizen.vtj.enums.EnumValueType;
import org.testng.annotations.Test;


/**
 * <p>Base class for unit testing implementations of {@link ValueType}. It tests serialization and the following methods
 * all value types should implement.</p>
 *
 * <ul>
 * <li><code>copy()</code></li>
 * <li><code>compareTo(T o)</code></li>
 * <li><code>hashCode()</code></li>
 * <li><code>equals(Object obj)</code></li>
 * <li><code>toString()</code></li>
 * </ul>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public abstract class AbstractValueTypeTest {

    private final Class<?> testClass;
    private final Class<?>[] constructorArgClasses;
    private final Object[] constructorArgs;
    private final boolean enumType;
    private EnumValueType<?> defaultEnum;
    private Constructor<?> constructor;

    /**
     * Constructs a {@link ValueType} implementation unit test.
     *
     * @param  testCls  the concrete class being tested
     * @param  cnstrtCls  the class(es) of the arguments for the default constructor
     * @param  cnstrtArgs  the constructor arguments to use
     *
     * @throws  IllegalArgumentException  if an invalid parameter is passed in
     */
    protected AbstractValueTypeTest(final Class<?> testCls, final Class<?>[] cnstrtCls, final Object[] cnstrtArgs) {
        super();
        AssertPrecondition.notNull("testCls", testCls);
        AssertPrecondition.isAssignable(ValueType.class, testCls);
        AssertPrecondition.notNull("cnstrsCls", cnstrtCls);
        AssertPrecondition.notNull("cnstrsArgs", cnstrtArgs);

        if (cnstrtCls.length != cnstrtArgs.length) {
            throw new IllegalArgumentException("the number of arguments " +
                    "does not match the constructor param classes count");
        }

        enumType = EnumValueType.class.isAssignableFrom(testCls);
        testClass = testCls;
        constructorArgClasses = cnstrtCls;
        constructorArgs = cnstrtArgs;
        validateConstruction();
    }

    private void validateConstruction() throws IllegalStateException, SecurityException, IllegalArgumentException {

        if (enumType) {

            // assume it is a Java 5 enum
            try {
                final Method m = testClass.getMethod("values", (Class<?>[]) null);
                final Enum<?>[] vals = (Enum<?>[]) m.invoke((Object) null);
                defaultEnum = (EnumValueType<?>) vals[0];
            } catch (final Exception e) {
                throw new IllegalStateException(e);
            }
        } else {

            try {
                constructor = getTestClass().getConstructor(constructorArgClasses);
            } catch (final NoSuchMethodException nsme) {
                throw new IllegalArgumentException("constructor parameter classes are not valid", nsme);
            }

            try {
                constructor.newInstance(constructorArgs);
            } catch (InvocationTargetException ite) {
                throw new IllegalArgumentException("constructor arguments are not valid", ite.getCause());
            } catch (final Exception e) {
                throw new IllegalStateException("unable to construct test object", e);
            }
        }
    }

    /**
     * Returns the implementing class being tested.
     *
     * @return  the class
     */
    protected final Class<?> getTestClass() {
        return testClass;
    }

    /**
     * Returns an instance of the class being tested populated with the default values.
     *
     * @return  the constructed object or <code>null</code> if problems which should not happen because it is checked in
     *          the constructor
     */
    protected final ValueType<?> getDefaultTestInstance() {

        if (enumType) {
            return defaultEnum;
        } else {

            try {
                return (ValueType<?>) constructor.newInstance(constructorArgs);
            } catch (final Exception e) {
                return null;
            }
        }
    }

    /**
     * Verifies the class has no setter methods.
     */
    @Test(groups = { "api" })
    public final void shouldBeImmutable() {
        final Method[] methods = getTestClass().getMethods();

        for (final Method method : methods) {

            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            assertFalse(method.getName().startsWith("set"));
        }
    }

    /**
     * Verifies that comparison with the same instance returns equals=<code>true</code>.
     */
    @Test(groups = { "api" })
    public final void shouldBeEqualsForSameObject() {
        final ValueType<?> vt1 = getDefaultTestInstance();
        assertTrue(vt1.equals(vt1));
        assertEquals(vt1.hashCode(), vt1.hashCode());
    }

    /**
     * Verifies that passing in a <code>null</code> to equals returns <code>false</code> and does not throw a <code>
     * NullPointerException</code>.
     */
    @Test(groups = { "api" })
    public final void shouldBeNotEqualsForNull() {
        final ValueType<?> vt1 = getDefaultTestInstance();
        assertFalse(vt1.equals(null));
    }

    /**
     * Verifies that equals compares on object contents instead of the default identity by passing in another instance
     * to equals with the same underlying value.
     */
    @Test(groups = { "api" })
    public final void shouldBeEqualsForEquivalentButDifferentObject() {
        final ValueType<?> vt1 = getDefaultTestInstance();
        final ValueType<?> vt2 = getDefaultTestInstance();

        if (!(vt1 instanceof Enum)) {
            assertFalse(vt1 == vt2);
        }

        assertTrue(vt1.equals(vt2));
        assertEquals(vt1.hashCode(), vt2.hashCode());
    }

    /**
     * Verifies that equals returns <code>false</code> for two instances of the class being tested that wrap different
     * content.
     */
    @Test(groups = { "api" })
    public final void shouldBeNotEqualsForNonEquivalentObjectsOfSameType() {
        doNonEqualsTesting();
    }

    /**
     * Verifies that equals returns <code>false</code> if an instance of a different class is passed in and does not
     * throw a <code>ClassCastException</code>.
     */
    @Test(groups = { "api" })
    public final void shouldBeNotEqualsForDifferentType() {
        final ValueType<?> vt1 = getDefaultTestInstance();
        assertFalse(vt1.equals("A non ValueType object"));
        assertFalse(vt1.equals(vt1.toString()));
    }

    /**
     * Verifies that <code>copy</code> returns a new instance.
     */
    @Test(groups = { "api" })
    public final void shouldReturnEquivalentCopyButNotSameObjectUnlessEnum() {
        final ValueType<?> vt1 = getDefaultTestInstance();
        final ValueType<?> vt2 = (ValueType<?>) vt1.copy();
        assertEquals(vt2, vt1);

        if (!enumType) {
            assertNotSame(vt1, vt2);
        }
    }

    /**
     * Verifies that class implements <code>Comparable</code> correctly.
     */
    @Test(groups = { "api" })
    public final void shouldImplementComparable() {
        final ValueType<?> vt1 = getDefaultTestInstance();
        assertTrue(vt1 instanceof Comparable);
        doCompareToTesting();
    }

    /**
     * Verifies that a <code>NullPointerException</code> is thrown if <code>null</code> is passed into <code>
     * compareTo</code>.
     */
    @Test(
        expectedExceptions = NullPointerException.class,
        groups = { "api" }
    )
    public final void shouldThrowExceptionComparingNull() {
        final ValueType<?> vt1 = getDefaultTestInstance();
        vt1.compareTo(null);
    }

    /**
     * Verifies that <code>Serializable</code> is implemented correctly.
     */
    @Test(groups = { "api" })
    public final void shouldImplementSerialization() {
        final ValueType<?> vt1 = getDefaultTestInstance();
        final ValueType<?> vt2 = (ValueType<?>) SerializationUtils.deserialize(SerializationUtils.serialize(vt1));
        assertEquals(vt2, vt1);
    }

    /**
     * Verifies that <code>toString</code> produces a unique value that enables a parser to identify the string as being
     * produced by this particular class.
     */
    @Test(groups = { "api" })
    public final void shouldOverrideStringWithUniquelyFormattedValue() {
        final ValueType<?> vt1 = getDefaultTestInstance();
        assertFalse(vt1.toString().equals(ObjectUtils.identityToString(vt1)));
        doToStringTesting();
    }

    /**
     * Abstract method which does additional <code>compareTo</code> testing.
     */
    protected abstract void doCompareToTesting();

    /**
     * Abstract method which does additional <code>equals</code> testing for non-equal instances of the class being
     * tested.
     */
    protected abstract void doNonEqualsTesting();

    /**
     * Abstract method which does additional testing of the <code>toString</code> method.
     */
    protected abstract void doToStringTesting();

}
