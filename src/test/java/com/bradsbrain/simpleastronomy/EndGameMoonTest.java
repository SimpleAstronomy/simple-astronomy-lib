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

import static com.bradsbrain.simpleastronomy.BaseUtils.formatDateAsShortDateLocalTime;
import static com.bradsbrain.simpleastronomy.BaseUtils.formatDateForGMT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

import java.text.DateFormat;
import static java.text.DateFormat.SHORT;
import java.text.SimpleDateFormat;
import java.util.*;


public class EndGameMoonTest {

    @Test
    public void testFindMoonPhaseAt() {
        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.YEAR, 1979);
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        cal.set(Calendar.DAY_OF_MONTH, 26);
        cal.set(Calendar.HOUR_OF_DAY, 16);
        cal.set(Calendar.MINUTE, 0);

        double moonVisible = MoonPhaseFinder.getMoonVisiblePercent(cal);
        System.out.println("the moon phase on " + formatCalendarAsReallyLongString(cal) +
                " is: " + MoonPhaseFinder.findMoonPhaseAt(cal) + ", " + moonVisible);
        assertThat(moonVisible, closeTo(0, 0.001));
    }

    @Test
    public void testFindNewMoonFollowing() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        cal.clear();
        cal.set(2010, Calendar.OCTOBER, 20);

        Date newMoonDate = MoonPhaseFinder.findNewMoonFollowing(cal);
        assertThat(newMoonDate, is(not(nullValue())));
        assertThat(formatDateAsShortDateLocalTime(newMoonDate), equalTo("2010-11-06"));
    }

    @Test
    public void testFindFullMoonFollowing() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        cal.clear();
        cal.set(2010, Calendar.OCTOBER, 20);

        Date fullMoonDate = MoonPhaseFinder.findFullMoonFollowing(cal);
        assertThat(fullMoonDate, is(not(nullValue())));
        assertThat(formatDateAsShortDateLocalTime(fullMoonDate), equalTo("2010-10-22"));

        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.clear();
        cal.set(2010, Calendar.OCTOBER, 20, 00, 00);
        System.out.println("Date to seek after: " + formatCalendarAsReallyLongString(cal));

        fullMoonDate = MoonPhaseFinder.findFullMoonFollowing(cal);
        System.out.println("Date of full moon: " + formatDateAsReallyLongString(fullMoonDate));
        assertThat(fullMoonDate, is(not(nullValue())));
        assertThat(formatDateAsShortDateLocalTime(fullMoonDate), equalTo("2010-10-22"));

    }

    @Test
    public void findEveryFullMoonFor2011() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.YEAR, 2011);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 1);

        Calendar oneYearLater = Calendar.getInstance();
        oneYearLater.setTimeInMillis(cal.getTimeInMillis());
        oneYearLater.add(Calendar.YEAR, 1);

        List<String> fullMoonDates = new ArrayList<String>();
        while ((cal.before(oneYearLater)) && fullMoonDates.size() < 12) {
            Date nextFullMoon = MoonPhaseFinder.findFullMoonFollowing(cal);
            fullMoonDates.add(formatDateForGMT(nextFullMoon));
            cal.add(Calendar.DATE, 30);
        }

        String[] actualFullMoons2011 = {
                "2011-01-19", "2011-02-18", "2011-03-19", "2011-04-18",
                "2011-05-17", "2011-06-15", "2011-07-15", "2011-08-13",
                "2011-09-12", "2011-10-12", "2011-11-10", "2011-12-10",};
        for (String fullMoon : actualFullMoons2011) {
            assert (fullMoonDates.contains(fullMoon));
        }
    }

    private static String formatCalendarAsReallyLongString(Calendar cal) {
        return formatDateAsReallyLongString(cal.getTime());
    }

    private static String formatDateAsReallyLongString(Date dt) {
        DateFormat longFormat = DateFormat.getDateTimeInstance(SHORT, SHORT);
        ((SimpleDateFormat) longFormat).applyPattern("yyyy-MM-dd 'at' HH:mm z Z");
        return longFormat.format(dt);
    }


}
