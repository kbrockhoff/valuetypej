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

import java.text.MessageFormat;
import java.util.ResourceBundle;


/**
 * <p>Provides static utility methods for validating method and constructor preconditions.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public final class AssertPrecondition {

    private static final ResourceBundle EXCEPTION_RESOURCES;

    static {
        EXCEPTION_RESOURCES = ResourceBundle.getBundle("org.codekaizen.vtj.ExceptionResources");
    }

    private AssertPrecondition() {
        super();  // static methods only
    }

    /**
     * Throws an informative exception if the supplied parameter is <code>null</code>. Should be used to verify input
     * parameters are not null where required values are needed.
     *
     * @param  paramName  the parameter name
     * @param  param  the supplied parameter
     *
     * @throws  IllegalArgumentException  if the parameter is <code>null</code>
     */
    public static void notNull(final String paramName, final Object param) throws IllegalArgumentException {

        if (param == null) {
            throw new IllegalArgumentException(MessageFormat.format(
                    EXCEPTION_RESOURCES.getString("org.codekaizen.vtj.precondition.notNull"), paramName));
        }
    }

    /**
     * Throws an informative exception if the supplied parameter is <code>null</code>, zero length or all whitespace.
     * Should be used to verify input parameters are not null where required values are needed.
     *
     * @param  paramName  the parameter name
     * @param  param  the supplied parameter
     *
     * @throws  IllegalArgumentException  if the parameter is blank
     */
    public static void notBlank(final String paramName, final CharSequence param) throws IllegalArgumentException {

        if (isBlank(param)) {
            throw new IllegalArgumentException(MessageFormat.format(
                    EXCEPTION_RESOURCES.getString("org.codekaizen.vtj.precondition.notBlank"), paramName));
        }
    }

    /**
     * Throws an informative exception if the supplied parameter is not between the lower and upper bounds inclusive.
     *
     * @param  paramName  the parameter name
     * @param  param  the supplied parameter
     * @param  lowerBound  the lowest permitted value
     * @param  upperBound  the highest permitted value
     *
     * @throws  IllegalArgumentException  if parameter is outside the specified range
     */
    @SuppressWarnings("unchecked")
    public static void withinRange(final String paramName, final Comparable param, final Comparable lowerBound,
            final Comparable upperBound) throws IllegalArgumentException {
        notNull(paramName, param);
        notNull("lowerBound", lowerBound);
        notNull("upperBound", upperBound);

        if (param.compareTo(lowerBound) < 0 || param.compareTo(upperBound) > 0) {
            throw new IllegalArgumentException(MessageFormat.format(
                    EXCEPTION_RESOURCES.getString("org.codekaizen.vtj.precondition.withinRange"), paramName, lowerBound,
                    upperBound));
        }
    }

    /**
     * Throws an informative exception if the supplied parameter is not between the lower and upper bounds inclusive.
     *
     * @param  paramName  the parameter name
     * @param  param  the supplied parameter
     * @param  lowerBound  the lowest permitted value
     * @param  upperBound  the highest permitted value
     *
     * @throws  IllegalArgumentException  if parameter is outside the specified range
     */
    public static void withinRange(final String paramName, final double param, final double lowerBound,
            final double upperBound) throws IllegalArgumentException {
        notNull(paramName, param);
        notNull("lowerBound", lowerBound);
        notNull("upperBound", upperBound);

        if (param < lowerBound || param > upperBound) {
            throw new IllegalArgumentException(MessageFormat.format(
                    EXCEPTION_RESOURCES.getString("org.codekaizen.vtj.precondition.withinRange"), paramName, lowerBound,
                    upperBound));
        }
    }

    /**
     * Throws an informative exception if the supplied subclass cannot be cast to the supplied super class or the
     * subclass is <code>null</code>.
     *
     * @param  superType  the super class or interface
     * @param  subType  the subclass
     *
     * @throws  IllegalArgumentException  if the subType is not a true subclass
     */
    public static void isAssignable(final Class superType, final Class subType) throws IllegalArgumentException {
        notNull("superType", superType);

        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(MessageFormat.format(
                    EXCEPTION_RESOURCES.getString("org.codekaizen.vtj.precondition.isAssignable"), subType, superType));
        }
    }

    /**
     * Throws an informative exception if the supplied double value is one of the IEEE 754 special floating point values
     * such as <code>NaN</code> or an infinity value.
     *
     * @param  paramName  the parameter name
     * @param  param  the supplied parameter
     *
     * @throws  IllegalArgumentException  if supplied parameter is a special number
     */
    public static void nonSpecialFloatingPointNumber(final String paramName, final double param)
        throws IllegalArgumentException {

        if (Double.isInfinite(param) || Double.isNaN(param)) {
            throw new IllegalArgumentException(MessageFormat.format(
                    EXCEPTION_RESOURCES.getString("org.codekaizen.vtj.precondition.nonSpecialFloatingPointNumber"),
                    paramName));
        }
    }

    /**
     * Returns whether the supplied string is <code>null</code> or is all whitespace.
     *
     * @param  value  the string to evaluate
     *
     * @return  is null or all whitespace or not
     */
    private static boolean isBlank(final CharSequence value) {
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

}
