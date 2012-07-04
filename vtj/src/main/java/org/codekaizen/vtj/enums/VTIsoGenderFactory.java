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
 * <p>Base object factory for {@link VTIsoGender}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTIsoGenderFactory extends AbstractVTFactory<VTIsoGender> {

    private static final long serialVersionUID = 715223545859384013L;
    static final String[] VALID_STRINGS = {
            "NOT_KNOWN", "0", "MALE", "M", "1", "FEMALE", "F", "2", "NOT_APPLICABLE", "N/A", "9",
        };
    static final int FIRST_MALE = 2;
    static final int FIRST_FEMALE = 5;
    static final int FIRST_NA = 8;

    /**
     * Constructs a factory.
     *
     * @param  strategy  the strategy to use in obtaining configuration information from the environment
     */
    public VTIsoGenderFactory(final ContextHandlingStrategy strategy) {
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
        return VTIsoGender.class.equals(clazz);
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
    protected VTIsoGender create(final Object... args) {

        if (args == null || args.length == 0 || args[0] == null) {
            return VTIsoGender.NOT_KNOWN;
        } else if (args[0] instanceof Number) {
            final int val = ((Number) args[0]).intValue();

            return VTIsoGender.getEnumValue(val);
        } else if (args[0] instanceof CharSequence) {
            return this.parse((CharSequence) args[0]);
        }

        throw new IllegalArgumentException("unable to convert to a gender");
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
    protected VTIsoGender doParse(final CharSequence s) {

        for (int i = 0; i < FIRST_MALE; i++) {

            if (VALID_STRINGS[i].equalsIgnoreCase(s.toString())) {
                return VTIsoGender.NOT_KNOWN;
            }
        }

        for (int i = FIRST_MALE; i < FIRST_FEMALE; i++) {

            if (VALID_STRINGS[i].equalsIgnoreCase(s.toString())) {
                return VTIsoGender.MALE;
            }
        }

        for (int i = FIRST_FEMALE; i < FIRST_NA; i++) {

            if (VALID_STRINGS[i].equalsIgnoreCase(s.toString())) {
                return VTIsoGender.FEMALE;
            }
        }

        for (int i = FIRST_NA; i < VALID_STRINGS.length; i++) {

            if (VALID_STRINGS[i].equalsIgnoreCase(s.toString())) {
                return VTIsoGender.NOT_APPLICABLE;
            }
        }

        throw new IllegalArgumentException("not convertible to a gender");
    }

    /**
     * DOCUMENT ME!
     *
     * @param  vt  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String format(final VTIsoGender vt) {
        return vt.name();
    }

}
