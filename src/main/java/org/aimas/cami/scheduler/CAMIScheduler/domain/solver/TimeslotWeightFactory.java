package org.aimas.cami.scheduler.CAMIScheduler.domain.solver;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ExcludedTimeslotsPenalty;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Timeslot;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;

/**
 * 
 * @author Bogdan
 *
 */
public class TimeslotWeightFactory implements SelectionSorterWeightFactory<ActivitySchedule, ActivityPeriod> {

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
					.append(period.getTimeslot().getTimeslot()[0].getHour(),
							weight.period.getTimeslot().getTimeslot()[0].getHour())
					.append(period.getTimeslot().getTimeslot()[0].getMinutes(),
							weight.period.getTimeslot().getTimeslot()[0].getMinutes())
					.append(period.getId(), weight.period.getId()).toComparison();
		}

	}

	@Override
	public Comparable createSorterWeight(ActivitySchedule solution, ActivityPeriod selection) {
		int penalty = 0;
		for (ExcludedTimeslotsPenalty etp : solution.getExcludedTimeslotsList()) {
			for (ActivityPeriod excludedPeriod : etp.getExcludedActivityPeriods()) {
				Timeslot excludedTimeslot = excludedPeriod.getTimeslot();
				if (selection.getTimeslot().getTimeslot()[0].getHour() >= excludedTimeslot.getTimeslot()[0].getHour()
						&& selection.getTimeslot().getTimeslot()[0].getMinutes() >= excludedTimeslot.getTimeslot()[0]
								.getMinutes()
						&& selection.getTimeslot().getTimeslot()[1].getHour() <= excludedTimeslot.getTimeslot()[1]
								.getHour()
						&& selection.getTimeslot().getTimeslot()[1].getMinutes() <= excludedTimeslot.getTimeslot()[1]
								.getMinutes()
						&& selection.getWeekDay() == excludedPeriod.getWeekDay()) {
					penalty++;
				}
			}
		}
		return new TimeslotWeight(selection, penalty);
	}

}
