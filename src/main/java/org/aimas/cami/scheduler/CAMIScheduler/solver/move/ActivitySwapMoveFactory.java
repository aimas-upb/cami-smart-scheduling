package org.aimas.cami.scheduler.CAMIScheduler.solver.move;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NormalActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.RelativeActivityPenalty;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

public class ActivitySwapMoveFactory implements MoveListFactory<ActivitySchedule> {

	@Override
	public List<? extends Move<ActivitySchedule>> createMoveList(ActivitySchedule solution) {
		List<ActivitySwapMove> moveList = new ArrayList<>();
		List<Activity> activityList = solution.getActivityList();

		for (ListIterator<Activity> leftIt = activityList.listIterator(); leftIt.hasNext();) {
			Activity leftActivity = leftIt.next();
			if (leftActivity.isImmovable() || !haveRelativeConstraints(leftActivity, solution))
				continue;
			for (ListIterator<Activity> rightIt = activityList.listIterator(leftIt.nextIndex()); rightIt.hasNext();) {
				Activity rightActivity = rightIt.next();
				if (rightActivity.isImmovable() || !haveRelativeConstraints(rightActivity, solution))
					continue;
				moveList.add(new ActivitySwapMove(leftActivity, rightActivity));
			}
		}
		return moveList;
	}

	private boolean haveRelativeConstraints(Activity activity, ActivitySchedule solution) {

		for (RelativeActivityPenalty rap : solution.getRelativeActivityPenaltyList()) {
			// if it is part of a relativity constraint

			if (activity instanceof NormalActivity) {
				if (rap.getCategory() != null && activity.getActivityCategory().getCode().equals(rap.getCategory()))
					return true;
			} else {
				if (rap.getCategory() != null && rap.getRelativeActivityType().equals(activity.getActivityTypeCode()))
					return true;
			}

		}
		return false;
	}

}
