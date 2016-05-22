package com.bradsbrain.simpleastronomy;

import java.time.ZonedDateTime;

/**
 * Determines how many days to look ahead when first starting
 * to find a MoonPhase.  This solves problems with using a binary
 * search mechanism with moon phases in edge cases near a moon phase.
 */
public interface LookAheadDays {

	/**
	 * Specify the number of days to look ahead when search for a phase,
	 * allows binary search mechanism to work in edge cases.
	 * 
	 * @param startDate days to look ahead when searching for a phase
	 * @return a number of days
	 */
	public int daysAheadToLook(ZonedDateTime startDate);
	
}
