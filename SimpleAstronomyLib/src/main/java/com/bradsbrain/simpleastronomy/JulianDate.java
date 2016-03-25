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
import java.time.temporal.ChronoUnit;

public class JulianDate {

    private static final ZonedDateTime DAY_ZERO = ZonedDateTime.of(-4713, 11, 24, 12, 0, 0, 0, ZoneOffset.UTC);

    /**
     * This method might not be accurate for older dates but works fine for nowadays
     */
    public static Double makeJulianDateUsingMyModified(ZonedDateTime cal) {
        return (double) ChronoUnit.SECONDS.between(DAY_ZERO, cal) / 24 / 3600;
    }

}
