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

import org.codekaizen.vtj.measure.converters.UnitConverter;
import org.codekaizen.vtj.measure.quantities.Quantity;


/**
 * <p>Represents base unit defined by Syst�me International d'Unit�s (SI) or some other standards body upon which a
 * system of units is derived.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public class BaseUnit<Q extends Quantity> extends Unit<Q> {

    private static final long serialVersionUID = -1818704348807297552L;

    /**
     * Creates a new BaseUnit object.
     *
     * @param  symbol  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public BaseUnit(final String symbol) {
        super(symbol);

        // checks if the symbol is associated to a different units
        synchronized (Unit.SYMBOL_TO_UNIT) {
            final Unit<?> unit = Unit.SYMBOL_TO_UNIT.get(symbol);

            if (unit == null) {
                Unit.SYMBOL_TO_UNIT.put(symbol, this);
            } else if (!(unit instanceof BaseUnit)) {
                throw new IllegalArgumentException("Symbol " + symbol + " is associated to a different unit");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Unit<? super Q> getStandardUnit() {
        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public UnitConverter toStandardUnit() {
        // TODO Auto-generated method stub
        return null;
    }

}
