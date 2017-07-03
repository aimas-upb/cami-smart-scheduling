package org.aimas.cami.scheduler.CAMIScheduler.solver.move;

import java.util.Collection;
import java.util.Collections;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AdjustActivityPeriod;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * Set another period value to the planning variable(e.g. an activity planned on
 * a permitted timeslot 7:00-10:00, with a duration = 30m -> here are many
 * possibilities, rules will choose the best one based on the assigned score)
 * 
 * @author Bogdan
 *
 */
public class ActivityPeriodMove extends AbstractMove<ActivitySchedule> {

	private final Activity activity;
	private final int duration;

	public ActivityPeriodMove(Activity activity, int duration) {
		super();
		this.activity = activity;
		this.duration = duration;
	}

	@Override
	public boolean isMoveDoable(ScoreDirector<ActivitySchedule> scoreDirector) {
		if (duration == 0 || activity.getActivityPeriod() == null)
			return false;
		return true;
	}

	@Override
	public Collection<? extends Object> getPlanningEntities() {
		return Collections.singletonList(activity);
	}

	@Override
	public Collection<? extends Object> getPlanningValues() {
		return Collections.singletonList(activity.getActivityPeriod());
	}

	@Override
	protected AbstractMove<ActivitySchedule> createUndoMove(ScoreDirector<ActivitySchedule> scoreDirector) {
		return new ActivityPeriodMove(activity, -duration);
	}

	/**
	 * Change a planning variable
	 */
	@Override
	protected void doMoveOnGenuineVariables(ScoreDirector<ActivitySchedule> scoreDirector) {
		// take care to not move immovable entities
		// if this activity has set timesPerDay / timesPerWeek, then make a move in this direction, too
		// or create another AbstractMove
		scoreDirector.beforeVariableChanged(activity, "activityPeriod");
		activity.setActivityPeriod(AdjustActivityPeriod.setPeriod(activity.getActivityPeriod(), duration));
		scoreDirector.afterVariableChanged(activity, "activityPeriod");
	}

}
