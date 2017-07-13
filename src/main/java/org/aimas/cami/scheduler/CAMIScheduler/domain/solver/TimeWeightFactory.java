package org.aimas.cami.scheduler.CAMIScheduler.domain.solver;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ExcludedTimePeriodsPenalty;
import org.aimas.cami.scheduler.CAMIScheduler.domain.PeriodInterval;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Time;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;

/**
 * 
 * @author Bogdan
 *
 */
public class TimeWeightFactory implements SelectionSorterWeightFactory<ActivitySchedule, ActivityPeriod> {

	public static class TimeslotWeight implements Comparable<TimeslotWeight> {

		private final ActivityPeriod period;
		private final int penalty;

		public TimeslotWeight(ActivityPeriod period, int penalty) {
			super();
			this.period = period;
			this.penalty = penalty;
		}

		@Override
		public int compareTo(TimeslotWeight weight) {
			// bigger penalty -> weaker
			return new CompareToBuilder().append(weight.penalty, this.penalty)
					.append(period.getWeekDay().getDayIndex(), weight.period.getWeekDay().getDayIndex())
					.append(period.getTime().getHour(), weight.period.getTime().getHour())
					.append(period.getTime().getMinutes(), weight.period.getTime().getMinutes())
					.append(period.getId(), weight.period.getId()).toComparison();
		}

	}

	@Override
	public Comparable createSorterWeight(ActivitySchedule solution, ActivityPeriod activityPeriod) {
		int penalty = 0;
		for (ExcludedTimePeriodsPenalty etp : solution.getExcludedTimePeriodsList()) {
			for (PeriodInterval excludedPeriod : etp.getExcludedActivityPeriods()) {
				Time leftExcludedTimeslot = excludedPeriod.getStartPeriod().getTime();
				Time rightExcludedTimeslot = excludedPeriod.getEndPeriod().getTime();
				if (activityPeriod.getTime().getHour() >= leftExcludedTimeslot.getHour()
						&& activityPeriod.getTime().getMinutes() >= leftExcludedTimeslot.getMinutes()
						&& activityPeriod.getTime().getHour() <= rightExcludedTimeslot.getHour()
						&& activityPeriod.getTime().getMinutes() <= rightExcludedTimeslot.getMinutes()
						&& (activityPeriod.getWeekDay() == excludedPeriod.getStartPeriod().getWeekDay()
								|| activityPeriod.getWeekDay() == excludedPeriod.getEndPeriod().getWeekDay())) {
					penalty++;
				}
			}
		}
		return new TimeslotWeight(activityPeriod, penalty);
	}

}
