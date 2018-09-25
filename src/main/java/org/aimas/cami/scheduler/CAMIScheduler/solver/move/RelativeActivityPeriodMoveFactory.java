package org.aimas.cami.scheduler.CAMIScheduler.solver.move;

import java.util.ArrayList;
import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NormalActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NormalRelativeActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Time;
import org.aimas.cami.scheduler.CAMIScheduler.domain.WeekDay;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

public class RelativeActivityPeriodMoveFactory implements MoveListFactory<ActivitySchedule> {

	@Override
	public List<? extends Move<ActivitySchedule>> createMoveList(ActivitySchedule solution) {
		List<RelativeActivityPeriodMove> moveList = new ArrayList<>();
		List<Activity> activityList1 = solution.getActivityList();
		List<Activity> activityList2 = solution.getActivityList();

		for (Activity activity1 : activityList1) {
			if (activity1 instanceof NormalActivity
					&& ((NormalActivity) activity1).getAssignedToRelativeActivityMap() != null) {
				NormalActivity normalActivity = (NormalActivity) activity1;

				for (Activity activity2 : activityList2) {
					if (activity2 instanceof NormalRelativeActivity
							&& normalActivity.getAssignedToRelativeActivityMap()
									.containsKey(activity2.getActivityTypeCode())
							&& normalActivity.getAssignedToRelativeActivityMap().get(activity2.getActivityTypeCode())
									.equals(activity2.getId())) {
						NormalRelativeActivity normalRelativeActivity = (NormalRelativeActivity) activity2;

						// or normalActivity.getActivityPeriod())
						moveList.add(new RelativeActivityPeriodMove(normalRelativeActivity,
								findPeriod(solution, normalRelativeActivity.getActivityPeriodTime(),
										normalActivity.getActivityPeriodWeekday().getDayIndex())));
					}
				}

			}
		}

		return moveList;
	}

	private ActivityPeriod findPeriod(ActivitySchedule solution, Time time, int dayIndex) {
		for (ActivityPeriod activityPeriod : solution.getActivityPeriodList()) {
			if (activityPeriod.getTime().equals(time) && activityPeriod.getWeekDayIndex() == dayIndex)
				return activityPeriod;
		}

		// null -> new ActivityPeriod(0, time, dayIndex))));
		return null;
	}

}
