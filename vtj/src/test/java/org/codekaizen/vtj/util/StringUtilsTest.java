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
package org.codekaizen.vtj.util;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.codekaizen.vtj.text.VTString;
import org.testng.annotations.Test;


/**
 * <p>Unit tests for {@link StringUtils}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class StringUtilsTest {

    /**
     * Creates a new StringUtilsTest object.
     */
    public StringUtilsTest() {
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "utilities" })
    public void shouldIdentifyNullAsBlank() {
        assertTrue(StringUtils.isBlank(null));
        assertFalse(StringUtils.isNotBlank(null));
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "utilities" })
    public void shouldIdentifyZeroLengthAsBlank() {
        assertTrue(StringUtils.isBlank(""));
        assertFalse(StringUtils.isNotBlank(""));
        assertTrue(StringUtils.isBlank(new StringBuilder()));
        assertFalse(StringUtils.isNotBlank(new StringBuilder()));
        assertTrue(StringUtils.isBlank(new VTString("")));
        assertFalse(StringUtils.isNotBlank(new VTString("")));
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "utilities" })
    public void shouldIdentifyWhitespaceAsBlank() {
        assertTrue(StringUtils.isBlank(" \t \n"));
        assertFalse(StringUtils.isNotBlank(" \t \n"));
    }

    /**
     * DOCUMENT ME!
     */
    @Test(groups = { "utilities" })
    public void shouldIdentifyNonWhitespaceAsNotBlank() {
        assertFalse(StringUtils.isBlank(" \tnon\n"));
        assertTrue(StringUtils.isNotBlank(" \tnon\n"));
        assertFalse(StringUtils.isBlank("1aA\u00DE"));
        assertTrue(StringUtils.isNotBlank("1aA\u00DE"));
    }

}
