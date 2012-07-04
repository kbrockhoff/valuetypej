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

/**
 * <p>Enumerates a representation of human sexes through a language-neutral single-digit code as defined by <strong>ISO
 * 5218</strong>.</p>
 *
 * <p>The four codes specified in ISO 5218 are:</p>
 *
 * <ul>
 * <li>0 = not known,</li>
 * <li>1 = male,</li>
 * <li>2 = female,</li>
 * <li>9 = not applicable.</li>
 * </ul>
 *
 * <p>The standard specifies that its use may be referred to by the designator "SEX".</p>
 *
 * <p><a href="http://en.wikipedia.org/wiki/ISO_5218">Wikipedia article</a></p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public enum VTIsoGender implements EnumValueType<VTIsoGender> {

    /** Gender has not been collected. */
    NOT_KNOWN(0),

    /** A male person. */
    MALE(1),

    /** A female person. */
    FEMALE(2),

    /** A person has specifically declined to specify gender. */
    NOT_APPLICABLE(9);

    private final int digitCode;

    private VTIsoGender(final int cd) {
        this.digitCode = cd;
    }

    /**
     * Returns the single-digit code.
     *
     * @return  the code
     */
    public int getDigitCode() {
        return this.digitCode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public VTIsoGender copy() {
        return this;
    }

    /**
     * Returns the enumeration value object matching the supplied ISO single-digit code.
     *
     * @param  digitCode  the single-digit code
     *
     * @return  the enum value
     *
     * @throws  IllegalArgumentException  if supplied an invalid code
     */
    public static VTIsoGender getEnumValue(final int digitCode) {

        for (int i = 0; i < VTIsoGender.values().length; i++) {

            if (VTIsoGender.values()[i].getDigitCode() == digitCode) {
                return VTIsoGender.values()[i];
            }
        }

        throw new IllegalArgumentException("Invalid code");
    }

}
