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
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import org.codekaizen.vtj.AssertPrecondition;
import org.codekaizen.vtj.text.VTString;


/**
 * <p>Represents the name of a person parsed into its separate parts.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTPersonName extends VTName<VTPersonName> {

    private static final long serialVersionUID = 2654879493115289623L;
    private static final String[] PREFIXES = {
            "Dr.", "Miss", "Mr.", "Mrs.", "Ms.", "Prof.", "Rev.", "DR", "MISS", "MR", "MRS", "MS", "PROF", "REV",
        };
    private static final String[] SUFFIXES = { "I", "II", "III", "Jr.", "Sr.", "JR", "SR", };
    private static final Pattern PATTERN = Pattern.compile("[A-Z][a-zA-Z]*\\.?(,?\\s+[A-Z][a-zA-Z]*\\.?)*");

    private final Locale locale;
    private VTString prefix;
    private VTString given;
    private VTString middle;
    private VTString family;
    private VTString suffix;

    /**
     * Constructs a person's name.
     *
     * @param  fullName  the complete name
     */
    public VTPersonName(final String fullName) {
        this(fullName, Locale.getDefault());
    }

    /**
     * Constructs a person's name.
     *
     * @param  fullName  the complete name
     * @param  locale  the nationality or culture the name follows the pattern of
     */
    public VTPersonName(final String fullName, final Locale locale) {
        super(fullName, 29);

        if (locale == null) {
            this.locale = Locale.getDefault();
        } else {
            this.locale = locale;
        }

        this.parseFullName(fullName);
    }

    /**
     * Constructs a person's name.
     *
     * @param  title  the abbreviated honorific or prefix
     * @param  given  the first name
     * @param  middle  the middle name
     * @param  family  the last name
     * @param  suffix  the suffix
     */
    public VTPersonName(final String title, final String given, final String middle, final String family,
            final String suffix) {
        this((title == null ? "" : title + " ") + (given == null ? "" : given) + (middle == null ? "" : " " + middle) +
                (family == null ? "" : " " + family) + (suffix == null ? "" : ", " + suffix), Locale.getDefault());

        if (title == null) {
            this.prefix = null;
        } else {
            this.prefix = new VTString(title);
        }

        AssertPrecondition.notBlank("given", given);
        this.given = new VTString(given);

        if (middle == null) {
            this.middle = null;
        } else {
            this.middle = new VTString(middle);
        }

        AssertPrecondition.notBlank("family", family);
        this.family = new VTString(family);

        if (suffix == null) {
            this.suffix = null;
        } else {
            this.suffix = new VTString(suffix);
        }
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
        final StringTokenizer st = new StringTokenizer(fullName, " ,", false);

        while (st.hasMoreTokens()) {
            final String part = st.nextToken().trim();
            nameParts.add(part);

            if (sb.length() > 0) {
                sb.append(' ');
            }

            sb.append(part);
        }

        return sb.toString();
    }

    private void parseFullName(final String fullName) {
        final StringTokenizer st = new StringTokenizer(fullName, ", ", false);

        if (this.isLastNameFirst(fullName)) {
            this.parseLastNameFirst(st);
        } else {
            this.parseFirstNameFirst(st);
        }
    }

    private boolean isLastNameFirst(final String fullName) {
        final int pos1 = fullName.indexOf(',');
        final int pos2 = fullName.indexOf(' ');

        return Locale.CHINA.equals(this.locale) || (pos1 != -1 && (pos1 < pos2 || pos2 == -1));
    }

    private void parseLastNameFirst(final StringTokenizer st) {
        String token = st.nextToken();
        this.family = new VTString(token);
        token = st.nextToken();

        if (this.isSuffix(token) && st.hasMoreTokens()) {
            this.suffix = new VTString(token);
            token = null;
        }

        if (token == null) {
            token = st.nextToken();
        }

        if (this.isPrefix(token) && st.hasMoreTokens()) {
            this.prefix = new VTString(token);
            token = null;
        }

        if (token == null) {
            token = st.nextToken();
        }

        this.given = new VTString(token);

        if (st.hasMoreTokens()) {
            this.middle = new VTString(st.nextToken());
        }
    }

    private void parseFirstNameFirst(final StringTokenizer st) {
        String token = st.nextToken();

        if (this.isPrefix(token) && st.countTokens() > 1) {
            this.prefix = new VTString(token);
            token = null;
        }

        if (token == null) {
            token = st.nextToken();
        }

        this.given = new VTString(token);

        if (st.hasMoreTokens()) {
            token = st.nextToken();
        }

        if (st.hasMoreTokens()) {
            this.middle = new VTString(token);
            token = st.nextToken();
        }

        this.family = new VTString(token);

        if (st.hasMoreTokens()) {
            token = st.nextToken();

            if (this.isSuffix(token)) {
                this.suffix = new VTString(token);
            }
        }
    }

    private boolean isPrefix(final String s) {
        boolean found = false;

        for (final String prefix : PREFIXES) {

            if (prefix.equalsIgnoreCase(s)) {
                found = true;

                break;
            }
        }

        return found;
    }

    private boolean isSuffix(final String s) {
        boolean found = false;

        for (final String suffix : SUFFIXES) {

            if (suffix.equalsIgnoreCase(s)) {
                found = true;

                break;
            }
        }

        return found;
    }

    /**
     * Returns the person's honorific.
     *
     * @return  the abbreviated title
     */
    public VTString getTitle() {
        return this.prefix;
    }

    /**
     * Returns the person's first name.
     *
     * @return  the name
     */
    public VTString getGivenName() {
        return this.given;
    }

    /**
     * Returns the person's middle name or initial.
     *
     * @return  the name
     */
    public VTString getMiddleName() {
        return this.middle;
    }

    /**
     * Returns the person's surname.
     *
     * @return  the name
     */
    public VTString getFamilyName() {
        return this.family;
    }

    /**
     * Returns the person's suffix if any.
     *
     * @return  the abbreviated suffix
     */
    public VTString getSuffix() {
        return this.suffix;
    }

    /**
     * Returns the person's name with the family name first so the string can be sorted.
     *
     * @return  the sortable string
     */
    public VTString getSortFormattedName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.family).append(", ");
        sb.append(this.given);

        if (this.middle != null) {
            sb.append(" ").append(this.middle);
        }

        return new VTString(sb);
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
    public VTPersonName plus(final CharSequence namePart) {
        return new VTPersonName(super.toString() + " " + namePart.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  namePart  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean startsWith(final CharSequence namePart) {
        return this.getGivenName().toString().equals(namePart.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  namePart  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean endsWith(final CharSequence namePart) {
        return this.getFamilyName().toString().equals(namePart.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTPersonName copy() {
        return new VTPersonName(super.getFullName().toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  o  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final VTPersonName o) {
        return this.getSortFormattedName().compareTo(o.getSortFormattedName());
    }

}
