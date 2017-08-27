package org.aimas.cami.scheduler.CAMIScheduler.domain.solver;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ExcludedTimePeriodsPenalty;
import org.aimas.cami.scheduler.CAMIScheduler.domain.PeriodInterval;
import org.aimas.cami.scheduler.CAMIScheduler.utils.Utility;
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
		public int compareTo(TimeslotWeight other) {
			// bigger penalty -> weaker
			return new CompareToBuilder().append(other.penalty, this.penalty)
					.append(period.getWeekDay().getDayIndex(), other.period.getWeekDay().getDayIndex())
					.append(period.getTime(), other.period.getTime())
					.append(period.getId(), other.period.getId()).toComparison();
		}

	}

	@Override
	public Comparable createSorterWeight(ActivitySchedule solution, ActivityPeriod activityPeriod) {
		int penalty = 0;
		for (ExcludedTimePeriodsPenalty etp : solution.getExcludedTimePeriodsList()) {
			for (PeriodInterval excludedPeriod : etp.getExcludedActivityPeriods()) {
				if ((excludedPeriod.getStartPeriod().getWeekDay() == null
						&& excludedPeriod.getEndPeriod().getWeekDay() == null)
						|| (activityPeriod.getWeekDay().getDayIndex() == excludedPeriod.getStartPeriod().getWeekDay()
								.getDayIndex()
								&& activityPeriod.getWeekDay().getDayIndex() == excludedPeriod.getEndPeriod()
										.getWeekDay().getDayIndex())) {

					if (Utility.checkTimeslots(activityPeriod, excludedPeriod, etp.getActivityType().getDuration(),
							false, false)) {
						penalty++;
					}

				}
			}
		}
		return new TimeslotWeight(activityPeriod, penalty);
	}

}
