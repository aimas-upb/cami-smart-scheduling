package org.aimas.cami.scheduler.CAMIScheduler.domain.solver;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.RelativeActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Timeslot;
import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class RelativeActivityPeriodUpdateListener implements VariableListener<Activity> {

	protected void updatePeriod(ScoreDirector scoreDirector, Activity Activity) {
		RelativeActivity relativeActivity = Activity.getRelativeActivity();
		ActivityPeriod period = relativeActivity.getActivityPeriod();

		scoreDirector.beforeVariableChanged(relativeActivity, "delayperiod");
		relativeActivity.setRelativeActivityPeriod(setPeriod(period, relativeActivity.getOffset()));
		scoreDirector.afterVariableChanged(relativeActivity, "delayperiod");
	}

	private ActivityPeriod setPeriod(ActivityPeriod period, int offsetTime) {
		Timeslot timeslot1 = period.getTimeslot();
		Timeslot newTimeslot = new Timeslot();
		int[] newTimeslotInterval = new int[2];

		newTimeslotInterval[0] = timeslot1.getTimeslot()[0] + offsetTime;
		newTimeslotInterval[1] = timeslot1.getTimeslot()[1] + offsetTime;

		newTimeslot.setTimeslot(newTimeslotInterval);

		ActivityPeriod ap = new ActivityPeriod();
		ap.setTimeslot(newTimeslot);
		ap.setWeekDay(period.getWeekDay());

		return ap;
	}

	@Override
	public void beforeEntityAdded(ScoreDirector scoreDirector, Activity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterEntityAdded(ScoreDirector scoreDirector, Activity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeVariableChanged(ScoreDirector scoreDirector, Activity entity) {
		// TODO Auto-generated method stub
		updatePeriod(scoreDirector, entity);
	}

	@Override
	public void afterVariableChanged(ScoreDirector scoreDirector, Activity entity) {
		// TODO Auto-generated method stub
		updatePeriod(scoreDirector, entity);
	}

	@Override
	public void beforeEntityRemoved(ScoreDirector scoreDirector, Activity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterEntityRemoved(ScoreDirector scoreDirector, Activity entity) {
		// TODO Auto-generated method stub

	}

}
