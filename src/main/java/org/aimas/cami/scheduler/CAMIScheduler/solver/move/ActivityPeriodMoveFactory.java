package org.aimas.cami.scheduler.CAMIScheduler.solver.move;

import java.util.ArrayList;
import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.TimeInterval;
import org.aimas.cami.scheduler.CAMIScheduler.utils.Utility;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

/**
 * Generate moves for activities.
 * 
 * @author Bogdan
 *
 */
public class ActivityPeriodMoveFactory implements MoveListFactory<ActivitySchedule> {

	@Override
	public List<ActivityPeriodMove> createMoveList(ActivitySchedule activitySchedule) {
		List<ActivityPeriodMove> moveList = new ArrayList<>();
		List<Activity> activityList = activitySchedule.getActivityList();

		for (Activity activity : activityList) {
			if (activity.getPermittedInterval() != null) {
				for (TimeInterval permittedInterval : activity.getPermittedInterval()) {
					int minutesInPermittedInterval = Utility.getNumberOfMinutesInPermittedInterval(permittedInterval)
							- activity.getActivityDuration();
					for (int i = 1; i <= minutesInPermittedInterval; i++) {
						moveList.add(new ActivityPeriodMove(activity, i));
					}
				}
			}
		}
		return moveList;
	}

}
