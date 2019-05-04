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

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Heavily based on procedures as documented in the
 * book "Practical Astronomy with Your Calculator", 3rd Edition,
 * by Peter Duffett-Smith, Cambridge University Press, 1988, 1990, 1992
 *
 * @author bradparks
 */
public class JulianDate {

    /**
     * This method is a simplification of formula in Section 3 of PAwyC3.
     * We'll assume we're only talking about Gregorian Calendar dates because
     * really, we don't care a whole lot about past dates.
     * 
     * @param cal the input date
     * @return the julian date
     */
    public static Double makeJulianDateUsingMyModified(ZonedDateTime cal) {
        cal = cal.withZoneSameInstant(ZoneOffset.UTC);
        // step 1
        int year = cal.getYear();
        int month = cal.getMonthValue();
        double day = cal.getDayOfMonth();
        double hour = cal.getHour() / 24.0;
        double minute = cal.getMinute() / 24.0 / 60.0;
        double second = cal.getSecond() / 24.0 / 60.0 / 60.0;

        // step 2
        if (month <= 2) {
            year--;
            month += 12;
        }

        // step 6
        return 1720995.5
                + Math.floor(365.243 * year)
                + Math.floor(30.6 * ++month)
                + day + hour + minute + second;
    }

}
