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

/**
 * <p>Contains static utility methods for manipulating strings.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class StringUtils {

    private StringUtils() {
        // non-instantiable
    }

    /**
     * Returns whether the supplied string is <code>null</code> or is all whitespace.
     *
     * @param  value  the string to evaluate
     *
     * @return  is null or all whitespace or not
     */
    public static boolean isBlank(final CharSequence value) {
        final int len;

        if (value == null || (len = value.length()) == 0) {
            return true;
        }

        for (int i = 0; i < len; i++) {

            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns whether the supplied string contains characters or not.
     *
     * @param  value  the string to evaluate
     *
     * @return  has any non-whitespace characters or not
     */
    public static boolean isNotBlank(final CharSequence value) {
        return !isBlank(value);
    }

}
