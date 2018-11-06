package org.aimas.cami.scheduler.CAMIScheduler.solver.move;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NormalActivity;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class SameActivitySwapMoveSelector implements SelectionFilter<ActivitySchedule, SwapMove> {

	@Override
	public boolean accept(ScoreDirector<ActivitySchedule> scoreDirector, SwapMove move) {
		Activity leftActivity = (Activity) move.getLeftEntity();
		Activity rightActivity = (Activity) move.getRightEntity();
		boolean decision = leftActivity.getActivityTypeCode().equals(rightActivity.getActivityTypeCode());
		return decision;
	}

}
