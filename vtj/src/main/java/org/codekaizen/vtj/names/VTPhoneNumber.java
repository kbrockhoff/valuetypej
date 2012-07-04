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
import org.codekaizen.vtj.text.VTString;


/**
 * <p>Represents a telephone number from any locale.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class VTPhoneNumber extends VTName<VTPhoneNumber> {

    private static final long serialVersionUID = 5507007012755047332L;
    private static final Pattern PATTERN = Pattern.compile(
            "[\\+\\(]?[0-9]+([\\s\\(\\)\\-\\.\\~\\*]+[0-9]+)+(\\s?\\#[0-9]+)?");

    private transient VTString countryCode;
    private transient VTString areaCode;
    private transient VTString localNumber;
    private transient VTString extension;

    /**
     * Constructs a phone number object.
     *
     * @param  fullName  the complete phone number
     */
    public VTPhoneNumber(final String fullName) {
        super(fullName, 57);
    }

    /**
     * Constructs a phone number object.
     *
     * @param  countryCode  the country code
     * @param  areaCode  the area or region code
     * @param  localNumber  the local number
     */
    public VTPhoneNumber(final String countryCode, final String areaCode, final String localNumber) {
        this((countryCode == null ? "+1" : countryCode) + " " + (areaCode == null ? "" : areaCode) + " " +
                (localNumber == null ? "" : localNumber));

        String s = countryCode == null ? "+1" : countryCode;

        if (s.startsWith("1")) {
            s = "+1";
        } else if (!s.startsWith("+")) {
            s = "+" + s;
        }

        this.countryCode = new VTString(s);
        this.areaCode = new VTString(areaCode);

        if (localNumber == null) {
            s = "";
        } else {
            s = localNumber.replace(' ', '-');
        }

        s = s.replace('.', '-');
        this.localNumber = new VTString(s);
        this.extension = null;
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
        final StringTokenizer st = new StringTokenizer(fullName, " -().#", false);

        while (st.hasMoreTokens()) {
            nameParts.add(st.nextToken());
        }

        if (nameParts.size() < 3) {
            throw new IllegalArgumentException("must be at least three parts");
        }

        this.populateNumberParts(nameParts, 0);

        return this.buildFormattedNumber();
    }

    private void populateNumberParts(final List<String> nameParts, final int startPos) {
        final String part1 = nameParts.get(startPos);

        switch (part1.charAt(0)) {
        case '+':

            // country code first
            if (part1.startsWith("+1")) {
                this.handleNorthAmerican(nameParts, startPos + 1);
            } else {
                this.handleOther(nameParts, startPos);
            }

            break;
        case '0':
            // dial out first
            this.populateNumberParts(nameParts, 1);

            break;
        case '1':
            // North American number
            this.handleNorthAmerican(nameParts, startPos + 1);

            break;
        default:

            if (this.validateNorthAmerican(nameParts, startPos) == -1) {
                // North American number
                this.handleNorthAmerican(nameParts, startPos);
            } else {
                this.handleOther(nameParts, startPos);
            }

            break;
        }
    }

    private void handleNorthAmerican(final List<String> nameParts, final int startPos) {
        this.countryCode = new VTString("+1");

        final int invalid = this.validateNorthAmerican(nameParts, startPos);

        if (invalid != -1) {
            throw new IllegalArgumentException("invalid phone number part");
        }

        switch (nameParts.size() - startPos) {
        case 3:
            this.areaCode = new VTString(nameParts.get(startPos));
            this.localNumber = new VTString(nameParts.get(startPos + 1) + "-" + nameParts.get(startPos + 2));

            break;
        case 4:
            this.areaCode = new VTString(nameParts.get(startPos));
            this.localNumber = new VTString(nameParts.get(startPos + 1) + "-" + nameParts.get(startPos + 2));

            if (nameParts.get(startPos + 3).startsWith("#")) {
                this.extension = new VTString(nameParts.get(startPos + 3).substring(1));
            } else {
                this.extension = new VTString(nameParts.get(startPos + 3));
            }

            break;
        default:
            throw new IllegalArgumentException("incorrect number of telephone number parts");
        }
    }

    private int validateNorthAmerican(final List<String> nameParts, final int startPos) {
        int invalidPartNo = -1;

        for (int i = 0; i < 3; i++) {
            final String part = nameParts.get(startPos + i);

            for (int j = 0; j < part.length(); j++) {

                if (!Character.isDigit(part.charAt(j))) {
                    invalidPartNo = startPos + i;
                }
            }

            switch (i) {
            case 0:

                if (part.length() != 3) {
                    invalidPartNo = startPos + i;
                }

                if (part.charAt(0) == '0' || part.charAt(0) == '1') {
                    invalidPartNo = startPos + i;
                }

                break;
            case 1:

                if (part.length() != 3) {
                    invalidPartNo = startPos + i;
                }

                break;
            case 2:

                if (part.length() != 4) {
                    invalidPartNo = startPos + i;
                }

                break;
            }
        }

        return invalidPartNo;
    }

    private void handleOther(final List<String> nameParts, final int startPos) {
        final int invalid = this.validateOther(nameParts, startPos);

        if (invalid != -1) {
            throw new IllegalArgumentException("invalid phone number part");
        }

        final String part = nameParts.get(startPos);

        if (part.startsWith("+")) {
            this.countryCode = new VTString(part);
        } else {
            this.countryCode = new VTString("+" + part);
        }

        int localStart = -1;

        if (nameParts.size() - startPos - 1 < 2) {
            this.areaCode = null;
            localStart = startPos + 1;
        } else {
            this.areaCode = new VTString(nameParts.get(startPos + 1));
            localStart = startPos + 2;
        }

        final StringBuilder sb = new StringBuilder();

        for (int i = localStart; i < nameParts.size(); i++) {

            if (i > localStart) {
                sb.append("-");
            }

            sb.append(nameParts.get(i));
        }

        this.localNumber = new VTString(sb);
    }

    private int validateOther(final List<String> nameParts, final int startPos) {
        int invalidPartNo = -1;

        for (int i = 0; i < nameParts.size() - startPos; i++) {
            final String part = nameParts.get(startPos + i);

            for (int j = 0; j < part.length(); j++) {

                if (!Character.isDigit(part.charAt(j))) {
                    invalidPartNo = startPos + i;
                }
            }
        }

        return invalidPartNo;
    }

    /**
     * Returns the country dialing code.
     *
     * @return  the country code
     */
    public VTString getCountryCode() {
        return this.countryCode;
    }

    /**
     * Returns the area or region code.
     *
     * @return  the area code
     */
    public VTString getAreaCode() {
        return this.areaCode;
    }

    /**
     * Returns the local number.
     *
     * @return  the local number
     */
    public VTString getLocalNumber() {
        return this.localNumber;
    }

    /**
     * Returns the telephone extension.
     *
     * @return  the extension
     */
    public VTString getExtension() {
        return this.extension;
    }

    private String buildFormattedNumber() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getCountryCode());
        sb.append('-').append(this.getAreaCode());
        sb.append('-').append(this.getLocalNumber());

        if (this.getExtension() != null) {
            sb.append("#").append(this.getExtension());
        }

        return sb.toString();
    }

    /**
     * Returns the full telephone number in a standardized format.
     *
     * @return  the number
     */
    public VTString getFormattedNumber() {
        return new VTString(this.buildFormattedNumber());
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
    public VTPhoneNumber plus(final CharSequence namePart) {
        return new VTPhoneNumber(this.toString() + "-" + namePart.toString());
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
        return this.countryCode.toString().equals(namePart.toString());
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
        return this.localNumber.toString().equals(namePart.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public VTPhoneNumber copy() {
        return new VTPhoneNumber(this.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  o  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final VTPhoneNumber o) {
        return this.toString().compareTo(o.toString());
    }

}
