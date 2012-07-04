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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.codekaizen.vtj.measure.quantities.AmountOfSubstance;
import org.codekaizen.vtj.measure.quantities.Duration;
import org.codekaizen.vtj.measure.quantities.ElectricCurrent;
import org.codekaizen.vtj.measure.quantities.Length;
import org.codekaizen.vtj.measure.quantities.LuminousIntensity;
import org.codekaizen.vtj.measure.quantities.Mass;
import org.codekaizen.vtj.measure.quantities.Temperature;


/**
 * <p>This class contains SI (SystËme International d'UnitÈs) base units and derived units.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
public final class SI extends SystemOfUnits {

    private static Set<Unit<?>> UNITS = new HashSet<Unit<?>>();
    private static final SI INSTANCE = new SI();

    /** The base unit for electric current quantities (<code>A</code>). */
    public static final BaseUnit<ElectricCurrent> AMPERE = si(new BaseUnit<ElectricCurrent>("A"));

    /** The base unit for luminous intensity quantities (<code>cd</code>). */
    public static final BaseUnit<LuminousIntensity> CANDELA = si(new BaseUnit<LuminousIntensity>("cd"));

    /** The base unit for thermodynamic temperature quantities (<code>K</code>). */
    public static final BaseUnit<Temperature> KELVIN = si(new BaseUnit<Temperature>("K"));

    /** The base unit for mass quantities (<code>kg</code>). */
    public static final BaseUnit<Mass> KILOGRAM = si(new BaseUnit<Mass>("kg"));

    /** The base unit for length quantities (<code>m</code>). */
    public static final BaseUnit<Length> METRE = si(new BaseUnit<Length>("m"));

    /** The base unit for amount of substance quantities (<code>mol</code>). */
    public static final BaseUnit<AmountOfSubstance> MOLE = si(new BaseUnit<AmountOfSubstance>("mol"));

    /** The base unit for duration quantities (<code>s</code>). */
    public static final BaseUnit<Duration> SECOND = si(new BaseUnit<Duration>("s"));

    private SI() {
        // non-instantiable
    }

    /**
     * Returns the unique instance of this class.
     *
     * @return  the SI instance.
     */
    public static SI getInstance() {
        return INSTANCE;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Set<Unit<?>> getUnits() {
        return Collections.unmodifiableSet(UNITS);
    }

    private static <U extends Unit<?>> U si(final U unit) {
        UNITS.add(unit);

        return unit;
    }

}
