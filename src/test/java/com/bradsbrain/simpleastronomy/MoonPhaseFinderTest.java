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

	private static final TimeZone chicagoTimeZone = TimeZone.getTimeZone("America/Chicago");
	
	private static final TimeZone melbourneTimeZone = TimeZone.getTimeZone("Australia/Melbourne");
	
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
        assertThat(formatDateAsShortDateLocalTime(newMoonDate, TimeZone.getTimeZone("America/Chicago")), equalTo("2010-11-06"));
    }

    @Test
    public void testFindFullMoonFollowing() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        cal.clear();
        cal.set(2010, Calendar.OCTOBER, 20);

        Date fullMoonDate = MoonPhaseFinder.findFullMoonFollowing(cal);
        assertThat(fullMoonDate, is(not(nullValue())));
        assertThat(formatDateAsShortDateLocalTime(fullMoonDate, TimeZone.getTimeZone("America/Chicago")), equalTo("2010-10-22"));

        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.clear();
        cal.set(2010, Calendar.OCTOBER, 20, 00, 00);
        System.out.println("Date to seek after: " + formatCalendarAsReallyLongString(cal));

        fullMoonDate = MoonPhaseFinder.findFullMoonFollowing(cal);
        System.out.println("Date of full moon: " + formatDateAsReallyLongString(fullMoonDate));
        assertThat(fullMoonDate, is(not(nullValue())));
        assertThat(formatDateAsShortDateLocalTime(fullMoonDate, TimeZone.getTimeZone("America/Chicago")), equalTo("2010-10-22"));

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
	
	@Test
	public void exampleFromDocumentation() {
		DateFormat formatter = new SimpleDateFormat("MMM dd HH:mm:ss zzz yyyy");
		formatter.setTimeZone(chicagoTimeZone);
		
		Calendar cal = Calendar.getInstance(chicagoTimeZone);
		cal.clear();
		cal.set(2011, Calendar.JUNE, 12);
		
		final Date fullMoon = MoonPhaseFinder.findFullMoonFollowing(cal);
		assertThat(formatter.format(fullMoon), is("Jun 15 15:25:00 CDT 2011"));
	}
	
	@Test
	public void mebourneFullMoonMay2015() {
		DateFormat formatter = new SimpleDateFormat("MMM dd HH:mm:ss zzz yyyy");
		formatter.setTimeZone(melbourneTimeZone);
		
		Calendar cal = Calendar.getInstance(melbourneTimeZone);
		cal.clear();
		cal.set(2015, Calendar.MAY, 1);
		
		final Date fullMoon = MoonPhaseFinder.findFullMoonFollowing(cal);
		// TODO: increase accuracy to May 04 13:42:00
		assertThat(formatter.format(fullMoon), is("May 04 13:49:00 AEST 2015"));
	}

	@Test
	public void mebourneFullMoonDec2015() {
		DateFormat formatter = new SimpleDateFormat("MMM dd HH:mm:ss zzz yyyy");
		formatter.setTimeZone(melbourneTimeZone);
		
		Calendar cal = Calendar.getInstance(melbourneTimeZone);
		cal.clear();
		cal.set(2015, Calendar.NOVEMBER, 27);
		
		final Date fullMoon = MoonPhaseFinder.findFullMoonFollowing(cal);
		// TODO: increase accuracy to Dec 25 22:11:00, note is daylight savings time
		assertThat(formatter.format(fullMoon), is("Dec 25 22:17:00 AEDT 2015"));
	}
	
}
