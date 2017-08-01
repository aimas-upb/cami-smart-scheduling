package org.aimas.cami.scheduler.CAMIScheduler.solver.move;

import java.util.Collection;
import java.util.Collections;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.RelativeActivity;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AdjustActivityPeriod;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * 
 * @author Bogdan
 *
 */
public class RelativeActivityPeriodMove extends AbstractMove<ActivitySchedule> {

	private final RelativeActivity relativeActivity;
	private final int offset;

	public RelativeActivityPeriodMove(RelativeActivity relativeActivity, int offset) {
		super();
		this.relativeActivity = relativeActivity;
		this.offset = offset;
	}

	@Override
	public boolean isMoveDoable(ScoreDirector<ActivitySchedule> scoreDirector) {
		return true;
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
		return new RelativeActivityPeriodMove(relativeActivity, -offset);
	}

	@Override
	protected void doMoveOnGenuineVariables(ScoreDirector<ActivitySchedule> scoreDirector) {
		scoreDirector.beforeVariableChanged(relativeActivity, "activityPeriod");
		relativeActivity.setActivityPeriod(AdjustActivityPeriod
				.getAdjustedPeriod(((RelativeActivity) relativeActivity).getActivityPeriod(), offset));
		scoreDirector.afterVariableChanged(relativeActivity, "activityPeriod");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + offset;
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
		if (offset != other.offset)
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
		return "RelativeActivityPeriodMove [relativeActivity=" + relativeActivity + ", offset=" + offset + "]";
	}

}
