package org.aimas.cami.scheduler.CAMIScheduler.domain.solver;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NormalActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NormalRelativeActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.RelativeActivityPenalty;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AdjustActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.utils.Utility;
import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * 
 * @author Bogdan
 *
 */
public class RelativeActivityPeriodUpdateListener implements VariableListener<NormalActivity> {

	protected void updatePeriod(ScoreDirector scoreDirector, NormalActivity activityEntity) {

		ActivitySchedule activitySchedule = (ActivitySchedule) scoreDirector.getWorkingSolution();

		for (RelativeActivityPenalty rap : activitySchedule.getRelativeActivityPenaltyList()) {

			boolean rightEntity = activityEntity.getActivityTypeCode().equals(rap.getStaticActivityType());

			if (activityEntity.getActivityCategory() != null
					&& activityEntity.getActivityCategory().getCode().equals(rap.getCategory()))
				rightEntity = true;

			if (rightEntity) {

				for (Activity activity : activitySchedule.getActivityList()) {
					if (activity instanceof NormalRelativeActivity) {
						NormalRelativeActivity relativeActivity = (NormalRelativeActivity) activity;

						if (relativeActivity.getActivityTypeCode().equals(rap.getRelativeActivityType())) {

							if (!activityEntity.getAssignedToRelativeActivityMap()
									.containsKey(relativeActivity.getActivityTypeCode())
									&& !relativeActivity.isAssigned()) {

								scoreDirector.beforeProblemPropertyChanged(activityEntity);
								activityEntity.getAssignedToRelativeActivityMap()
										.put(relativeActivity.getActivityTypeCode(), relativeActivity.getId());
								scoreDirector.afterProblemPropertyChanged(activityEntity);

								scoreDirector.beforeProblemPropertyChanged(relativeActivity);
								relativeActivity.setAssigned(true);
								scoreDirector.afterProblemPropertyChanged(relativeActivity);

							}

							if (relativeActivity != null && activityEntity.getActivityPeriod() != null) {

								if (activityEntity.getAssignedToRelativeActivityMap()
										.containsKey(relativeActivity.getActivityTypeCode())
										&& activityEntity.getAssignedToRelativeActivityMap()
												.get(relativeActivity.getActivityTypeCode())
												.equals(relativeActivity.getId())) {

									if (relativeActivity.getOffset() < 0) {

										ActivityPeriod period = Utility.getRelativeActivityPeriod(activitySchedule,
												relativeActivity,
												AdjustActivityPeriod.getAdjustedPeriod(
														activityEntity.getActivityPeriod(),
														relativeActivity.getOffset()
																- relativeActivity.getActivityType().getDuration()),
												-5);

										scoreDirector.beforeVariableChanged(relativeActivity, "activityPeriod");
										relativeActivity.setActivityPeriod(period);
										scoreDirector.afterVariableChanged(relativeActivity, "activityPeriod");

									} else {

										ActivityPeriod period = Utility.getRelativeActivityPeriod(activitySchedule,
												relativeActivity,
												AdjustActivityPeriod.getAdjustedPeriod(
														activityEntity.getActivityEndPeriod(),
														relativeActivity.getOffset()),
												5);

										scoreDirector.beforeVariableChanged(relativeActivity, "activityPeriod");
										relativeActivity.setActivityPeriod(period);
										scoreDirector.afterVariableChanged(relativeActivity, "activityPeriod");

									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void beforeEntityAdded(ScoreDirector scoreDirector, NormalActivity entity) {

	}

	@Override
	public void afterEntityAdded(ScoreDirector scoreDirector, NormalActivity entity) {

	}

	@Override
	public void beforeVariableChanged(ScoreDirector scoreDirector, NormalActivity entity) {
		updatePeriod(scoreDirector, entity);

	}

	@Override
	public void afterVariableChanged(ScoreDirector scoreDirector, NormalActivity entity) {
		updatePeriod(scoreDirector, entity);

	}

	@Override
	public void beforeEntityRemoved(ScoreDirector scoreDirector, NormalActivity entity) {

	}

	@Override
	public void afterEntityRemoved(ScoreDirector scoreDirector, NormalActivity entity) {

	}

}
