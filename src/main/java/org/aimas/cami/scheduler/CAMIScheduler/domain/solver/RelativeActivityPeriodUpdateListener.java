package org.aimas.cami.scheduler.CAMIScheduler.domain.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.RelativeActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.RelativeActivityPenalty;
import org.aimas.cami.scheduler.CAMIScheduler.domain.WeekDay;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AdjustActivityPeriod;
import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * 
 * @author Bogdan
 *
 */
public class RelativeActivityPeriodUpdateListener implements VariableListener<Activity> {

	protected void updatePeriod(ScoreDirector scoreDirector, Activity activityEntity) {

		ActivitySchedule activitySchedule = (ActivitySchedule) scoreDirector.getWorkingSolution();

		for (RelativeActivityPenalty rap : activitySchedule.getRelativeActivityPenaltyList()) {

			boolean rightEntity = activityEntity.getActivityTypeCode().equals(rap.getStaticActivityType())
					|| activityEntity.getActivityCategory().getCode().equals(rap.getCategory());

			if (rightEntity) {

				for (Activity activity : activitySchedule.getActivityList()) {
					if (activity instanceof RelativeActivity) {
						RelativeActivity relativeActivity = (RelativeActivity) activity;

						if (relativeActivity.getActivityTypeCode().equals(rap.getRelativeActivityType())) {

							if (!activityEntity.getAssignedRelativeActivityList()
									.containsKey(relativeActivity.getActivityTypeCode())
									&& !relativeActivity.isAssigned()) {

								scoreDirector.beforeProblemPropertyChanged(activityEntity);
								activityEntity.getAssignedRelativeActivityList()
										.put(relativeActivity.getActivityTypeCode(), relativeActivity.getId());
								scoreDirector.afterProblemPropertyChanged(activityEntity);

								scoreDirector.beforeProblemPropertyChanged(relativeActivity);
								relativeActivity.setAssigned(true);
								scoreDirector.afterProblemPropertyChanged(relativeActivity);

							}

							if (relativeActivity != null && activityEntity.getActivityPeriod() != null) {

								if (activityEntity.getAssignedRelativeActivityList()
										.containsKey(relativeActivity.getActivityTypeCode())
										&& activityEntity.getAssignedRelativeActivityList()
												.get(relativeActivity.getActivityTypeCode())
												.equals(relativeActivity.getId())) {

									if (relativeActivity.getOffset() < 0) {
										scoreDirector.beforeVariableChanged(relativeActivity, "relativeActivityPeriod");
										relativeActivity
												.setRelativeActivityPeriod(AdjustActivityPeriod.getAdjustedPeriod(
														activityEntity.getActivityPeriod(), relativeActivity.getOffset()
																- relativeActivity.getActivityType().getDuration()));
										scoreDirector.afterVariableChanged(relativeActivity, "relativeActivityPeriod");

									} else {
										scoreDirector.beforeVariableChanged(relativeActivity, "relativeActivityPeriod");
										relativeActivity.setRelativeActivityPeriod(AdjustActivityPeriod
												.getAdjustedPeriod(activityEntity.getActivityEndPeriod(),
														relativeActivity.getOffset()));
										scoreDirector.afterVariableChanged(relativeActivity, "relativeActivityPeriod");

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
	public void beforeEntityAdded(ScoreDirector scoreDirector, Activity entity) {

	}

	@Override
	public void afterEntityAdded(ScoreDirector scoreDirector, Activity entity) {

	}

	@Override
	public void beforeVariableChanged(ScoreDirector scoreDirector, Activity entity) {
		updatePeriod(scoreDirector, entity);

	}

	@Override
	public void afterVariableChanged(ScoreDirector scoreDirector, Activity entity) {
		updatePeriod(scoreDirector, entity);

	}

	@Override
	public void beforeEntityRemoved(ScoreDirector scoreDirector, Activity entity) {

	}

	@Override
	public void afterEntityRemoved(ScoreDirector scoreDirector, Activity entity) {

	}

}
