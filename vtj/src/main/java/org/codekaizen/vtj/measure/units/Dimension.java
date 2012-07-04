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
package org.codekaizen.vtj.measure.units;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;


/**
 * <p>Represents the dimension of a unit.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class Dimension implements Serializable {

    private static final long serialVersionUID = 5387198473441922661L;

    /** Holds dimensionless. */
    public static final Dimension NONE = new Dimension(' ');

    /** DOCUMENT ME! */
    public static final Dimension LENGTH = new Dimension('L');

    /** DOCUMENT ME! */
    public static final Dimension MASS = new Dimension('M');

    /** DOCUMENT ME! */
    public static final Dimension TIME = new Dimension('T');

    /** DOCUMENT ME! */
    public static final Dimension ELECTRIC_CURRENT = new Dimension('Q');

    /** DOCUMENT ME! */
    public static final Dimension TEMPERATURE = new Dimension('\u03F4');

    /** DOCUMENT ME! */
    public static final Dimension AMOUNT_OF_SUBSTANCE = new Dimension('N');

    /** DOCUMENT ME! */
    public static final Dimension LUMINOUS_INTENSITY = new Dimension('R');

    /** DOCUMENT ME! */
    public static final Dimension MONEY = new Dimension('\u00A4');

    /** DOCUMENT ME! */
    protected static final String DOT_OPERATOR = "\u22C5";

    /** DOCUMENT ME! */
    protected static final String NEG_ONE_POWER = "\u207B\u00B9";

    /** DOCUMENT ME! */
    protected static final String NEG_TWO_POWER = "\u207B\u00B2";

    /** DOCUMENT ME! */
    protected static final String NEG_THREE_POWER = "\u207B\u00B3";

    /** DOCUMENT ME! */
    protected static final String NEG_FOUR_POWER = "\u207B\u2074";
    private static final Set<DefinedDimension> DEFINED_DIMENSIONS = new HashSet<DefinedDimension>();

    /**
     * Enumeration of operators used construct composite dimensions.
     */
    public static enum Operator {

        MULTIPLY('*'), DIV('/'), POW('^');

        private static final long serialVersionUID = 1L;

        private final char symbol;

        private Operator(final char symbol) {
            this.symbol = symbol;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public char symbol() {
            return symbol;
        }
    }

    private final String symbol;
    private final Dimension subDimension1;
    private final Operator operator;
    private final Dimension subDimension2;
    private final int power;

    private Dimension(final char symbol) {
        final Map<String, String> values = DimensionSymbolHolder.INSTANCE;
        this.symbol = Character.toString(symbol);
        this.subDimension1 = null;
        this.operator = null;
        this.subDimension2 = null;
        this.power = 1;
    }

    private Dimension(final Dimension subDimension1, final Operator operator, final Dimension subDimension2) {
        final Map<String, String> values = DimensionSymbolHolder.INSTANCE;
        this.subDimension1 = subDimension1;
        this.operator = operator;
        this.subDimension2 = subDimension2;
        this.power = 1;
        this.symbol = subDimension1.toPartString() + operator.symbol() + subDimension2.toPartString();
    }

    private Dimension(final Dimension subDimension1, final int power) {
        final Map<String, String> values = DimensionSymbolHolder.INSTANCE;
        this.subDimension1 = subDimension1;
        this.operator = null;
        this.subDimension2 = null;
        this.power = power;
        this.symbol = subDimension1.toPartString() + Operator.POW.symbol() + power;
    }

    /**
     * Returns the first sub-dimension of this dimension or <code>null</code> if this is a fundamental dimension.
     *
     * @return  the first sub-dimension
     */
    public final Dimension getSubDimension1() {
        return subDimension1;
    }

    /**
     * Returns the second sub-dimension of this dimension or <code>null</code> if this is a fundamental dimension.
     *
     * @return  the second sub-dimension
     */
    public final Dimension getSubDimension2() {
        return subDimension2;
    }

    /**
     * Returns the operator applied to the two sub-dimensions or <code>null</code> if this is a fundamental dimension.
     *
     * @return  the operator
     */
    public final Operator getOperator() {
        return operator;
    }

    /**
     * Returns the power.
     *
     * @return  the power
     */
    public final int getPower() {
        return power;
    }

    /**
     * Returns the product of this dimension with the one specified.
     *
     * @param  dim  the multiplicand
     *
     * @return  the composite dimension
     */
    public final Dimension multiply(final Dimension dim) {
        return new Dimension(this, Operator.MULTIPLY, dim);
    }

    /**
     * Returns the quotient of this dimension with the one specified.
     *
     * @param  dim  the dimension divisor
     *
     * @return  the composite dimension
     */
    public final Dimension div(final Dimension dim) {
        return new Dimension(this, Operator.DIV, dim);
    }

    /**
     * Returns this dimension rasied to the specified power.
     *
     * @param  pow  the power
     *
     * @return  the composite dimension
     */
    public final Dimension pow(final int pow) {
        return new Dimension(this, pow);
    }

    private String toPartString() {

        if (subDimension1 == null) {
            return symbol;
        } else {
            return "(" + symbol + ")";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String toString() {
        return symbol;
    }

    /**
     * Returns the dimension matching the supplied string.
     *
     * @param  s  the output of <code>toString()</code>
     *
     * @return  the dimension
     */
    public static Dimension valueOf(final CharSequence s) {
        final Dimension d = null;

        return d;
    }

    private static final class DimensionSymbolHolder {
        static final Map<String, String> INSTANCE = new HashMap<String, String>();

        static {
            final Properties props = new Properties();
            final InputStream is = Dimension.class.getResourceAsStream("dimensions.properties");

            try {
                props.load(is);

                for (final Object key : props.keySet()) {
                    INSTANCE.put(key.toString(), props.getProperty(key.toString()));
                }
            } catch (final IOException e) {
                throw new IllegalStateException(e);
            } finally {

                try {
                    is.close();
                } catch (final IOException ignore) {
                    // do nothing
                }
            }
        }
    }

    protected class DefinedDimension implements Serializable {
        private static final long serialVersionUID = 6791946875429122370L;

        private final String symbol;
        private final String name;
        private final String baseUnitSymbol;
        private final String baseUnitName;

        protected DefinedDimension(final String line) {
            final StringTokenizer st = new StringTokenizer(line, "|", false);
            symbol = st.nextToken();
            name = st.nextToken();
            baseUnitSymbol = st.nextToken();
            baseUnitName = st.nextToken();
        }

        public String getSymbol() {
            return symbol;
        }

        public String getName() {
            return name;
        }

        public String getBaseUnitSymbol() {
            return baseUnitSymbol;
        }

        public String getBaseUnitName() {
            return baseUnitName;
        }

        @Override
        public int hashCode() {
            return symbol.hashCode();
        }

        @Override
        public boolean equals(final Object obj) {
            boolean result;

            if (this == obj) {
                result = true;
            } else if (obj == null || getClass() != obj.getClass()) {
                result = false;
            } else {
                final DefinedDimension other = (DefinedDimension) obj;
                result = symbol.equals(other.symbol);
            }

            return result;
        }

        @Override
        public String toString() {
            return symbol + " - " + name;
        }

    }

}
