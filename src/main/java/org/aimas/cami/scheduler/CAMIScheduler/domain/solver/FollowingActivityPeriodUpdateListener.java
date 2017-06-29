package org.aimas.cami.scheduler.CAMIScheduler.domain.solver;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.FollowingActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.LeadingActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Timeslot;
import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class FollowingActivityPeriodUpdateListener implements VariableListener<Activity> {

	protected void updatePeriod(ScoreDirector scoreDirector, FollowingActivity followingActivity) {
		LeadingActivity leadingActivity = followingActivity.getActivityType().getPreviousActivity();
		ActivityPeriod period = leadingActivity.getActivityPeriod();

		scoreDirector.beforeVariableChanged(followingActivity, "delayperiod");
		followingActivity.setFollowingActivityPeriod(setPeriod(period, followingActivity));
		scoreDirector.afterVariableChanged(followingActivity, "delayperiod");
	}

	private ActivityPeriod setPeriod(ActivityPeriod period, FollowingActivity followingActivity) {
		Timeslot timeslot1 = period.getFixedTimeslot();
		int offsetTime = followingActivity.getOffset();

		Timeslot newTimeslot = new Timeslot();

		int[] newTimeslotInterval = new int[2];

		newTimeslotInterval[0] = timeslot1.getTimeslot()[0] + offsetTime;
		newTimeslotInterval[1] = timeslot1.getTimeslot()[1] + offsetTime;

		newTimeslot.setTimeslot(newTimeslotInterval);

		ActivityPeriod ap = new ActivityPeriod();
		ap.setFixedTimeslot(newTimeslot);
		ap.setWeekDay(period.getWeekDay());

		return ap;
	}

	@Override
	public void beforeEntityAdded(ScoreDirector scoreDirector, FollowingActivity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterEntityAdded(ScoreDirector scoreDirector, FollowingActivity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeVariableChanged(ScoreDirector scoreDirector, FollowingActivity entity) {
		// TODO Auto-generated method stub
		updatePeriod(scoreDirector, entity);
	}

	@Override
	public void afterVariableChanged(ScoreDirector scoreDirector, FollowingActivity entity) {
		// TODO Auto-generated method stub
		updatePeriod(scoreDirector, entity);
	}

	@Override
	public void beforeEntityRemoved(ScoreDirector scoreDirector, FollowingActivity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterEntityRemoved(ScoreDirector scoreDirector, FollowingActivity entity) {
		// TODO Auto-generated method stub

	}

}
