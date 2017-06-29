package org.aimas.cami.scheduler.CAMIScheduler.domain.solver;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ExcludedTimeslotsPenalty;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Timeslot;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;

public class TimeslotWeightFactory implements SelectionSorterWeightFactory<ActivitySchedule, Timeslot> {

	@Override
	public Comparable createSorterWeight(ActivitySchedule solution, Timeslot selection) {
		int penalty = 0;
		for (ExcludedTimeslotsPenalty etp : solution.getExcludedTimeslotsList()) {
			Timeslot excluded = etp.getExcludedTimeslot();
			if (selection.getTimeslot()[0] >= excluded.getTimeslot()[0]
					&& selection.getTimeslot()[1] <= excluded.getTimeslot()[1]) {
				penalty++;
			}
		}
		return new TimeslotWeight(selection, penalty);
	}

	public static class TimeslotWeight implements Comparable<TimeslotWeight> {

		private final Timeslot timeslot;
		private final int penalty;

		public TimeslotWeight(Timeslot timeslot, int penalty) {
			this.timeslot = timeslot;
			this.penalty = penalty;
		}

		@Override
		public int compareTo(TimeslotWeight weight) {
			// bigger penalty -> weaker
			return new CompareToBuilder().append(weight.penalty, this.penalty)
					.append(timeslot.getTimeslot(), weight.timeslot.getTimeslot())
					.append(timeslot.getId(), weight.timeslot.getId()).toComparison();
		}

	}

}
