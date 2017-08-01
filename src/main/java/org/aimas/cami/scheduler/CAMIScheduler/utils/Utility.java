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

	public static void help(final KnowledgeHelper drools, final String message) {
		System.out.println(message);
		System.out.println("rule triggered: " + drools.getRule().getName() + "\n");
	}

	public static void helper(final KnowledgeHelper drools) {
		System.out.println("\nrule triggered: " + drools.getRule().getName());
	}

	public static Integer getNumberOfMinutesInPermittedInterval(TimeInterval permittedInterval) {

		return (permittedInterval.getMaxEnd().getHour() - permittedInterval.getMinStart().getHour()) * 60
				+ permittedInterval.getMaxEnd().getMinutes() - permittedInterval.getMinStart().getMinutes();
	}

	public static Set<Character> stringToCharacterSet(String s) {
		Set<Character> set = new LinkedHashSet<>();
		for (char c : s.toCharArray()) {
			set.add(c);
		}
		return set;
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
			if (activityStartTime.getHour() >= excludedStartTime.getHour()
					&& activityStartTime.getHour() < excludedEndTime.getHour()) {

				return true;

			}
		}

		return false;
	}

}