package com.bradsbrain.simpleastronomy;

import static com.bradsbrain.simpleastronomy.BaseUtils.formatDateAsShortDateLocalTime;
import static com.bradsbrain.simpleastronomy.BaseUtils.formatDateForGMT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.LongConsumer;
import java.util.stream.LongStream;

import org.junit.Before;
import org.junit.Test;

/**
 * Expected output values can be found for Chicago tests at
 * <a href="http://eclipse.gsfc.nasa.gov/phase/phase2001gmt.html">NASA.</a>.
 * 
 * Expected output values can be found for Melbourne tests at
 * <a href="https://www.timeanddate.com/moon/phases/australia/melbourne?year=2015">timeanddate.com</a>.
 */
public class FullMoonFinderTest {
    
    private static final ZoneId chicagoTimeZone = ZoneId.of("America/Chicago");

    private static final ZoneId melbourneTimeZone = ZoneId.of("Australia/Melbourne");
    
    private static final DateTimeFormatter chicagoFormatter = DateTimeFormatter
            .ofPattern("dd MMM HH:mm:ss Z yyyy")
            .withLocale(Locale.ENGLISH)
            .withZone(chicagoTimeZone);
    
    private static final DateTimeFormatter melbourneFormatter = DateTimeFormatter
            .ofPattern("dd MMM HH:mm:ss Z yyyy")
            .withLocale(Locale.ENGLISH)
            .withZone(melbourneTimeZone);

    @Before
    public void setUp() throws Exception {
//      private FullMoonFinder fullMoonFinder;
//      private MoonPhaseFinder phaseFinder;        
//      fullMoonFinder = new FullMoonFinder();
//      phaseFinder = new MoonPhaseFinder();
    }

    @Test
    public void testFindNewMoonFollowing() {
        ZonedDateTime cal = ZonedDateTime.of(2010, 10, 20, 0, 0, 0, 0, chicagoTimeZone);

        ZonedDateTime newMoonDate = MoonPhaseFinder.findNewMoonFollowing(cal);
        assertThat(newMoonDate, is(not(nullValue())));
        assertThat(formatDateAsShortDateLocalTime(newMoonDate, chicagoTimeZone), equalTo("2010-11-06"));
    }

