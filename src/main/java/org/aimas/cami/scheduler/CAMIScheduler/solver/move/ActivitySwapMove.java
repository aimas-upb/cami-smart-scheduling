package org.aimas.cami.scheduler.CAMIScheduler.solver.move;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class ActivitySwapMove extends AbstractMove<ActivitySchedule> {

	private Activity leftActivity;
	private Activity rightActivity;

	public ActivitySwapMove(Activity leftActivity, Activity rightActivity) {
		super();
		this.leftActivity = leftActivity;
		this.rightActivity = rightActivity;
	}

	@Override
	public boolean isMoveDoable(ScoreDirector<ActivitySchedule> scoreDirector) {
		return Objects.equals(leftActivity.getActivityTypeCode(), rightActivity.getActivityTypeCode());
	}

	@Override
	public Collection<? extends Object> getPlanningEntities() {
		return Arrays.asList(leftActivity, rightActivity);
	}

	@Override
	public Collection<? extends Object> getPlanningValues() {
		return Arrays.asList(leftActivity.getActivityPeriod(), rightActivity.getActivityPeriod());
	}

	@Override
	protected AbstractMove<ActivitySchedule> createUndoMove(ScoreDirector<ActivitySchedule> scoreDirector) {
		return new ActivitySwapMove(rightActivity, leftActivity);
	}

	@Override
	protected void doMoveOnGenuineVariables(ScoreDirector<ActivitySchedule> scoreDirector) {
		ActivityPeriod oldLeftActivityPeriod = leftActivity.getActivityPeriod();
		ActivityPeriod oldRightActivityPeriod = rightActivity.getActivityPeriod();

		scoreDirector.beforeVariableChanged(leftActivity, "activityPeriod");
		leftActivity.setActivityPeriod(oldRightActivityPeriod);
		scoreDirector.afterVariableChanged(leftActivity, "activityPeriod");

		scoreDirector.beforeVariableChanged(rightActivity, "activityPeriod");
		rightActivity.setActivityPeriod(oldLeftActivityPeriod);
		scoreDirector.afterVariableChanged(rightActivity, "activityPeriod");
	}

	@Override
	public Move<ActivitySchedule> rebase(ScoreDirector<ActivitySchedule> destinationScoreDirector) {
		return new ActivitySwapMove(destinationScoreDirector.lookUpWorkingObject(leftActivity),
				destinationScoreDirector.lookUpWorkingObject(rightActivity));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leftActivity == null) ? 0 : leftActivity.hashCode());
		result = prime * result + ((rightActivity == null) ? 0 : rightActivity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ActivitySwapMove))
			return false;
		ActivitySwapMove other = (ActivitySwapMove) obj;
		if (leftActivity == null) {
			if (other.leftActivity != null)
				return false;
		} else if (!leftActivity.equals(other.leftActivity))
			return false;
		if (rightActivity == null) {
			if (other.rightActivity != null)
				return false;
		} else if (!rightActivity.equals(other.rightActivity))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActivitySwapMove [leftActivity=" + leftActivity + ", rightActivity=" + rightActivity + "]";
	}

}
