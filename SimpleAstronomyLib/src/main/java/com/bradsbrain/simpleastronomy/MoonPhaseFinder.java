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
import java.time.temporal.ChronoUnit;

public class MoonPhaseFinder {

	private static final int _31_DAYS_AS_MINUTES = 31 * 24 * 60;
	
    private static final MoonFinder newMoonFinder = new NewMoonFinder();
    
    private static final MoonFinder fullMoonFinder = new FullMoonFinder();

    private static final MoonFinder firstQuarterFinder = new FirstQuarterFinder();

    private static final MoonFinder lastQuarterFinder = new LastQuarterFinder();

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
        return findDatePassingBounds(cal, fullMoonFinder);
    }

    public static ZonedDateTime findLastQuarterFollowing(ZonedDateTime cal) {
        return findDatePassingBounds(cal, lastQuarterFinder);
    }

    public static ZonedDateTime findNewMoonFollowing(ZonedDateTime cal) {
        return findDatePassingBounds(cal, newMoonFinder);
    }

    public static ZonedDateTime findFirsQuarterFollowing(ZonedDateTime cal) {
        return findDatePassingBounds(cal, firstQuarterFinder);
    }

    /**
     * Finds a type of moon after the given calendar.  Uses a binary search.  Recursive.
     *
     * @param cal         the calendar date for which to compute the moon position
     * @param moonFinder  the NewMoon or FullMoon checker
     * @return the forward date which passes the given bounds provided
     */
    private static ZonedDateTime findDatePassingBounds(ZonedDateTime cal, MoonFinder moonFinder) {
        ZonedDateTime start = cal;
        ZonedDateTime end = cal.plusDays(29).plusHours(20); //plus longest synodic month ever
        while (start.until(end, ChronoUnit.MINUTES) > 1) {
            ZonedDateTime middle = start.plusMinutes(Math.round(start.until(end, ChronoUnit.MINUTES) / 2.125)); //a bit less than the middle as sorter synodic months can be up to 13.5h shorter than the longest one

            if (isMoonBefore(middle, moonFinder)) {
                end = middle;
            } else {
                start = middle;
            }
        }
        start = isMoonBefore(start.plusSeconds(30), moonFinder) ? start : end;
        if (start == cal) {//is the found event actually before the beginning?
            return findDatePassingBounds(cal.plusDays(15), moonFinder);
        }
        return start;
    }

    private static boolean isMoonBefore(ZonedDateTime time, MoonFinder moonFinder) {
        double percent = 100 * MoonPhaseFinder.getMoonVisiblePercent(time);
        double angle = MoonPhaseFinder.getMoonAngle(time);
        return moonFinder.isMoonBefore(angle, percent);
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
