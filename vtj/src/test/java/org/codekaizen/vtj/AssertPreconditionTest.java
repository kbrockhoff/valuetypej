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

import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link AssertPrecondition}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class AssertPreconditionTest {

    /**
     * Creates a new AssertPreconditionTest object.
     */
    public AssertPreconditionTest() {
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowNotNullExceptionOnNullParameter() {
        final String param1 = null;
        AssertPrecondition.notNull("param1", param1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldNotThrowNotNullExceptionOnNonNullParameter() {
        final String param1 = "VALUE";
        AssertPrecondition.notNull("param1", param1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowNotBlankExceptionOnNullString() {
        final String param1 = null;
        AssertPrecondition.notBlank("param1", param1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowNotBlankExceptionOnZeroLengthString() {
        final String param1 = "";
        AssertPrecondition.notBlank("param1", param1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowNotBlankExceptionOnSpacesOnlyString() {
        final String param1 = "\t ";
        AssertPrecondition.notBlank("param1", param1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldNotThrowNotBlankExceptionOnRealString() {
        final String param1 = "VALUE";
        AssertPrecondition.notBlank("param1", param1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowWithinRangeExceptionIfLessThanLowerBound() {
        final Integer param1 = Integer.valueOf(-1);
        AssertPrecondition.withinRange("param1", param1, Integer.valueOf(0), Integer.valueOf(100));
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowWithinRangeExceptionIfGreaterThanUpperBound() {
        final Integer param1 = Integer.valueOf(101);
        AssertPrecondition.withinRange("param1", param1, Integer.valueOf(0), Integer.valueOf(100));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldNotThrowWithinRangeExceptionIfAtLowerBound() {
        final Integer param1 = Integer.valueOf(0);
        AssertPrecondition.withinRange("param1", param1, Integer.valueOf(0), Integer.valueOf(100));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldNotThrowWithinRangeExceptionIfAtUpperBound() {
        final Integer param1 = Integer.valueOf(100);
        AssertPrecondition.withinRange("param1", param1, Integer.valueOf(0), Integer.valueOf(100));
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowIsAssignableExceptionOnNonSubclass() {
        final String param1 = "VALUE";
        AssertPrecondition.isAssignable(Number.class, param1.getClass());
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowIsAssignableExceptionOnNullSubclass() {
        AssertPrecondition.isAssignable(Number.class, null);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void shouldNotThrowIsAssignableExceptionOnSubclass() {
        final String param1 = "VALUE";
        AssertPrecondition.isAssignable(CharSequence.class, param1.getClass());
    }

}
