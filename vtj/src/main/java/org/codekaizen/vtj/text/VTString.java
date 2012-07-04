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
package org.codekaizen.vtj.text;

import java.io.IOException;
import org.codekaizen.vtj.AssertPrecondition;
import org.codekaizen.vtj.VT;


/**
 * <p>Represents a string which is not a formal name. This class is the functional equivalent of {@link java.lang.String
 * String}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTString extends VT<VTString> implements CharSequence, Appendable {

    private static final long serialVersionUID = -1858437542176407890L;

    private final String value;

    /**
     * Constructs a string object.
     *
     * @param  val  the string to wrap
     */
    public VTString(final CharSequence val) {
        AssertPrecondition.notNull("val", val);
        this.value = val.toString();
    }

    /**
     * Adds or appends the supplied string to the end of this string and returns the new combined string.
     *
     * @param  str  the characters to put on the end of this string
     *
     * @return  the new string
     */
    public VTString plus(final CharSequence str) {
        return new VTString(this.value + str.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public VTString copy() {
        return new VTString(this.value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public char charAt(final int index) {
        return this.value.charAt(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int length() {
        return this.value.length();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  start  DOCUMENT ME!
     * @param  end  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public CharSequence subSequence(final int start, final int end) {
        return new VTString(this.value.subSequence(start, end));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public Appendable append(final char c) throws IOException {
        return new VTString(this.value + c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  csq  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public Appendable append(final CharSequence csq) throws IOException {
        return new VTString(this.value + csq);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  csq  DOCUMENT ME!
     * @param  start  DOCUMENT ME!
     * @param  end  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public Appendable append(final CharSequence csq, final int start, final int end) throws IOException {
        return new VTString(this.value + csq.subSequence(start, end));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  o  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int compareTo(final VTString o) {
        return this.value.compareTo(o.value);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int hashCode() {
        return 31 * this.value.hashCode();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  obj  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof VTString)) {
            return false;
        }

        final VTString other = (VTString) obj;

        return this.value.equals(other.value);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String toString() {
        return this.value;
    }

}
