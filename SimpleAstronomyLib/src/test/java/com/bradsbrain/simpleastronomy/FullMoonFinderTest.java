package com.bradsbrain.simpleastronomy;

import static org.junit.Assert.*;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class FullMoonFinderTest {

	private FullMoonFinder fullMoonFinder;
	private MoonPhaseFinder phaseFinder; 
	
    private static final ZoneId melbourneTimeZone = ZoneId.of("Australia/Melbourne");
    
	@Before
	public void setUp() throws Exception {
		fullMoonFinder = new FullMoonFinder();
		phaseFinder = new MoonPhaseFinder();
	}
	
    private static final DateTimeFormatter melbourneFormatter = DateTimeFormatter
    		.ofPattern("dd MMM HH:mm:ss Z yyyy")
    		.withLocale(Locale.ENGLISH)
    		.withZone(melbourneTimeZone);

	@Test
	public void plotAnglesOverMonth() {
		String melbourneJan2016FullMoonString = "24 Jan 12:54:00 +1100 2016";
		// FIXME: last date has wrong angle, why? Bad full moon date??
		String melbourneMar2016FullMoonString = "23 Mar 22:01:00 +1100 2016";
		
		ZonedDateTime melJan2016FullMoon = ZonedDateTime.parse(melbourneJan2016FullMoonString, melbourneFormatter);
		ZonedDateTime melMar2016FullMoon = ZonedDateTime.parse(melbourneMar2016FullMoonString, melbourneFormatter);
		
		int n = 40;
		Duration nTh = Duration.between(melJan2016FullMoon, melMar2016FullMoon).dividedBy(n);
		
		for (int index = 0 ; index < n + 1 ; index++) {
			Duration duration = nTh.multipliedBy(index);
			ZonedDateTime workingDate = melJan2016FullMoon.plus(duration);
			
			double testDateAngle = MoonPhaseFinder.getMoonAngle(workingDate);
			
			String dateString = workingDate.format(melbourneFormatter);
			double visible = MoonPhaseFinder.getMoonVisiblePercent(workingDate);
			String visibleString = Double.toString(visible);
			
			ZonedDateTime oneSecondLater = workingDate.plus(Duration.ofHours(1));
			double secondAngle = MoonPhaseFinder.getMoonAngle(oneSecondLater);
			double secondVisible = MoonPhaseFinder.getMoonVisiblePercent(oneSecondLater);
			
			String angleChangeType = secondAngle - testDateAngle > 0 ? "rising" : "falling";
			String visibleChangeType = secondVisible - visible > 0 ? "rising" : "falling";
			
			// FIXME: try visible percent instead, useful rising metric??
			//System.out.println("Date [" + dateString + "] has angle [" + testDateAngle + "] and change is [" + angleChangeType + "]");
			System.out.println("Date [" + dateString + "] has visible [" + visibleString + "] and change is [" + visibleChangeType + "]");
		}
	}

}

