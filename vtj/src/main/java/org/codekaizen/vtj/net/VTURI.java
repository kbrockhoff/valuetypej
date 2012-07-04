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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Pattern;
import org.codekaizen.vtj.AssertPrecondition;
import org.codekaizen.vtj.names.VTName;
import org.codekaizen.vtj.text.VTString;
import org.codekaizen.vtj.util.StringUtils;


/**
 * <p>Represents a Uniform Resource Identifier.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTURI extends VTName<VTURI> {

    private static final long serialVersionUID = -5910211791394572741L;
    private static final Pattern PATTERN = Pattern.compile("([A-Za-z][0-9\\-\\+\\.A-Za-z]+\\:)?([\\p{ASCII}^\\s]+)" +
                "([\\:/\\?\\#][\\p{ASCII}^\\s]+)*");

    private transient URI uri;

    /**
     * Constructs a URI object.
     *
     * @param  fullName  the full text
     */
    public VTURI(final String fullName) {
        super(fullName, 7);
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

        try {
            this.uri = new URI(fullName).normalize();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        if (StringUtils.isNotBlank(this.uri.getScheme())) {
            nameParts.add(this.uri.getScheme());
        }

        if (StringUtils.isNotBlank(this.uri.getRawUserInfo())) {
            nameParts.add(this.uri.getRawUserInfo());
        }

        if (StringUtils.isNotBlank(this.uri.getHost())) {
            nameParts.add(this.uri.getHost());
        }

        if (this.uri.getPort() != -1) {
            nameParts.add(Integer.toString(this.uri.getPort()));
        }

        if (nameParts.size() < 2 && StringUtils.isNotBlank(this.uri.getRawAuthority())) {
            nameParts.add(this.uri.getRawAuthority());
        }

        if (StringUtils.isNotBlank(this.uri.getRawPath())) {
            nameParts.add(this.uri.getRawPath());
        }

        if (StringUtils.isNotBlank(this.uri.getRawQuery())) {
            nameParts.add(this.uri.getRawQuery());
        }

        if (nameParts.size() < 2 && StringUtils.isNotBlank(this.uri.getRawSchemeSpecificPart())) {
            nameParts.add(this.uri.getRawSchemeSpecificPart());
        }

        if (StringUtils.isNotBlank(this.uri.getRawFragment())) {
            nameParts.add(this.uri.getRawFragment());
        }

        return this.uri.toASCIIString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public VTString getScheme() {

        if (StringUtils.isNotBlank(this.uri.getScheme())) {
            return new VTString(this.uri.getScheme());
        } else {
            return new VTString("");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public VTString getSchemeSpecificPart() {

        if (StringUtils.isNotBlank(this.uri.getRawSchemeSpecificPart())) {
            return new VTString(this.uri.getRawSchemeSpecificPart());
        } else {
            return new VTString("");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public VTString getAuthority() {

        if (StringUtils.isNotBlank(this.uri.getRawAuthority())) {
            return new VTString(this.uri.getRawAuthority());
        } else {
            return new VTString("");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public VTString getUserInfo() {

        if (StringUtils.isNotBlank(this.uri.getRawUserInfo())) {
            return new VTString(this.uri.getRawUserInfo());
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public VTString getHost() {

        if (StringUtils.isNotBlank(this.uri.getHost())) {
            return new VTString(this.uri.getHost());
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public VTString getPort() {

        if (this.uri.getPort() != -1) {
            return new VTString(Integer.toString(this.uri.getPort()));
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public VTString getPath() {

        if (StringUtils.isNotBlank(this.uri.getRawPath())) {
            return new VTString(this.uri.getRawPath());
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public VTString getQuery() {

        if (StringUtils.isNotBlank(this.uri.getRawQuery())) {
            return new VTString(this.uri.getRawQuery());
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public VTString getFragment() {

        if (StringUtils.isNotBlank(this.uri.getRawFragment())) {
            return new VTString(this.uri.getRawFragment());
        } else {
            return null;
        }
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
    public VTURI plus(final CharSequence namePart) {
        AssertPrecondition.notNull("namePart", namePart);

        URI uri = null;

        if (namePart instanceof VTURI) {
            uri = ((VTURI) namePart).uri;
        } else {

            try {
                uri = new URI(namePart.toString());
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        uri = this.uri.resolve(uri);

        return new VTURI(uri.toASCIIString());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTURI copy() {
        return new VTURI(super.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  o  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final VTURI o) {
        return this.uri.compareTo(o.uri);
    }

}
