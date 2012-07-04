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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>Retrieves the MAC addresses for all of the NICS on the host computer.</p>
 *
 * @author  <a href="mailto:kbrockhoff@codekaizen.org">Kevin Brockhoff</a>
 */
class MACAddressRetriever {

    private static final Pattern COLON_PATTERN = Pattern.compile("([^0-9A-Fa-f\\:][0-9A-Fa-f]{1,2}\\:[0-9A-Fa-f]{1,2}" +
                "\\:[0-9A-Fa-f]{1,2}\\:[0-9A-Fa-f]{1,2}" + "\\:[0-9A-Fa-f]{1,2}\\:[0-9A-Fa-f]{1,2}[^0-9A-Fa-f\\:])" +
                "|([^0-9A-Fa-f\\:][0-9A-Fa-f]{1,2}\\:[0-9A-Fa-f]{1,2}" + "\\:[0-9A-Fa-f]{1,2}\\:[0-9A-Fa-f]{1,2}" +
                "\\:[0-9A-Fa-f]{1,2}\\:[0-9A-Fa-f]{1,2})$");
    private static final Pattern DASH_PATTERN = Pattern.compile("([^0-9A-Fa-f\\-][0-9A-Fa-f]{1,2}\\-[0-9A-Fa-f]{1,2}" +
                "\\-[0-9A-Fa-f]{1,2}\\-[0-9A-Fa-f]{1,2}" + "\\-[0-9A-Fa-f]{1,2}\\-[0-9A-Fa-f]{1,2}[^0-9A-Fa-f\\-])" +
                "|([^0-9A-Fa-f\\-][0-9A-Fa-f]{1,2}\\-[0-9A-Fa-f]{1,2}" + "\\-[0-9A-Fa-f]{1,2}\\-[0-9A-Fa-f]{1,2}" +
                "\\-[0-9A-Fa-f]{1,2}\\-[0-9A-Fa-f]{1,2})$");

    MACAddressRetriever() {
    }

    Set<String> retrieveAddresses() {
        final Set<String> set = new HashSet<String>();
        // use OS to determine which command prints addresses
        String[] cmdLine = null;
        final String osname = System.getProperty("os.name", "");

        if (osname.startsWith("Windows")) {
            cmdLine = new String[2];
            cmdLine[0] = "ipconfig";
            cmdLine[1] = "/all";
        } else if (osname.startsWith("Solaris") || osname.startsWith("SunOS")) {

            if (new File("/usr/sbin/ifconfig").exists()) {
                cmdLine = new String[2];
                cmdLine[0] = "/usr/sbin/ifconfig";
                cmdLine[1] = "-a";
            }
        } else if (new File("/sbin/ifconfig").exists()) {
            cmdLine = new String[2];
            cmdLine[0] = "/sbin/ifconfig";
            cmdLine[1] = "-a";
        }

        if (cmdLine != null) {

            // run command and parse output
            try {
                final Process p = Runtime.getRuntime().exec(cmdLine);
                final BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()), 128);
                this.parseOutput(set, r);
            } catch (Exception e) {
                // do nothing
            }
        }

        return set;
    }

    void parseOutput(final Set<String> set, final BufferedReader r) throws IOException {
        String line = r.readLine();

        while (line != null) {
            final String addr = this.parseLine(line);

            if (addr != null) {
                set.add(addr);
            }

            line = r.readLine();
        }
    }

    String parseLine(final String line) {

        if (line == null) {
            return null;
        }

        int start = -1;
        int end = -1;
        Matcher matcher = COLON_PATTERN.matcher(line);

        if (matcher.find()) {
            start = matcher.start();
            end = matcher.end();
        } else {
            matcher = DASH_PATTERN.matcher(line);

            if (matcher.find()) {
                start = matcher.start();
                end = matcher.end();
            }
        }

        if (start == -1) {
            return null;
        } else {

            if (end - start > 18) {
                end--;
            }

            return line.substring(start + 1, end);
        }
    }

}
