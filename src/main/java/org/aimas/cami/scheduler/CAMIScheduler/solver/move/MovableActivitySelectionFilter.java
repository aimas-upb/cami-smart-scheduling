package org.aimas.cami.scheduler.CAMIScheduler.solver.move;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class MovableActivitySelectionFilter implements SelectionFilter<ActivitySchedule, Activity> {

	@Override
	public boolean accept(ScoreDirector<ActivitySchedule> scoreDirector, Activity activity) {
		return !activity.isImmovable();
	}

}
