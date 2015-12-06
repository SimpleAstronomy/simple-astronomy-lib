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

import org.junit.Test;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.bradsbrain.simpleastronomy.BaseUtils.formatDateAsShortDateLocalTime;
import static com.bradsbrain.simpleastronomy.BaseUtils.formatDateForGMT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * This is close to the end result of what we're looking for in our library:
 * Given a date/calendar, find the new moon or full moon following that date.
 * 
 * Expected output values can be found for Chicago tests at
 * <a href="http://eclipse.gsfc.nasa.gov/phase/phase2001gmt.html">NASA.</a>.
 * 
 * Expected output values cab be found for Melbourne tests at the
 * <a href="http://museumvictoria.com.au/planetarium/discoverycentre/moon-phases/moon-phases-2015/">Melbourne Planetarium</a>.
 */
public class MoonPhaseFinderTest {

    private static final ZoneId chicagoTimeZone = ZoneId.of("America/Chicago");

    private static final ZoneId melbourneTimeZone = ZoneId.of("Australia/Melbourne");

    @Test
    public void testFindMoonPhaseAt() {
        ZonedDateTime cal = ZonedDateTime.of(1979, 2, 26, 16, 0, 0, 0, ZoneOffset.UTC);

        double moonVisible = MoonPhaseFinder.getMoonVisiblePercent(cal);
        System.out.println("the moon phase on " + formatDateAsReallyLongString(cal) +
                " is: " + MoonPhaseFinder.findMoonPhaseAt(cal) + ", " + moonVisible);
        assertThat(moonVisible, closeTo(0, 0.001));
    }

    @Test
    public void testFindNewMoonFollowing() {
        ZonedDateTime cal = ZonedDateTime.of(2010, 10, 20, 0, 0, 0, 0, ZoneId.of("America/Chicago"));

        ZonedDateTime newMoonDate = MoonPhaseFinder.findNewMoonFollowing(cal);
        assertThat(newMoonDate, is(not(nullValue())));
        assertThat(formatDateAsShortDateLocalTime(newMoonDate, ZoneId.of("America/Chicago")), equalTo("2010-11-06"));
    }

    @Test
    public void testFindFullMoonFollowing() {
        ZonedDateTime cal = ZonedDateTime.of(2010, 10, 20, 0, 0, 0, 0, ZoneId.of("America/Chicago"));

        ZonedDateTime fullMoonDate = MoonPhaseFinder.findFullMoonFollowing(cal);
        assertThat(fullMoonDate, is(not(nullValue())));
        assertThat(formatDateAsShortDateLocalTime(fullMoonDate, ZoneId.of("America/Chicago")), equalTo("2010-10-22"));

        cal = ZonedDateTime.of(2010, 10, 20, 0, 0, 0, 0, ZoneOffset.UTC);
        System.out.println("Date to seek after: " + formatDateAsReallyLongString(cal));

        fullMoonDate = MoonPhaseFinder.findFullMoonFollowing(cal);
        System.out.println("Date of full moon: " + formatDateAsReallyLongString(fullMoonDate));
        assertThat(fullMoonDate, is(not(nullValue())));
        assertThat(formatDateAsShortDateLocalTime(fullMoonDate, ZoneId.of("America/Chicago")), equalTo("2010-10-22"));

    }

    @Test
    public void findEveryFullMoonFor2011() {
        ZonedDateTime cal = ZonedDateTime.of(2011, 1, 1, 1, 1, 0, 0, ZoneOffset.UTC);

        ZonedDateTime oneYearLater = cal.plusYears(1);

        List<String> fullMoonDates = new ArrayList<String>();
        while ((cal.isBefore(oneYearLater)) && fullMoonDates.size() < 12) {
            ZonedDateTime nextFullMoon = MoonPhaseFinder.findFullMoonFollowing(cal);
            fullMoonDates.add(formatDateForGMT(nextFullMoon));
            cal = cal.plusDays(30);
        }

        String[] actualFullMoons2011 = {
                "2011-01-19", "2011-02-18", "2011-03-19", "2011-04-18",
                "2011-05-17", "2011-06-15", "2011-07-15", "2011-08-13",
                "2011-09-12", "2011-10-12", "2011-11-10", "2011-12-10",};
        for (String fullMoon : actualFullMoons2011) {
            assert (fullMoonDates.contains(fullMoon));
        }
    }

    private static String formatDateAsReallyLongString(ZonedDateTime dt) {
        return dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm z Z"));
    }
	
	@Test
	public void exampleFromDocumentation() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM HH:mm:ss zzz yyyy");
        ZonedDateTime cal = ZonedDateTime.of(2011, 6, 12, 0, 0, 0, 0, chicagoTimeZone);

        final ZonedDateTime fullMoon = MoonPhaseFinder.findFullMoonFollowing(cal);
        assertThat(fullMoon.format(formatter), is("15.06 15:25:00 CDT 2011"));
    }

	@Test
	public void mebourneFullMoonMay2015() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM HH:mm:ss zzz yyyy");

        ZonedDateTime cal = ZonedDateTime.of(2015, 5, 1, 0, 0, 0, 0, melbourneTimeZone);

        final ZonedDateTime fullMoon = MoonPhaseFinder.findFullMoonFollowing(cal);
        // TODO: increase accuracy to May 04 13:42:00
        assertThat(fullMoon.format(formatter), is("04.05 13:49:00 EST 2015"));
    }

	@Test
	public void mebourneFullMoonDec2015() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM HH:mm:ss zzz yyyy");

        ZonedDateTime cal = ZonedDateTime.of(2015, 11, 27, 0, 0, 0, 0, melbourneTimeZone);

        final ZonedDateTime fullMoon = MoonPhaseFinder.findFullMoonFollowing(cal);
        // TODO: increase accuracey to Dec 25 22:11:00, note is daylight savings time
        assertThat(fullMoon.format(formatter), is("25.12 22:16:00 EST 2015"));
    }
	
}
