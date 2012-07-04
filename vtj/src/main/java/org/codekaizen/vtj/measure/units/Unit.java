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

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.codekaizen.vtj.measure.converters.UnitConverter;
import org.codekaizen.vtj.measure.quantities.Quantity;


/**
 * <p>Represents a determinate quantity adopted as a standard of measurement.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public abstract class Unit<Q extends Quantity> implements Serializable {

    private static final long serialVersionUID = -8816904048556111449L;

    static final Map<String, Unit<?>> SYMBOL_TO_UNIT = new ConcurrentHashMap<String, Unit<?>>();

    private final String symbol;

    /**
     * Creates a new Unit object.
     *
     * @param  symbol  DOCUMENT ME!
     */
    protected Unit(final String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the {@link BaseUnit base unit}, or product of base units and alternate units this unit is derived from.
     *
     * @return  the system unit this unit is derived from.
     */
    public abstract Unit<? super Q> getStandardUnit();


    /**
     * Returns the converter from this unit to its system unit.
     *
     * @return  the converter
     */
    public abstract UnitConverter toStandardUnit();

    /**
     * Indicates if this unit is a standard unit (base units and alternate units are standard units).
     *
     * @return  standard or not
     */
    public boolean isStandardUnit() {
        return getStandardUnit().equals(this);
    }

    /**
     * Indicates if this unit is compatible with the unit specified.
     *
     * @param  that  the other unit.
     *
     * @return  compatible or not
     */
    public final boolean isCompatible(final Unit<?> that) {
        return (this == that) || this.getStandardUnit().equals(that.getStandardUnit());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int hashCode() {
        return 3 * this.symbol.hashCode();
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

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Unit)) {
            return false;
        }

        final Unit<?> other = (Unit<?>) obj;

        return this.symbol.equals(other.symbol);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String toString() {
        return this.symbol;
    }

    /**
     * Returns the unit represented by the supplied official abbreviation or name.
     *
     * @param  s  the abbreviation
     *
     * @return  the unit
     */
    public static Unit<? extends Quantity> valueOf(final String s) {
        final Unit<? extends Quantity> unit = SYMBOL_TO_UNIT.get(s);

        if (unit == null) {

        }

        return unit;
    }

}
