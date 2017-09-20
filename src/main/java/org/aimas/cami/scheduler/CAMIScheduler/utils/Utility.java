package org.aimas.cami.scheduler.CAMIScheduler.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NormalRelativeActivity;
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

	public static Integer getNumberOfMinutesInPeriodInterval(int dayIndexLeft, int dayIndexRight, Time left,
			Time right) {

		return ((dayIndexRight - dayIndexLeft) * 24 + (right.getHour() - left.getHour())) * 60 + right.getMinutes()
				- left.getMinutes();
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

	/**
	 * Get all the free periods from the schedule(except some periods that are
	 * constrained).
	 * 
	 * @param activitySchedule
	 * @param activityEntity
	 * @return List<ActivityPeriod> activityPeriodList
	 */
	public static List<ActivityPeriod> getFreePeriods(ActivitySchedule activitySchedule, Activity activityEntity) {
		List<ActivityPeriod> activityPeriodList = new ArrayList<>();
		boolean overlapFound = false;

		for (ActivityPeriod activityPeriod : activitySchedule.getActivityPeriodList()) {
			overlapFound = false;
			ActivityPeriod activityEndPeriod = AdjustActivityPeriod.getAdjustedPeriod(activityPeriod,
					activityEntity.getActivityDuration());

			if (activityPeriod.getPeriodHour() >= 6) {

				overlapFound = findOverlap(activitySchedule, activityPeriod, activityEndPeriod);

				if (!overlapFound) {
					activityPeriodList.add(activityPeriod);
				}
			}
		}
		return activityPeriodList;
	}

	public static List<ActivityPeriod> getFreePeriodsInInterval(ActivitySchedule activitySchedule,
			Activity activityEntity, TimeInterval timeInterval, int dayIndex) {

		List<ActivityPeriod> activityPeriodList = new ArrayList<>();
		boolean overlapFound = false;

		for (ActivityPeriod activityPeriod : activitySchedule.getActivityPeriodList()) {

			overlapFound = false;
			ActivityPeriod activityEndPeriod = AdjustActivityPeriod.getAdjustedPeriod(activityPeriod,
					activityEntity.getActivityDuration());

			if (activityPeriod.getWeekDayIndex() == dayIndex && activityPeriod.getPeriodHour() >= 6
					&& fullOverlap(activityPeriod.getTime(), activityEndPeriod.getTime(), timeInterval.getMinStart(),
							timeInterval.getMaxEnd())) {

				overlapFound = findOverlap(activitySchedule, activityPeriod, activityEndPeriod);

				if (!overlapFound) {
					activityPeriodList.add(activityPeriod);
				}
			}
		}
		return activityPeriodList;
	}

	public static List<ActivityPeriod> getFreePeriodsLaterThisWeek(ActivitySchedule activitySchedule,
			Activity activityEntity, int dayIndex) {

		List<ActivityPeriod> activityPeriodList = new ArrayList<>();
		boolean overlapFound = false;

		for (ActivityPeriod activityPeriod : activitySchedule.getActivityPeriodList()) {

			overlapFound = false;

			if (activityPeriod.getWeekDayIndex() > dayIndex && activityPeriod.getPeriodHour() >= 6) {

				ActivityPeriod activityEndPeriod = AdjustActivityPeriod.getAdjustedPeriod(activityPeriod,
						activityEntity.getActivityDuration());

				overlapFound = findOverlap(activitySchedule, activityPeriod, activityEndPeriod);

				if (!overlapFound) {
					activityPeriodList.add(activityPeriod);
				}
			}
		}
		return activityPeriodList;
	}

	public static ActivityPeriod getRelativeActivityPeriod(ActivitySchedule activitySchedule, Activity activityEntity,
			ActivityPeriod activityPeriod, int increment) {

		boolean overlapFound = false;

		while (true) {

			overlapFound = false;
			ActivityPeriod activityEndPeriod = AdjustActivityPeriod.getAdjustedPeriod(activityPeriod,
					activityEntity.getActivityDuration());

			overlapFound = findOverlap(activitySchedule, activityPeriod, activityEndPeriod);

			if (!overlapFound) {
				return activityPeriod;
			}

			activityPeriod = AdjustActivityPeriod.getAdjustedPeriod(activityPeriod, increment);
		}

	}

	private static boolean findOverlap(ActivitySchedule activitySchedule, ActivityPeriod activityPeriod,
			ActivityPeriod activityEndPeriod) {
		for (Activity activity : activitySchedule.getActivityList()) {
			if (activity.getActivityPeriod() != null
					&& activityPeriod.getWeekDayIndex() == activity.getActivityPeriodWeekday().getDayIndex()) {

				if (Utility.before(activityPeriod.getTime(), activity.getActivityEndPeriod().getTime())
						&& Utility.after(activity.getActivityPeriodTime(), activityEndPeriod.getTime())) {
					return true;

				}
			}
		}

		return false;
	}

	public static Boolean checkTimeslots(ActivityPeriod activityPeriod, PeriodInterval excludedPeriodInterval,
			int activityDuration, boolean sameStartDay, boolean sameEndDay) {

		ActivityPeriod activityEndPeriod = AdjustActivityPeriod.getAdjustedPeriod(activityPeriod, activityDuration);
		Time activityStartTime = activityPeriod.getTime();
		Time activityEndTime = activityEndPeriod.getTime();
		Time excludedStartTime = excludedPeriodInterval.getStartPeriod().getTime();
		Time excludedEndTime = excludedPeriodInterval.getEndPeriod().getTime();

		if (sameStartDay) {

			if (before(activityStartTime, new Time(24, 0)) && after(excludedStartTime, activityEndTime))
				return true;

		} else if (sameEndDay) {

			if (before(activityStartTime, excludedEndTime) && after(new Time(0, 0), activityEndTime))
				return true;

		} else {

			if (before(activityStartTime, excludedEndTime) && after(excludedStartTime, activityEndTime))
				return true;

		}

		return false;
	}

}