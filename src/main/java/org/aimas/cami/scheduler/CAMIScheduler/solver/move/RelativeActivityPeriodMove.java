package org.aimas.cami.scheduler.CAMIScheduler.solver.move;

import java.util.Collection;
import java.util.Collections;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NormalRelativeActivity;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class RelativeActivityPeriodMove extends AbstractMove<ActivitySchedule> {

	private NormalRelativeActivity relativeActivity;
	private ActivityPeriod newActivityPeriod;

	public RelativeActivityPeriodMove(NormalRelativeActivity relativeActivity, ActivityPeriod newActivityPeriod) {
		super();
		this.relativeActivity = relativeActivity;
		this.newActivityPeriod = newActivityPeriod;
	}

	@Override
	public boolean isMoveDoable(ScoreDirector<ActivitySchedule> scoreDirector) {
		return relativeActivity.getActivityPeriodWeekday().getDayIndex() != newActivityPeriod.getWeekDayIndex();
	}

	@Override
	public Collection<? extends Object> getPlanningEntities() {
		return Collections.singletonList(relativeActivity);
	}

	@Override
	public Collection<? extends Object> getPlanningValues() {
		return Collections.singletonList(relativeActivity.getActivityPeriod());
	}

	@Override
	protected AbstractMove<ActivitySchedule> createUndoMove(ScoreDirector<ActivitySchedule> scoreDirector) {
		return new RelativeActivityPeriodMove(relativeActivity, relativeActivity.getActivityPeriod());
	}

	@Override
	protected void doMoveOnGenuineVariables(ScoreDirector<ActivitySchedule> scoreDirector) {
		scoreDirector.beforeVariableChanged(relativeActivity, "activityPeriod");
		relativeActivity.setActivityPeriod(newActivityPeriod);
		scoreDirector.afterVariableChanged(relativeActivity, "activityPeriod");
	}

	@Override
	public Move<ActivitySchedule> rebase(ScoreDirector<ActivitySchedule> destinationScoreDirector) {
		return new RelativeActivityPeriodMove(destinationScoreDirector.lookUpWorkingObject(relativeActivity),
				destinationScoreDirector.lookUpWorkingObject(newActivityPeriod));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((newActivityPeriod == null) ? 0 : newActivityPeriod.hashCode());
		result = prime * result + ((relativeActivity == null) ? 0 : relativeActivity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RelativeActivityPeriodMove))
			return false;
		RelativeActivityPeriodMove other = (RelativeActivityPeriodMove) obj;
		if (newActivityPeriod == null) {
			if (other.newActivityPeriod != null)
				return false;
		} else if (!newActivityPeriod.equals(other.newActivityPeriod))
			return false;
		if (relativeActivity == null) {
			if (other.relativeActivity != null)
				return false;
		} else if (!relativeActivity.equals(other.relativeActivity))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RelativeActivityPeriodMove [relativeActivity=" + relativeActivity + ", newActivityPeriod="
				+ newActivityPeriod + "]";
	}

}
