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
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class SunPositionTest extends TestCase {

    @Test
    public void testSunPosition() {
        Calendar c = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.clear();
        c.set(Calendar.YEAR, 1980);
        c.set(Calendar.MONTH, Calendar.JULY);
        c.set(Calendar.DAY_OF_MONTH, 27);
        c.set(Calendar.HOUR_OF_DAY, 0);

        SunPosition sunpos = new SunPosition(c);

        assertThat(sunpos.getMeanAnomaly(), closeTo(202.065386, 0.05));

        assertThat(sunpos.getEclipticLongitude(), closeTo(124.114347, 0.05));
    }
}
