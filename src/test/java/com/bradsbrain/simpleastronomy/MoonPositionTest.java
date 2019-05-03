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

import org.junit.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;


public class MoonPositionTest {

    @Test
    public void testGetTrueLongitude() {
        ZonedDateTime c = ZonedDateTime.of(1979, 2, 26, 16, 0, 50, 0, ZoneOffset.UTC);

        MoonPosition moonPosition = new MoonPosition(c);

        assertThat(moonPosition.getTrueLongitude(), closeTo(336.967472, 0.025)); // is this close enough?
    }

}
