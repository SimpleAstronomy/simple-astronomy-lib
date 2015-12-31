/*
 *  Copyright 2011 Brad Parks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bradsbrain.simpleastronomy;

import junit.framework.TestCase;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

// external verification
// http://www.usno.navy.mil/USNO/astronomical-applications/data-services/cal-to-jd-conv/
// book Practical Astronomy with your Calculator by Peter Duffet-Smith

public class JulianDateTest extends TestCase {

    @Test
    public void testSanityMath() {
        int fingers = 5;
        double weight = 190.0;
        assertThat(1 / fingers, is(Integer.class));      //      int/int is an int
        assertThat(weight / fingers, is(Double.class));  //      double/int is a double
        assertThat(fingers / weight, is(Double.class));  //      int/double is a double
    }

    @Test
    public void testMakeJulianDateSampleDateOnWikipedia() {
        Calendar c = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.clear();
        c.set(Calendar.YEAR, 2000);
        c.set(Calendar.MONTH, Calendar.MARCH);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);

        Double julianDate = JulianDate.makeJulianDateUsingMyModified(c);

        assertThat(julianDate, closeTo(2451604.5, 0.5));
    }

    /**
     * Page 7 of the book, says 1985 February 17.25 (6 a.m.) should be 2446113.75
     */
    @Test
    public void testMakeJulianDateSampleDateInBook() {
        Calendar c = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.set(Calendar.YEAR, 1985);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 17);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.SECOND, 0);

        Double julianDate = JulianDate.makeJulianDateUsingMyModified(c);

        assertThat(julianDate, closeTo(2446113.75, 0.05));
    }

    @Test
    public void testMakeJulianDateUsingSection46Epoch() {
        Calendar c = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.clear();
        c.set(Calendar.YEAR, 1990);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DAY_OF_MONTH, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);

        Double julianDate = JulianDate.makeJulianDateUsingMyModified(c);

        assertThat(julianDate, closeTo(2447891.5, 0.05));
    }

}
