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
package org.codekaizen.vtj.names;

import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;


/**
 * <p>Represents a name in the Java programming language.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTJavaName extends VTName<VTJavaName> {

    private static final long serialVersionUID = 9064080724893744868L;
    private static final Pattern PATTERN = Pattern.compile("[A-Za-z][0-9_\\-A-Za-z]*(\\.[A-Za-z][0-9_\\-A-Za-z]*)*");

    /**
     * Constructs a Java name object.
     *
     * @param  fullName  the name
     */
    public VTJavaName(final String fullName) {
        super(fullName, 31);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fullName  DOCUMENT ME!
     * @param  nameParts  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    protected String parseName(final String fullName, final List<String> nameParts) {
        final StringBuilder sb = new StringBuilder();
        final StringTokenizer st = new StringTokenizer(fullName, ".");

        while (st.hasMoreTokens()) {
            final String part = st.nextToken().trim();

            if (part.length() == 0) {
                continue;
            }

            nameParts.add(part);

            if (sb.length() > 0) {
                sb.append('.');
            }

            sb.append(part);
        }

        return sb.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Pattern getPattern() {
        return PATTERN;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  namePart  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTJavaName plus(final CharSequence namePart) {
        super.validateNotBlank(namePart, "namePart");

        if (namePart.charAt(0) == '.') {
            return new VTJavaName(super.getFullName().toString() + namePart.toString());
        } else {
            return new VTJavaName(super.getFullName().toString() + '.' + namePart.toString());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTJavaName copy() {
        return new VTJavaName(super.getFullName().toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  o  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final VTJavaName o) {
        return super.getFullName().compareTo(o.getFullName());
    }

}
