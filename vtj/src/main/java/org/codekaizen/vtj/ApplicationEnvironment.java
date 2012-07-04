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
package org.codekaizen.vtj;

/**
 * <p>Enumerates the available pre-defined application environment contexts in regards to mathematical calculations
 * along with number and date formatting.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public enum ApplicationEnvironment {

    /**
     * Uses fixed decimal point for monetary calculations and floating point arithmetic for other calculations along
     * with local common formatting.
     */
    LOCAL_CUSTOM,

    /**
     * Uses a fixed decimal point and avoidance of rounding errors when executing mathematical operations along with ISO
     * standards for formatting numeric and date-time values.
     */
    INTERNATIONAL_BUSINESS,

    /**
     * Follows generally accepted scientific rules for numeric precision when executing mathematical operations along
     * with IEEE 754R Decimal64 rules.
     */
    SCIENTIFIC;

}
