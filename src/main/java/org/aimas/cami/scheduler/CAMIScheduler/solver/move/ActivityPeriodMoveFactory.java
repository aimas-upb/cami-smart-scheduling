package org.aimas.cami.scheduler.CAMIScheduler.solver.move;

import java.util.ArrayList;
import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.TimeInterval;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

/**
 * Generate moves for ActivityPeriodMove class.
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
				int minutesInPermittedInterval = getNumberOfMinutesInPermittedInterval(activity.getPermittedInterval())
						- activity.getActivityDuration();
				for (int i = 1; i <= minutesInPermittedInterval; i++) {
					moveList.add(new ActivityPeriodMove(activity, i));
				}
			}
		}
		return moveList;
	}

	private Integer getNumberOfMinutesInPermittedInterval(TimeInterval permittedInterval) {

		return (permittedInterval.getMaxEnd().getHour() - permittedInterval.getMinStart().getHour()) * 60
				+ permittedInterval.getMaxEnd().getMinutes() - permittedInterval.getMinStart().getMinutes();
	}

}
