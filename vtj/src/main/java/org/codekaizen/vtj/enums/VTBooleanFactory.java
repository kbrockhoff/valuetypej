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
package org.codekaizen.vtj.enums;

import org.codekaizen.vtj.AbstractVTFactory;
import org.codekaizen.vtj.ContextHandlingStrategy;
import org.codekaizen.vtj.ValueType;


/**
 * <p>Base object factory for {@link VTBoolean}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTBooleanFactory extends AbstractVTFactory<VTBoolean> {

    private static final long serialVersionUID = -5648218760462670297L;
    static final String[] VALID_STRINGS = { "true", "yes", "y", "1", "false", "no", "n", "0", };
    static final int FIRST_FALSE = 4;

    /**
     * Constructs a factory.
     *
     * @param  strategy  the strategy for extracting context values
     */
    public VTBooleanFactory(final ContextHandlingStrategy strategy) {
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
        return VTBoolean.class.equals(clazz);
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
    protected VTBoolean create(final Object... args) {

        if (args == null || args.length == 0 || args[0] == null) {
            return VTBoolean.UNKNOWN;
        } else if (args[0] instanceof Boolean) {
            return this.trueOrFalse((Boolean) args[0]);
        } else if (args[0] instanceof Number) {
            return this.trueOrFalse(((Number) args[0]).doubleValue() != 0.0);
        } else if (args[0] instanceof CharSequence) {
            return this.parse((CharSequence) args[0]);
        } else {
            throw new IllegalArgumentException("cannot convert to a boolean");
        }
    }

    private VTBoolean trueOrFalse(final boolean b) {

        if (b) {
            return VTBoolean.TRUE;
        } else {
            return VTBoolean.FALSE;
        }
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

        if (s == null) {
            return false;
        }

        for (final String validStr : VALID_STRINGS) {

            if (validStr.equalsIgnoreCase(s.toString())) {
                return true;
            }
        }

        for (int i = 0; i < VTBoolean.values().length; i++) {

            if (VTBoolean.values()[i].name().equalsIgnoreCase(s.toString())) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    @Override
    protected VTBoolean doParse(final CharSequence s) {

        if (VTBoolean.UNKNOWN.name().equals(s)) {
            return VTBoolean.UNKNOWN;
        }

        for (int i = 0; i < FIRST_FALSE; i++) {

            if (VALID_STRINGS[i].equalsIgnoreCase(s.toString())) {
                return this.trueOrFalse(true);
            }
        }

        for (int i = FIRST_FALSE; i < VALID_STRINGS.length; i++) {

            if (VALID_STRINGS[i].equalsIgnoreCase(s.toString())) {
                return this.trueOrFalse(false);
            }
        }

        throw new IllegalArgumentException("not convertible to true or false");
    }

    /**
     * DOCUMENT ME!
     *
     * @param  vt  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String format(final VTBoolean vt) {
        return vt.name();
    }

}
