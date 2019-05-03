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

import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MoonPhaseFinder {
	
    private static final MoonFinder newMoonFinder = new NewMoonFinder();
    
    private static final MoonFinder fullMoonFinder = new FullMoonFinder();

    public enum MoonPhase {
        NEW,
        WAXINGCRESCENT,
        FIRSTQUARTER,
        WAXINGGIBBOUS,
        FULL,
        WANINGGIBBOUS,
        LASTQUARTER,
        WANINGCRESCENT;

        static MoonPhase finder(double percent) {
            return FULL;
        }
    }

    /**
     * Someday this will return a descriptive MoonPhase enum when handed a cal/date
     */
    public static MoonPhase findMoonPhaseAt(ZonedDateTime cal) {
        return null;
    }

    public static ZonedDateTime findFullMoonFollowing(ZonedDateTime cal) {
    	// to account for rounding problems, make several dates near the 
    	
    	ZonedDateTime nearDatePast = cal.minusHours(24);
    	ZonedDateTime nearDateFuture = cal.plusHours(24);
    	
    	ZonedDateTime answer1 = _findRoundedDatePassingBounds(nearDatePast, fullMoonFinder);
    	ZonedDateTime answer2 = _findRoundedDatePassingBounds(cal, fullMoonFinder);
		ZonedDateTime answer3 = _findRoundedDatePassingBounds(nearDateFuture, fullMoonFinder);
    	
    	List<ZonedDateTime> sortedCandidates = Arrays.asList(answer1, answer2, answer3);
    	Collections.sort(sortedCandidates);
    	
    	for (ZonedDateTime dateTime : sortedCandidates) {
    		if (cal.isBefore(dateTime)) {
    			return dateTime;
    		}
    	}
    	
    	throw new IllegalStateException("Full moon unexpectedly not found.");
    }

        

    // FIXME: Damon, fix this method as above
	public static ZonedDateTime findNewMoonFollowing(ZonedDateTime cal) {
        return _findRoundedDatePassingBounds(cal, newMoonFinder);
    }
	
	private static ZonedDateTime _findRoundedDatePassingBounds(ZonedDateTime cal, MoonFinder finder) {
		ZonedDateTime found = findDatePassingBounds(cal, finder);
		
		int seconds = found.getSecond();
		int secondsChange = seconds < 30 ? seconds : -1 * (60 - seconds);
		
		return found.minus(secondsChange, ChronoUnit.SECONDS)
				.minus(found.get(ChronoField.MILLI_OF_SECOND), ChronoUnit.MILLIS);
	}
    
    /**
     * Finds a type of moon after the given calendar.  Uses a recursive binary search.
     *
     * @param cal         the calendar date for which to compute the moon position
     * @param moonFinder  the NewMoon or FullMoon checker
     * @return the forward date which passes the given bounds provided
     */
	// FIXME: Damon, try a rewrite back to using raw types for moving bounds
//    private static ZonedDateTime findDatePassingBounds(ZonedDateTime cal, MoonFinder moonFinder) {
//        ZonedDateTime start = cal, end = cal.plusDays(31);
//        while (start.until(end, ChronoUnit.MILLIS) > 500) {
//        	long millisToMiddle = Math.round(start.until(end, ChronoUnit.MILLIS) / 2d);
//            ZonedDateTime middle = start.plus(millisToMiddle, ChronoUnit.MILLIS);
//
//            if (isMoonBefore(middle, moonFinder)) {
//                end = middle;
//            } else {
//                start = middle;
//            }
//        }
//        return end;
//    }

//	
//	// FIXME: Damon try bi-binary, biggest in each of two halves.
//
//	private static ZonedDateTime biBinarySearch(ZonedDateTime cal, MoonFinder finder) {
//        long start = 0, end = _31_DAYS_AS_MILLIS;
//        long middle = start + ((end - start) / 2l);
//        
//        ZonedDateTime cal1 = binarySearch(cal, start, middle, finder);
//        ZonedDateTime cal2 = binarySearch(cal, middle, end, finder);
//        
//        double angle1 = 
//        double angle2 = 
//        
//	}
//	
//	private static ZonedDateTime binarySearch(ZonedDateTime cal, long start, long end, MoonFinder moonFinder) {
//		
//	}
	
	private static final long _31_DAYS_AS_MILLIS = 31 * 24l * 60l * 60l * 1000l
			+ 5l * 60l * 60l * 1000l
			+ 49l * 60l * 1000l
			;

//	private static final long _31_DAYS_AS_MILLIS = 29 * 24l * 60l * 60l * 1000l
//			+ 12l * 60l * 60l * 1000l
//			;

//	private static final long _31_DAYS_AS_MILLIS = 29 * 24l * 60l * 60l * 1000l
//	+ 20l * 60l * 1000l
//	;
	
	
    private static ZonedDateTime findDatePassingBounds(ZonedDateTime cal, MoonFinder moonFinder) {
    	long start = 0, end = _31_DAYS_AS_MILLIS;
    	if (moonFinder instanceof LookAheadDays) {
    		LookAheadDays lookahead = (LookAheadDays) moonFinder;
    		end = lookahead.daysAheadToLook(cal) * 24l * 60l * 60l * 1000l;
    	}
    	
        ZonedDateTime middleCal = cal;
        while (500 < (end - start)) {
        	long middle = start + ((end - start) / 2l);
        	middleCal = cal.plus(middle, ChronoUnit.MILLIS);

            double percent = 100 * MoonPhaseFinder.getMoonVisiblePercent(middleCal);
            double angle = MoonPhaseFinder.getMoonAngle(middleCal);
            if (moonFinder.isMoonBefore(angle, percent)) {
                end = middle;
            } else {
                start = middle;
            }
        }
        
        return middleCal;
    }
    

    /**
     * Returns a (much-too-)high-precision value for the amount of moon visible.
     * Value will be somewhere in the range 0% to 100%  (i.e. 0.00 to 1.00)
     *
     * @return percent of moon which is visible
     */
    public static double getMoonVisiblePercent(ZonedDateTime cal) {
        double moonAngle = getMoonAngle(cal);
        return BaseUtils.useLessPrecision(0.5 * (1 - BaseUtils.cosDegrees(moonAngle)), 3);
    }

    /**
     * The moon angle.  For that we need the sun's position and the moon's position. </br>
     * The moon angle will be in the range 0 to 360.  <br/>
     * 0 or 360 is NEW, 180 is FULL
     *
     * @return the angle of the moon in relation to the earth
     */
    public static double getMoonAngle(ZonedDateTime cal) {
        SunPosition sunPos = new SunPosition(cal);
        MoonPosition moonPos = new MoonPosition(cal);

        double angleAge = moonPos.getTrueLongitude() - sunPos.getEclipticLongitude();
        if (angleAge < 0) {
            return 360 + angleAge;
        }
        return angleAge;
    }

}
 