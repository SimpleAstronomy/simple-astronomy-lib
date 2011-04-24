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

import java.text.DateFormat;
import static java.text.DateFormat.SHORT;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class BaseUtils {

    public static double exactDaysSince(Calendar myCal, double epoch) {
        return JulianDate.makeJulianDateUsingMyModified(myCal) - epoch;
    }

    public static Calendar getSafeLocalCopy(long millis) {
        // safe local copy
        Calendar myCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        myCal.clear();
        myCal.setTimeInMillis(millis);
        return myCal;
    }

    public static double adjustTo360Range(double valToAdjust) {
        double howMany = Math.floor(valToAdjust / 360);
        double adjustedValue = valToAdjust - (howMany * 360);
        return adjustedValue;
    }

    public static double sinDegrees(double angleInDegrees) {
        return Math.sin(Math.toRadians(angleInDegrees));
    }

    public static double cosDegrees(double angleInDegrees) {
        return Math.cos(Math.toRadians(angleInDegrees));
    }

    public static double useLessPrecision(double d, int precision) {
        double digits = Math.pow(10, precision);
        return Math.round(d * digits) / digits;
    }

    /**
     * Useful date-to-string formatting which I found myself using a lot
     *
     * @param moonDate
     * @return the date in GMT timezone
     */
    public static String formatDateForGMT(Date moonDate) {
        DateFormat sdf = SimpleDateFormat.getDateInstance(SHORT);
        ((SimpleDateFormat) sdf).applyPattern("yyyy-MM-dd");
        ((SimpleDateFormat) sdf).setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(moonDate);
    }

    /**
     * Useful date-to-string formatting which I found myself using a lot
     *
     * @param moonDate
     * @return the date in whatever timezone is default
     */
    public static String formatDateAsShortDateLocalTime(Date moonDate) {
        DateFormat sdf = SimpleDateFormat.getDateInstance(SHORT);
        ((SimpleDateFormat) sdf).applyPattern("yyyy-MM-dd");
        return sdf.format(moonDate);
    }


}
