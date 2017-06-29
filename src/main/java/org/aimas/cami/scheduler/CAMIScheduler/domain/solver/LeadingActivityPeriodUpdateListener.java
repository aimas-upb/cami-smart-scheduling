package org.aimas.cami.scheduler.CAMIScheduler.domain.solver;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.FollowingActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.LeadingActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Timeslot;
import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class LeadingActivityPeriodUpdateListener implements VariableListener<LeadingActivity> {

	protected void updatePeriod(ScoreDirector scoreDirector, LeadingActivity leadingActivity) {
		FollowingActivity followingActivity = leadingActivity.getActivityType().getFollowingActivity();
		ActivityPeriod period = followingActivity.getActivityPeriod();

		scoreDirector.beforeVariableChanged(leadingActivity, "advanceperiod");
		leadingActivity.setLeadingActivityPeriod(setPeriod(period, leadingActivity));
		scoreDirector.afterVariableChanged(leadingActivity, "advanceperiod");
	}

	private ActivityPeriod setPeriod(ActivityPeriod period, LeadingActivity leadingActivity) {
		Timeslot timeslot1 = period.getFixedTimeslot();
		int offsetTime = leadingActivity.getOffset();

		Timeslot newTimeslot = new Timeslot();

		int[] newTimeslotInterval = new int[2];

		newTimeslotInterval[0] = timeslot1.getTimeslot()[0] - offsetTime;
		newTimeslotInterval[1] = timeslot1.getTimeslot()[1] - offsetTime;

		newTimeslot.setTimeslot(newTimeslotInterval);

		ActivityPeriod ap = new ActivityPeriod();
		ap.setFixedTimeslot(newTimeslot);
		ap.setWeekDay(period.getWeekDay());

		return ap;
	}

	@Override
	public void beforeEntityAdded(ScoreDirector scoreDirector, LeadingActivity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterEntityAdded(ScoreDirector scoreDirector, LeadingActivity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeVariableChanged(ScoreDirector scoreDirector, LeadingActivity entity) {
		// TODO Auto-generated method stub
		updatePeriod(scoreDirector, entity);
	}

	@Override
	public void afterVariableChanged(ScoreDirector scoreDirector, LeadingActivity entity) {
		// TODO Auto-generated method stub
		updatePeriod(scoreDirector, entity);
	}

	@Override
	public void beforeEntityRemoved(ScoreDirector scoreDirector, LeadingActivity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterEntityRemoved(ScoreDirector scoreDirector, LeadingActivity entity) {
		// TODO Auto-generated method stub

	}

}