    @Test
    public void testFindFullMoonFollowing() {
        ZonedDateTime cal = ZonedDateTime.of(2010, 10, 20, 0, 0, 0, 0, chicagoTimeZone);

        ZonedDateTime fullMoonDate = MoonPhaseFinder.findFullMoonFollowing(cal);
        assertThat(fullMoonDate, is(not(nullValue())));
        assertThat(formatDateAsShortDateLocalTime(fullMoonDate, chicagoTimeZone), equalTo("2010-10-22"));

        cal = ZonedDateTime.of(2010, 10, 20, 0, 0, 0, 0, ZoneOffset.UTC);
        System.out.println("Date to seek after: " + formatDateAsReallyLongString(cal));

        fullMoonDate = MoonPhaseFinder.findFullMoonFollowing(cal);
        System.out.println("Date of full moon: " + formatDateAsReallyLongString(fullMoonDate));
        assertThat(fullMoonDate, is(not(nullValue())));
        assertThat(formatDateAsShortDateLocalTime(fullMoonDate, chicagoTimeZone), equalTo("2010-10-22"));
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


    @Test
    public void exampleFromDocumentation() {
        ZonedDateTime cal = ZonedDateTime.of(2011, 6, 12, 0, 0, 0, 0, chicagoTimeZone);

        ZonedDateTime fullMoon = MoonPhaseFinder.findFullMoonFollowing(cal);
        assertThat(fullMoon.format(chicagoFormatter), is("15 Jun 15:19:00 -0500 2011"));
    }

    @Test
    public void mebourneFullMoonMay2015() {
        ZonedDateTime cal = ZonedDateTime.of(2015, 5, 1, 0, 0, 0, 0, melbourneTimeZone);

        ZonedDateTime fullMoon = MoonPhaseFinder.findFullMoonFollowing(cal);
        assertThat(fullMoon.format(melbourneFormatter), is("04 May 13:42:00 +1000 2015"));
    }

    @Test
    public void mebourneFullMoonDec2015() {
        ZonedDateTime cal = ZonedDateTime.of(2015, 11, 27, 0, 0, 0, 0, melbourneTimeZone);

        ZonedDateTime fullMoon = MoonPhaseFinder.findFullMoonFollowing(cal);
        assertThat(fullMoon.format(melbourneFormatter), is("25 Dec 22:11:00 +1100 2015"));
    }

    @Test
    public void troubleDateDec2015_00() {
        givenMelbourneDate("25 Dec 22:09:00 +1100 2015");
        whenFullMoonIsFound();
        then25Dec2015IsTheFullMoon();
    }

    @Test
    public void troubleDateDec2015_0() {
        givenMelbourneDate("25 Dec 22:11:49 +1100 2015");
        whenFullMoonIsFound();
        then24Jan2016IsTheFullMoon();
    }
    
    
    @Test
    public void troubleDateDec2015_1() {
        givenMelbourneDate("25 Dec 22:16:49 +1100 2015");
        whenFullMoonIsFound();
        then24Jan2016IsTheFullMoon();
    }
    
    @Test
    public void troubleDateDec2015_2() {
        givenMelbourneDate("25 Dec 22:20:02 +1100 2015");
        whenFullMoonIsFound();
        then24Jan2016IsTheFullMoon();
    }
    
    @Test
    public void troubleDateDec2015_3() {
        givenMelbourneDate("25 Dec 22:26:48 +1100 2015");
        whenFullMoonIsFound();
        then24Jan2016IsTheFullMoon();
    } 
    
    @Test
    public void troubleDateDec2015_4() {
        givenMelbourneDate("25 Dec 23:29:27 +1100 2015");
        whenFullMoonIsFound();
        then24Jan2016IsTheFullMoon();
    }
     
    @Test
    public void troubleDateDec2015_5() {
        givenMelbourneDate("26 Dec 04:02:14 +1100 2015");
        whenFullMoonIsFound();
        then24Jan2016IsTheFullMoon();
    }

    @Test
    public void troubleDateJan2016_1() {
        givenMelbourneDate("24 Jan 11:00:31 +1100 2016");
        whenFullMoonIsFound();
        then24Jan2016IsTheFullMoon();
    }
    
    @Test
    public void troubleDateJan2016_2() {
        givenMelbourneDate("24 Jan 11:30:31 +1100 2016");
        whenFullMoonIsFound();
        then24Jan2016IsTheFullMoon();
    }
    
    @Test
    public void troubleDateJan2016_3() {
        givenMelbourneDate("24 Jan 12:00:31 +1100 2016");
        whenFullMoonIsFound();
        then24Jan2016IsTheFullMoon();
    }
    
    private String testDateString;
    private ZonedDateTime testDateTime;
    private ZonedDateTime foundFullMoon;
    
    public void givenMelbourneDate(String dateString) {
        testDateString = dateString;
        testDateTime = ZonedDateTime.parse(testDateString, melbourneFormatter);
    }
    
    public void whenFullMoonIsFound() {
        foundFullMoon = MoonPhaseFinder.findFullMoonFollowing(testDateTime);
    }
    
    public void then24Jan2016IsTheFullMoon() {
        String melbourneJan2016FullMoonString = "24 Jan 12:54:00 +1100 2016";
        String fullMoonString = foundFullMoon.format(melbourneFormatter);
        
        double testDateAngle = MoonPhaseFinder.getMoonAngle(testDateTime);
        System.out.println("Input date [" + testDateString + "] has angle [" + testDateAngle + "]");
        
        double fullMoonAngle = MoonPhaseFinder.getMoonAngle(foundFullMoon);
        System.out.println("Full moon  [" + fullMoonString + "] has angle [" + fullMoonAngle + "]");

        ZonedDateTime correct = ZonedDateTime.parse(melbourneJan2016FullMoonString, melbourneFormatter);
        double correctAngle = MoonPhaseFinder.getMoonAngle(correct);
        System.out.println("Correct  [" + melbourneJan2016FullMoonString + "] has angle [" + correctAngle + "]");
        System.out.println();
        
        assertThat(fullMoonString, is(melbourneJan2016FullMoonString));
    }
    
    public void then25Dec2015IsTheFullMoon() {
        String fullMoonString = foundFullMoon.format(melbourneFormatter);
        assertThat(fullMoonString, is("25 Dec 22:11:00 +1100 2015"));
    }
    
    /**
     * Attempt to detect subtle problems with the binary search inside the
     * moon finding algorithm.
     */
    @Test
    public void twentyThousandFinds() {
        final String decFullMoon = "25 Dec 22:11:00 +1100 2015";
        
        final String janFullMoon = "24 Jan 12:54:00 +1100 2016";
        final ZonedDateTime decFullMoonDate = ZonedDateTime.parse(decFullMoon, melbourneFormatter);
        ZonedDateTime janFullMoonDate = ZonedDateTime.parse(janFullMoon, melbourneFormatter);
        long startEpochMillis = decFullMoonDate.minusDays(28).toInstant().toEpochMilli();
        long endEpochMillis = janFullMoonDate.toInstant().toEpochMilli();
        
        LongStream longs = new Random(808l).longs(20000, startEpochMillis, endEpochMillis);
        
        LongConsumer action = new LongConsumer() {  
            public void accept(long value) {                
                ZonedDateTime testTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(value), melbourneTimeZone);
                ZonedDateTime fullMoon = MoonPhaseFinder.findFullMoonFollowing(testTime);
                
                System.out.println(testTime.format(melbourneFormatter) + " -> " + fullMoon.format(melbourneFormatter));
                
                if (testTime.isAfter(decFullMoonDate)) {
                    assertThat(fullMoon.format(melbourneFormatter), is(janFullMoon));
                } else {
                    assertThat(fullMoon.format(melbourneFormatter), is(decFullMoon));
                }
            }
        };
        longs.sequential().forEach(action);
    }
    
