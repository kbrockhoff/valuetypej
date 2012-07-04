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
package org.codekaizen.vtj.names;

import static org.testng.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;
import org.codekaizen.vtj.AbstractValueTypeTest;
import org.testng.annotations.Test;


/**
 * <p>Base class for unit testing concrete subclasses of {@link VTName}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public abstract class AbstractNameTypeTest extends AbstractValueTypeTest {

    /**
     * Constructs a {@link VTName} subclass unit test.
     *
     * @param  testCls  the concrete class being tested
     * @param  cnstrtCls  the class(es) of the arguments for the default constructor
     * @param  cnstrtArgs  the constructor arguments to use
     */
    protected AbstractNameTypeTest(final Class<?> testCls, final Class<?>[] cnstrtCls, final Object[] cnstrtArgs) {
        super(testCls, cnstrtCls, cnstrtArgs);
    }

    /**
     * Returns valid strings for use in constructor arguments.
     *
     * @return  the test string parameters
     */
    protected abstract String[] getValidPatterns();

    /**
     * Returns invalid string for use in constructor arguments.
     *
     * @return  the test string parameters
     */
    protected abstract String[] getInvalidPatterns();

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldMatchSuppliedRegexPatterns() {
        final VTName<?> n = (VTName<?>) super.getDefaultTestInstance();
        final Pattern p = n.getPattern();
        String[] patterns = this.getValidPatterns();

        for (int i = 0; i < patterns.length; i++) {
            assertTrue(p.matcher(patterns[i]).matches(), patterns[i]);
        }

        patterns = this.getInvalidPatterns();

        for (int i = 0; i < patterns.length; i++) {
            assertFalse(p.matcher(patterns[i]).matches(), patterns[i]);
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldImplementStringMatchingRegexConstructor() {
        Constructor<?> c = null;

        try {
            c = super.getTestClass().getConstructor(String.class);
        } catch (NoSuchMethodException nsme) {
            fail("missing String constructor");
        }

        VTName<?> n = null;
        String[] patterns = this.getValidPatterns();

        for (int i = 0; i < patterns.length; i++) {

            try {
                n = (VTName<?>) c.newInstance(patterns[i]);
            } catch (InvocationTargetException e) {
                fail(e.getTargetException().getMessage());
            } catch (Exception e) {
                fail(e.getMessage());
            }

            assertTrue(n.getFullName().length() > 0);
        }

        patterns = this.getInvalidPatterns();

        for (int i = 0; i < patterns.length; i++) {

            try {
                n = (VTName<?>) c.newInstance(patterns[i]);
                fail("should have failed constructing from " + patterns[i]);
            } catch (InvocationTargetException e) {
                // do nothing
            } catch (Exception e) {
                fail(e.getMessage());
            }

            assertTrue(n.getFullName().length() > 0);
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public abstract void shouldAddNamePartsAsExpectedUsingFluentAPI();

    /**
     * DOCUMENT ME!
     */
    @Test
    public abstract void shouldImplementStartsWithAsExpectedUsingFluentAPI();

    /**
     * DOCUMENT ME!
     */
    @Test
    public abstract void shouldImplementEndsWithAsExpectedUsingFluentAPI();

    /**
     * DOCUMENT ME!
     */
    @Test
    public abstract void shouldReturnNumberOfNamePartsAsSize();

    /**
     * DOCUMENT ME!
     */
    @Test
    public abstract void shouldImplementCharAtPerCharSequenceAPI();

    /**
     * DOCUMENT ME!
     */
    @Test
    public abstract void shouldImplementLengthPerCharSequenceAPI();

    /**
     * DOCUMENT ME!
     */
    @Test
    public abstract void shouldImplementSubSequencePerCharSequenceAPI();

    /**
     * DOCUMENT ME!
     */
    @Test
    public abstract void shouldIterateOverNameParts();

}
