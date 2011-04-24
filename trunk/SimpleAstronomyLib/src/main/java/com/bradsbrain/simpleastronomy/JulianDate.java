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

import java.util.Calendar;

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
     * @param cal
     * @return
     */
    public static Double makeJulianDateUsingMyModified(Calendar cal) {
        Calendar myCal = BaseUtils.getSafeLocalCopy(cal.getTimeInMillis());
        // step 1
        int year = myCal.get(Calendar.YEAR);
        int month = myCal.get(Calendar.MONTH) + 1;    // fix the January=0
        double day = myCal.get(Calendar.DAY_OF_MONTH);
        double hour = myCal.get(Calendar.HOUR_OF_DAY) / 24.0;
        double minute = myCal.get(Calendar.MINUTE) / 24.0 / 60.0;
        double second = myCal.get(Calendar.SECOND) / 24.0 / 60.0 / 60.0;

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