    private static String formatDateAsReallyLongString(ZonedDateTime dt) {
        return dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm z Z"));
    }
    
    // TODO: make a custom hamcrest matcher, is within 15 minutes of ("<date>")    
    
//  @Test
//  public void plotAnglesOverMonth() {
//      String melbourneJan2016FullMoonString = "24 Jan 12:54:00 +1100 2016";
//      // TODO: last date has wrong angle, why? Bad full moon date??
//      String melbourneMar2016FullMoonString = "23 Mar 23:00:00 +1100 2016";
//      
//      ZonedDateTime melJan2016FullMoon = ZonedDateTime.parse(melbourneJan2016FullMoonString, melbourneFormatter);
//      ZonedDateTime melMar2016FullMoon = ZonedDateTime.parse(melbourneMar2016FullMoonString, melbourneFormatter);
//      
//      int n = 40;
//      Duration nTh = Duration.between(melJan2016FullMoon, melMar2016FullMoon).dividedBy(n);
//      
//      for (int index = 0 ; index < n + 1 ; index++) {
//          Duration duration = nTh.multipliedBy(index);
//          ZonedDateTime workingDate = melJan2016FullMoon.plus(duration);
//          
//          double testDateAngle = MoonPhaseFinder.getMoonAngle(workingDate);
//          
//          String dateString = workingDate.format(melbourneFormatter);
//          double visible = MoonPhaseFinder.getMoonVisiblePercent(workingDate);
//          String visibleString = Double.toString(visible);
//          
//          ZonedDateTime oneSecondLater = workingDate.plus(Duration.ofHours(1));
//          double secondAngle = MoonPhaseFinder.getMoonAngle(oneSecondLater);
//          double secondVisible = MoonPhaseFinder.getMoonVisiblePercent(oneSecondLater);
//          
//          String angleChangeType = secondAngle - testDateAngle > 0 ? "rising" : "falling";
//          String visibleChangeType = secondVisible - visible > 0 ? "rising" : "falling";
//          
//          // TODO: try visible percent instead, useful rising metric??
//          //System.out.println("Date [" + dateString + "] has angle [" + testDateAngle + "] and change is [" + angleChangeType + "]");
//          System.out.println("Date [" + dateString + "] has visible [" + visibleString + "] and change is [" + visibleChangeType + "]");
//      }
//  }

}

