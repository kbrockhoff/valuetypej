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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import org.codekaizen.vtj.text.VTString;


/**
 * <p>Represents a name in an LDAP repository.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTLdapName extends VTName<VTLdapName> {

    private static final long serialVersionUID = -2865133984400571083L;
    private static final Pattern PATTERN = Pattern.compile("[A-Za-z][0-9_\\-A-Za-z]*\\=[A-Za-z][0-9_\\-A-Za-z\\s]*" +
                "(\\,[A-Za-z][0-9_\\-A-Za-z]*\\=[A-Za-z][0-9_\\-A-Za-z\\s]*)*");

    /**
     * Constructs a Java name object.
     *
     * @param  fullName  the name
     */
    public VTLdapName(final String fullName) {
        super(fullName, 79);
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
        final StringBuilder sb = new StringBuilder();
        final StringTokenizer st = new StringTokenizer(fullName, ",", false);

        while (st.hasMoreTokens()) {
            final String part = st.nextToken().trim();

            if (part.indexOf('=') == -1) {
                throw new IllegalArgumentException("not a key-value pair");
            }

            nameParts.add(part);

            if (sb.length() > 0) {
                sb.append(',');
            }

            sb.append(part);
        }
        // reverse to hierarchical order
        Collections.reverse(nameParts);

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
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    @Override
    public VTLdapName plus(final CharSequence namePart) {

        if (namePart.toString().indexOf('=') == -1) {
            throw new IllegalArgumentException("not a key-value pair");
        }

        return new VTLdapName(namePart + "," + this.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTLdapName copy() {
        return new VTLdapName(this.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  o  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final VTLdapName o) {
        int result = this.size() - o.size();

        if (result == 0) {
            final Iterator<VTString> otherIt = o.iterator();

            for (final VTString vtString : this) {
                final int r = vtString.compareTo(otherIt.next());

                if (r != 0) {
                    result = r;

                    break;
                }
            }
        }

        return result;
    }

}
