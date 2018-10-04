package org.aimas.cami.scheduler.CAMIScheduler.solver.move;

import java.util.Collection;
import java.util.Collections;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NormalActivity;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class NormalActivityPeriodMove extends AbstractMove<ActivitySchedule> {
	private NormalActivity normalActivity;
	private ActivityPeriod newActivityPeriod;

	public NormalActivityPeriodMove(NormalActivity normalActivity, ActivityPeriod newActivityPeriod) {
		super();
		this.normalActivity = normalActivity;
		this.newActivityPeriod = newActivityPeriod;
	}

	@Override
	public boolean isMoveDoable(ScoreDirector<ActivitySchedule> scoreDirector) {
		return normalActivity.getActivityPeriodWeekday().getDayIndex() != newActivityPeriod.getWeekDayIndex();
	}

	@Override
	public Collection<? extends Object> getPlanningEntities() {
		return Collections.singletonList(normalActivity);
	}

	@Override
	public Collection<? extends Object> getPlanningValues() {
		return Collections.singletonList(normalActivity.getActivityPeriod());
	}

	@Override
	protected AbstractMove<ActivitySchedule> createUndoMove(ScoreDirector<ActivitySchedule> scoreDirector) {
		return new NormalActivityPeriodMove(normalActivity, normalActivity.getActivityPeriod());
	}

	@Override
	protected void doMoveOnGenuineVariables(ScoreDirector<ActivitySchedule> scoreDirector) {
		scoreDirector.beforeVariableChanged(normalActivity, "activityPeriod");
		normalActivity.setActivityPeriod(newActivityPeriod);
		scoreDirector.afterVariableChanged(normalActivity, "activityPeriod");
	}

	@Override
	public Move<ActivitySchedule> rebase(ScoreDirector<ActivitySchedule> destinationScoreDirector) {
		return new NormalActivityPeriodMove(destinationScoreDirector.lookUpWorkingObject(normalActivity),
				destinationScoreDirector.lookUpWorkingObject(newActivityPeriod));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((newActivityPeriod == null) ? 0 : newActivityPeriod.hashCode());
		result = prime * result + ((normalActivity == null) ? 0 : normalActivity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof NormalActivityPeriodMove))
			return false;
		NormalActivityPeriodMove other = (NormalActivityPeriodMove) obj;
		if (newActivityPeriod == null) {
			if (other.newActivityPeriod != null)
				return false;
		} else if (!newActivityPeriod.equals(other.newActivityPeriod))
			return false;
		if (normalActivity == null) {
			if (other.normalActivity != null)
				return false;
		} else if (!normalActivity.equals(other.normalActivity))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NormalActivityPeriodMove [normalActivity=" + normalActivity + ", newActivityPeriod=" + newActivityPeriod
				+ "]";
	}

}
