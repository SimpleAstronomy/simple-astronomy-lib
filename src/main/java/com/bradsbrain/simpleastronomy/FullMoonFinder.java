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

/**
 * Provides a full moon finding calculation for use in a binary search.
 */
public class FullMoonFinder implements MoonFinder {

	private static final double _360 = 360;
	
	/**
	 * This value is close to 180, but the calculate yield more accurate results
	 * compared to observatories with this value than with a higher value.
	 */
	private static final double ROTATE_ANGLE = 179.95;

	private static final double FULL_MOON_HALF_ANGLE = _360/ 2;
	
	/** {@inheritDoc} */
	public boolean isMoonBefore(double angle, double unused) {
		double usefulAngle = (angle + ROTATE_ANGLE) % _360;		
		return usefulAngle < FULL_MOON_HALF_ANGLE;
	}

}
