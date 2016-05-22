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

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Provides a full moon finding calculation for use in a binary search.
 */
public class FullMoonFinder implements MoonFinder, LookAheadDays {

	private static final double _360 = 360;
	
	// FIXME: Damon, revise use of 179.5, does that give a better accuracy overall?
	private static final double ROTATE_ANGLE = 179.95;

	private static final double FULL_MOON_HALF_ANGLE = _360/ 2;
	
	// FIXME: Damon unit tes
	/** {@inheritDoc} */
	public int daysAheadToLook(ZonedDateTime startDate) {
		ZonedDateTime oneHourLater = startDate.plus(Duration.ofHours(2));
		double visible = MoonPhaseFinder.getMoonVisiblePercent(startDate);
		double secondVisible = MoonPhaseFinder.getMoonVisiblePercent(oneHourLater);
		
		boolean rising = secondVisible - visible > 0;
		// FIXME: try either is close to full moon?
		boolean eitherPrettyFull = visible > 0.99 || secondVisible > 0.99;
		
		// FIXME: Damon, try with angles, do we get a better result, are we getting rounding probs
		// with the visible calc?
		
		if (rising) { // || eitherPrettyFull) {
			return 16; // rising. Good number?
		} else {
			// FIXME: need to switch to weird magic number?
			// falling
			return 32; // falling. Good number?
		}
	}
	
	/** {@inheritDoc} */
	public boolean isMoonBefore(double angle, double unused) {
		double usefulAngle = (angle + ROTATE_ANGLE) % _360;
		
		return usefulAngle < FULL_MOON_HALF_ANGLE;
	}
	
	// high and rising || low and falling = short lookahead else long 31 days lookahead
	
	
	// FIXME: need unit tests with
//	Input date [25 Dec 22:16:49 +1100 2015] has angle [180.10286220597558]
//	Full moon  [25 Dec 22:17:00 +1100 2015] has angle [180.10441683893725]
//	Correct  [24 Jan 13:01:00 +1100 2016] has angle [180.1028703997246]
//
//	Input date [25 Dec 22:20:02 +1100 2015] has angle [180.13013823408903]
//	Full moon  [25 Dec 22:20:00 +1100 2015] has angle [180.12985558968967]
//	Correct  [24 Jan 13:01:00 +1100 2016] has angle [180.1028703997246]
//
//	Input date [25 Dec 22:26:48 +1100 2015] has angle [180.1875122349249]
//	Full moon  [25 Dec 22:27:00 +1100 2015] has angle [180.1892079260113]
//	Correct  [24 Jan 13:01:00 +1100 2016] has angle [180.1028703997246]
//
//	Input date [25 Dec 23:29:27 +1100 2015] has angle [180.71842217798624]
//	Full moon  [25 Dec 23:29:00 +1100 2015] has angle [180.71461066509028]
//	Correct  [24 Jan 13:01:00 +1100 2016] has angle [180.1028703997246]
//
//	Input date [26 Dec 04:02:14 +1100 2015] has angle [183.02394062040955]
//	Full moon  [24 Jan 13:01:00 +1100 2016] has angle [180.1028703997246]
//	Correct  [24 Jan 13:01:00 +1100 2016] has angle [180.1028703997246]

}
