package org.aimas.cami.scheduler.CAMIScheduler.solver.move;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

public class ActivitySwapMoveFactory implements MoveListFactory<ActivitySchedule> {

	@Override
	public List<? extends Move<ActivitySchedule>> createMoveList(ActivitySchedule solution) {
		List<ActivitySwapMove> moveList = new ArrayList<>();
		List<Activity> activityList = solution.getActivityList();

		for (ListIterator<Activity> leftIt = activityList.listIterator(); leftIt.hasNext();) {
			Activity leftActivity = leftIt.next();
			for (ListIterator<Activity> rightIt = activityList.listIterator(leftIt.nextIndex()); rightIt.hasNext();) {
				Activity rightActivity = rightIt.next();
				moveList.add(new ActivitySwapMove(leftActivity, rightActivity));
			}
		}
		return moveList;
	}

}
