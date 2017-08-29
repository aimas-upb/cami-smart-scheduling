package org.aimas.cami.scheduler.CAMIScheduler.utils;

import java.util.LinkedHashSet;
import java.util.Set;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.PeriodInterval;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Time;
import org.aimas.cami.scheduler.CAMIScheduler.domain.TimeInterval;
import org.drools.core.spi.KnowledgeHelper;

/**
 * 
 * @author Bogdan
 *
 */
public class Utility {

	public static void main(String[] args) {
		System.out.println(fullOverlap(new Time(19, 05), new Time(19, 35), new Time(19, 04), new Time(19, 34)));
	}

	public static void help(final KnowledgeHelper drools, final String message) {
		System.out.println(message);
		System.out.println("rule triggered: " + drools.getRule().getName() + "\n");
	}

	public static void helper(final KnowledgeHelper drools) {
		System.out.println("\nrule triggered: " + drools.getRule().getName());
	}

	public static Integer getNumberOfMinutesInInterval(Time left, Time right) {

		return (right.getHour() - left.getHour()) * 60 + right.getMinutes() - left.getMinutes();
	}

	public static Set<Character> stringToCharacterSet(String s) {
		Set<Character> set = new LinkedHashSet<>();
		for (char c : s.toCharArray()) {
			set.add(c);
		}
		return set;
	}

	/**
	 * Check if the start of activity A is <= the end of activity B
	 * 
	 * @param startA
	 *            start of activity A
	 * @param endB
	 *            end of activity B
	 * @return true if start A is before end B, else false.
	 */
	public static boolean before(Time startA, Time endB) {
		if (startA.getHour() < endB.getHour())
			return true;
		else if (startA.getHour() == endB.getHour())
			if (startA.getMinutes() <= endB.getMinutes())
				return true;
		return false;
	}

	/**
	 * Check if the start of activity B is <= the end of activity A
	 * 
	 * @param startB
	 *            start of activity B
	 * @param endA
	 *            end of activity A
	 * @return true if end A is after start B, else false.
	 */
	public static boolean after(Time startB, Time endA) {
		if (startB.getHour() < endA.getHour())
			return true;
		else if (startB.getHour() == endA.getHour())
			if (startB.getMinutes() <= endA.getMinutes())
				return true;
		return false;
	}

	public static boolean exclusiveBefore(Time timeA, Time timeB) {
		if (timeA.getHour() < timeB.getHour())
			return true;
		else if (timeA.getHour() == timeB.getHour())
			if (timeA.getMinutes() < timeB.getMinutes())
				return true;
		return false;
	}

	public static boolean exclusiveAfter(Time timeA, Time timeB) {
		if (timeA.getHour() < timeB.getHour())
			return true;
		else if (timeA.getHour() == timeB.getHour())
			if (timeA.getMinutes() < timeB.getMinutes())
				return true;
		return false;
	}

	/**
	 * 
	 * @param startActivity
	 * @param endActivity
	 * @param startPermittedInterval
	 * @param endPermittedInterval
	 * @return true, if the activity and the permitted interval fully overlap.
	 */
	public static boolean fullOverlap(Time startActivity, Time endActivity, Time startPermittedInterval,
			Time endPermittedInterval) {

		if (before(startPermittedInterval, startActivity) && before(endActivity, endPermittedInterval))
			return true;

		return false;

	}

	public static Boolean checkTimeslots(ActivityPeriod activityPeriod, PeriodInterval excludedPeriodInterval,
			int activityDuration, boolean sameStartDay, boolean sameEndDay) {

		ActivityPeriod activityEndPeriod = AdjustActivityPeriod.getAdjustedPeriod(activityPeriod, activityDuration);
		Time activityStartTime = activityPeriod.getTime();
		Time activityEndTime = activityEndPeriod.getTime();
		Time excludedStartTime = excludedPeriodInterval.getStartPeriod().getTime();
		Time excludedEndTime = excludedPeriodInterval.getEndPeriod().getTime();

		if (((activityStartTime.getHour() > excludedStartTime.getHour()
				|| activityEndTime.getHour() > excludedStartTime.getHour()) && sameStartDay)
				|| (activityStartTime.getHour() < excludedEndTime.getHour() && sameEndDay)) {

			return true;

		} else if (sameStartDay) {

			if (activityStartTime.getHour() == excludedStartTime.getHour()) {

				if ((activityStartTime.getMinutes() + activityDuration) >= excludedStartTime.getMinutes()) {
					return true;
				}

			} else if (activityEndTime.getHour() == excludedStartTime.getHour()) {

				if (activityEndTime.getMinutes() > excludedStartTime.getMinutes()) {
					return true;
				}

			}

		} else if (sameEndDay) {

			if (activityStartTime.getHour() == excludedEndTime.getHour()) {

				if (activityStartTime.getMinutes() < excludedEndTime.getMinutes()) {
					return true;
				}

			}

		} else {

			if (before(activityStartTime, excludedEndTime) && after(excludedStartTime, activityEndTime))
				return true;

		}

		return false;
	}

}