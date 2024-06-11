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

        // TODO: consider an implementation, start reading at https://www.quia.com/jg/431146list.html
        // TODO: dateandtime.com has new moon, first moon and third quarter values to make tests with
    }

    /**
     * Someday this will return a descriptive MoonPhase enum when handed a cal/date
     *
     * @param cal the input date
     * @return a MoonPhase
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

    private static final long LONGEST_SYNODIC_MONTH_EVER = (29 * 24 + 20) * 3600000L;

    private static ZonedDateTime findDatePassingBounds(ZonedDateTime cal, MoonFinder moonFinder) {
        long start = 0;
        long end = LONGEST_SYNODIC_MONTH_EVER;

        ZonedDateTime middleCal = cal;
        while (500 < (end - start)) {
            long middle = start + Math.round((end - start) / 2.125); //a bit less than the middle as shorter synodic months can be up to 13.5h shorter than the longest one
            middleCal = cal.plus(middle, ChronoUnit.MILLIS);

            double percent = 100 * MoonPhaseFinder.getMoonVisiblePercent(middleCal);
            double angle = MoonPhaseFinder.getMoonAngle(middleCal);
            if (moonFinder.isMoonBefore(angle, percent)) {
                end = middle;
            } else {
                start = middle;
            }
        }

        ZonedDateTime result = roundToMinutes(middleCal);
        if (start > 0) {
            return result;
        }
        return findDatePassingBounds(cal.plusDays(15), moonFinder);
    }

    private static ZonedDateTime roundToMinutes(ZonedDateTime input) {
        ZonedDateTime result = input.withSecond(0).withNano(0);
        if (input.getSecond() * 1_000_000_000L + input.getNano() >= 30_000_000_000L) {
            result = result.plusMinutes(1);
        }
        return result;
    }

    /**
     * Returns a (much-too-)high-precision value for the amount of moon visible.
     * Value will be somewhere in the range 0% to 100%  (i.e. 0.00 to 1.00)
     *
     * @param cal the input date
     * @return percent of moon which is visible
     */
    public static double getMoonVisiblePercent(ZonedDateTime cal) {
        double moonAngle = getMoonAngle(cal);
        return BaseUtils.useLessPrecision(0.5 * (1 - BaseUtils.cosDegrees(moonAngle)), 3);
    }

    /**
     * The moon angle.  For that we need the sun's position and the moon's position. <br>
     * The moon angle will be in the range 0 to 360.  <br>
     * 0 or 360 is NEW, 180 is FULL
     *
     * @param cal the input date
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
 