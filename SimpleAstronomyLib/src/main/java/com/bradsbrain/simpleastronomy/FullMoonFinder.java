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

public class FullMoonFinder implements MoonFinder {

	private static final int _360 = 360;
	
	private static final double _180 = 180.0;

	public boolean isMoonBefore(double angle, double unused) {
		double usefulAngle = (angle + _180) % _360;
		
		return 180.0 > usefulAngle;
	}
}
