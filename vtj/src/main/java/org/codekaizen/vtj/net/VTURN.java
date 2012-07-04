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
package org.codekaizen.vtj.net;

import java.util.List;
import org.codekaizen.vtj.text.VTString;


/**
 * <p>Represents a Uniform Resource Name.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTURN extends VTURI {

    private static final long serialVersionUID = 6859771589690828925L;

    private transient VTString subScheme;

    /**
     * Constructs a URN name object.
     *
     * @param  fullName  the complete name
     */
    public VTURN(final String fullName) {
        super(fullName);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fullName  DOCUMENT ME!
     * @param  nameParts  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    @Override
    protected String parseName(final String fullName, final List<String> nameParts) {
        final String s = super.parseName(fullName, nameParts);

        if (!"urn".equals(super.getScheme().toString())) {
            throw new IllegalArgumentException("must be urn");
        }

        try {
            final int pos = s.indexOf(':');
            this.subScheme = new VTString(s.substring(pos + 1, s.indexOf(':', pos + 1)));
        } catch (Exception e) {
            throw new IllegalArgumentException("missing sub-scheme");
        }

        return s;
    }

    /**
     * Returns the URN sub-scheme.
     *
     * @return  the sub-scheme
     */
    public VTString getSubScheme() {
        return this.subScheme;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  namePart  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    @Override
    public VTURI plus(final CharSequence namePart) {
        throw new IllegalArgumentException("unable to add parts to a URN");
    }

}
