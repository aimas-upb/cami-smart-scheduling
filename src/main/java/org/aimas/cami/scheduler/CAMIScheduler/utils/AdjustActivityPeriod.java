package org.aimas.cami.scheduler.CAMIScheduler.utils;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Time;

/**
 * Shift a period(to left or to right).
 * 
 * @author Bogdan
 *
 */
public class AdjustActivityPeriod {

	/**
	 * Shift a period.
	 * 
	 * @param period
	 *            {@link ActivityPeriod}
	 * @param offsetTime
	 *            {@link Integer}
	 * @return the shifted period.
	 */
	public static ActivityPeriod getAdjustedPeriod(ActivityPeriod period, int offsetTime) {
		Time time = period.getTime();

		return new ActivityPeriod(new Time(time.getTime() + offsetTime), period.getWeekDay());
	}

	/**
	 * Shift a time.
	 * 
	 * @param time
	 *            {@link Time}
	 * @param offsetTime
	 *            {@link Integer}
	 * @return the shifted time.
	 */
	public static Time getAdjustedTime(Time time, int offsetTime) {

		return new Time(time.getTime() + offsetTime);
	}

}
