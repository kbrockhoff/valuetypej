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
package org.codekaizen.vtj.measure.quantities;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import org.testng.annotations.Test;


/**
 * <p>Units tests to check if all interfaces in the package extend {@link Quantity}.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
@Test
public class QuantitySubclassingTest {

    /**
     * Creates a new QuantitySubclassingTest object.
     */
    public QuantitySubclassingTest() {
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  ClassNotFoundException  DOCUMENT ME!
     */
    public void testSubclassing() throws ClassNotFoundException {
        final Package p = Quantity.class.getPackage();
        final String path = p.getName().replace('.', '/');
        final ClassLoader cl = Quantity.class.getClassLoader();
        final URL resource = cl.getResource(path);
        String dirPath = resource.getFile();

        if (dirPath.indexOf("test-") != -1) {
            dirPath = dirPath.replace("test-", "");
        }

        final File directory = new File(dirPath);
        final String[] files = directory.list();

        for (int i = 0; i < files.length; i++) {

            if (files[i].endsWith(".class") && files[i].indexOf("Test") == -1) {
                final String nme = p.getName() + '.' + files[i].substring(0, files[i].length() - 6);
                final Class<?> sub = Class.forName(nme);
                assertTrue(Quantity.class.isAssignableFrom(sub), sub.getName() + " missing extends Quantity");
            }
        }
    }

}
