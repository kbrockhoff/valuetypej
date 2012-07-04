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
package org.codekaizen.vtj.math;

import org.codekaizen.vtj.AbstractVTFactory;
import org.codekaizen.vtj.ContextHandlingStrategy;
import org.codekaizen.vtj.ValueType;


/**
 * <p>Factory class for {@link VTFraction}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTFractionFactory extends AbstractVTFactory<VTFraction> {

    /**
     * Creates a new VTFractionFactory object.
     *
     * @param  strategy  DOCUMENT ME!
     */
    public VTFractionFactory(final ContextHandlingStrategy strategy) {
        super(strategy);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  clazz  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean isCreatable(final Class<? extends ValueType<?>> clazz) {
        return VTFraction.class.equals(clazz);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    @Override
    protected VTFraction create(final Object... args) {

        if (args == null || args.length == 0 || args[0] == null) {
            throw new IllegalArgumentException("cannot be empty");
        }

        VTFraction result = null;

        if (args.length == 1) {

            if (args[0] instanceof VTFraction) {
                result = ((VTFraction) args[0]).copy();
            } else if (args[0] instanceof VTNumber) {
                result = new VTFraction(((VTNumber) args[0]).longValue(), 1L);
            } else if (args[0] instanceof Number) {
                result = new VTFraction(((Number) args[0]).longValue(), 1L);
            } else if (isParsable(args[0].toString())) {
                result = parse(args[0].toString());
            } else {
                throw new IllegalArgumentException("cannot compute fraction");
            }
        } else if (args.length == 2) {

            if (args[0] instanceof VTFraction) {

                if (args[1] instanceof VTNumber) {
                    result = ((VTFraction) args[0]).div((VTNumber) args[1]);
                } else if (args[1] instanceof Number) {
                    result = ((VTFraction) args[0]).div(new VTDouble(((Number) args[1]).doubleValue()));
                } else {
                    throw new IllegalArgumentException("is not a number");
                }
            } else {
                long num = 0L;
                long den = 0L;

                if (args[0] instanceof VTNumber) {
                    num = ((VTNumber) args[0]).longValue();
                } else if (args[0] instanceof Number) {
                    num = ((Number) args[0]).longValue();
                }

                if (args[1] instanceof VTNumber) {
                    den = ((VTNumber) args[1]).longValue();
                } else if (args[1] instanceof Number) {
                    den = ((Number) args[1]).longValue();
                }

                result = new VTFraction(num, den);
            }
        } else {
            throw new IllegalArgumentException("too many arguments");
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean isParsable(final CharSequence s) {

        if (s == null || s.length() < 3) {
            return false;
        }

        boolean hasNum = false;
        boolean hasSlash = false;
        boolean hasDenom = false;

        for (int i = 0; i < s.length(); i++) {

            if (Character.isWhitespace(s.charAt(i))) {
                continue;
            }

            if (!hasNum) {

                if (Character.isDigit(s.charAt(i))) {
                    hasNum = true;
                }
            } else if (!hasSlash) {

                if ('/' == s.charAt(i)) {
                    hasSlash = true;
                }
            } else {

                if (Character.isDigit(s.charAt(i))) {
                    hasDenom = true;
                }
            }
        }

        return hasNum && hasSlash && hasDenom;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected VTFraction doParse(final CharSequence s) {
        long num = 0L;
        long den = 0L;
        boolean hasSlash = false;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {

            if (Character.isWhitespace(s.charAt(i))) {
                continue;
            }

            if (!hasSlash) {

                if (Character.isDigit(s.charAt(i))) {
                    sb.append(s.charAt(i));
                } else if ('/' == s.charAt(i)) {
                    hasSlash = true;
                    num = Long.parseLong(sb.toString());
                    sb = new StringBuilder();
                }
            } else {

                if (Character.isDigit(s.charAt(i))) {
                    sb.append(s.charAt(i));
                }
            }
        }

        den = Long.parseLong(sb.toString());

        return new VTFraction(num, den);
    }

}
