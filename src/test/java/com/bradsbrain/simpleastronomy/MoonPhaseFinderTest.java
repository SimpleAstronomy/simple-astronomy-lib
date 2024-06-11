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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.Test;

public class MoonPhaseFinderTest {

    @Test
    public void testFindMoonPhaseAt() {
        ZonedDateTime cal = ZonedDateTime.of(1979, 2, 26, 16, 0, 0, 0, ZoneOffset.UTC);

        double moonVisible = MoonPhaseFinder.getMoonVisiblePercent(cal);
        assertThat(moonVisible, closeTo(0, 0.001));
    }

    @Test
    public void testFindFirstQuarterMoonFollowing() {
        ZonedDateTime cal = ZonedDateTime.of(2015, 12, 1, 0, 0, 0, 0, ZoneOffset.UTC);

        ZonedDateTime moonEventDate = MoonPhaseFinder.findFirsQuarterFollowing(cal);

        assertThat(formatDateAsShortDateLocalTime(moonEventDate, ZoneOffset.UTC), equalTo("2015-12-18"));
    }

    @Test
    public void testFindLastQuarterMoonFollowing() {
        ZonedDateTime cal = ZonedDateTime.of(2015, 12, 1, 0, 0, 0, 0, ZoneOffset.UTC);

        ZonedDateTime moonEventDate = MoonPhaseFinder.findLastQuarterFollowing(cal);

        assertThat(formatDateAsShortDateLocalTime(moonEventDate, ZoneOffset.UTC), equalTo("2015-12-03"));
    }

    @Test
    public void testFindFirstQuarterMoonFollowingJustBeforeItShouldStart() {
        ZonedDateTime cal = ZonedDateTime.of(2015, 11, 19, 6, 20, 0, 0, ZoneOffset.UTC);

        ZonedDateTime moonEventDate = MoonPhaseFinder.findFirsQuarterFollowing(cal);

        assertThat(formatDateAsShortDateLocalTime(moonEventDate, ZoneOffset.UTC), equalTo("2015-11-19"));
    }

    @Test
    public void testFindFirstQuarterMoonFollowingJustAfterItStarted() {
        ZonedDateTime cal = ZonedDateTime.of(2015, 11, 19, 6, 40, 0, 0, ZoneOffset.UTC);

        ZonedDateTime moonEventDate = MoonPhaseFinder.findFirsQuarterFollowing(cal);

        assertThat(formatDateAsShortDateLocalTime(moonEventDate, ZoneOffset.UTC), equalTo("2015-12-18"));
    }
}
