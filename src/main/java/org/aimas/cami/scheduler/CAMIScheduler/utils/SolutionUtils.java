package org.aimas.cami.scheduler.CAMIScheduler.utils;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityType;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ExcludedTimePeriodsPenalty;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NewActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NormalActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NormalRelativeActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.RelativeActivityPenalty;
import org.aimas.cami.scheduler.CAMIScheduler.swingui.SolverAndPersistenceFrame;

import com.thoughtworks.xstream.XStream;

public class SolutionUtils<Solution_> {

	public void addNewActivityFromXML(SolutionBusiness<Solution_> solutionBusiness, Object xmlActivity,
			SolverAndPersistenceFrame solverAndPersistenceFrame) {

		NewActivity newActivityDAO = null;

		XStream xStream = new XStream();
		xStream.alias("NewActivity", NewActivity.class);
		xStream.setMode(XStream.ID_REFERENCES);
		xStream.autodetectAnnotations(true);

		if (xmlActivity instanceof String) {
			newActivityDAO = (NewActivity) xStream.fromXML((String) xmlActivity);
		} else {
			newActivityDAO = (NewActivity) xStream.fromXML((Reader) xmlActivity);
		}

		Activity newActivity = newActivityDAO.getActivity();
		ExcludedTimePeriodsPenalty excludedTimePeriodsPenalty = newActivityDAO.getExcludedTimePeriodsPenalty();
		RelativeActivityPenalty relativeActivityPenalty = newActivityDAO.getRelativeActivityPenalty();

		ActivitySchedule activitySchedule = (ActivitySchedule) solutionBusiness.getSolution();

		if (newActivity instanceof NormalActivity) {

			solutionBusiness.doProblemFactChange(scoreDirector -> {

				List<Activity> activityList = new ArrayList<>(activitySchedule.getActivityList());
				activitySchedule.setActivityList(activityList);

				List<ActivityType> activityTypeList = new ArrayList<>(activitySchedule.getActivityTypeList());
				activitySchedule.setActivityTypeList(activityTypeList);

				// get activity instances
				int instances = 1;
				if (newActivity.getActivityType().getInstancesPerDay() != 0)
					instances = newActivity.getActivityType().getInstancesPerDay() * 7;
				else if (newActivity.getActivityType().getInstancesPerWeek() != 0)
					instances = newActivity.getActivityType().getInstancesPerWeek();

				// set activity type id
				newActivity.getActivityType().setId(activityTypeList.get(activityTypeList.size() - 1).getId() + 1);

				// create instances of this type of activity
				for (int i = 0; i < instances; i++) {
					NormalActivity activity = new NormalActivity();

					activity.setActivityType(newActivity.getActivityType());
					activity.setImmovable(newActivity.isImmovable());
					activity.setPeriodDomainRangeList(activitySchedule.getActivityPeriodList());
					activity.setId(activityList.get(activityList.size() - 1).getId() + 1);

					scoreDirector.beforeEntityAdded(activity);
					activityList.add(activity);
					scoreDirector.afterEntityAdded(activity);

					// if its period is imposed
					if (activity.getActivityType().getImposedPeriod() != null) {
						scoreDirector.beforeVariableChanged(activity, "activityPeriod");
						activity.setActivityPeriod(activity.getActivityType().getImposedPeriod());
						scoreDirector.afterVariableChanged(activity, "activityPeriod");

						activity.setImmovable(true);
					}
				}

				scoreDirector.beforeProblemFactAdded(newActivity.getActivityType());
				activityTypeList.add(newActivity.getActivityType());
				scoreDirector.afterProblemFactAdded(newActivity.getActivityType());

				scoreDirector.triggerVariableListeners();

			});
		} else if (newActivity instanceof NormalRelativeActivity) {

			if (relativeActivityPenalty != null) {
				solutionBusiness.doProblemFactChange(scoreDirector -> {
					List<Activity> activityList = new ArrayList<>(activitySchedule.getActivityList());
					activitySchedule.setActivityList(activityList);

					List<ActivityType> activityTypeList = new ArrayList<>(activitySchedule.getActivityTypeList());
					activitySchedule.setActivityTypeList(activityTypeList);

					List<RelativeActivityPenalty> relativeActivityPenaltyList = new ArrayList<>(
							activitySchedule.getRelativeActivityPenaltyList());
					activitySchedule.setRelativeActivityPenaltyList(relativeActivityPenaltyList);

					newActivity.getActivityType().setId(activityTypeList.get(activityTypeList.size() - 1).getId() + 1);

					// if the activity is relative to a specific activity
					if (relativeActivityPenalty.getNormalActivityType() != null) {

						int instances = 1;

						// get activity instances(activity to which newActivity is relative)
						for (ActivityType activityType : activityTypeList) {
							if (activityType.getCode().equals(relativeActivityPenalty.getNormalActivityType())) {

								if (activityType.getInstancesPerDay() != 0) {
									instances = activityType.getInstancesPerDay() * 7;
									newActivity.getActivityType().setInstancesPerDay(instances / 7);
								} else if (activityType.getInstancesPerWeek() != 0) {
									instances = activityType.getInstancesPerWeek();
									newActivity.getActivityType().setInstancesPerWeek(instances);
								}

								break;
							}
						}

						// create instances of this type of activity
						for (int i = 0; i < instances; i++) {
							NormalRelativeActivity relativeActivity = new NormalRelativeActivity();
							relativeActivity.setActivityType(newActivity.getActivityType());
							relativeActivity.setOffset(((NormalRelativeActivity) newActivity).getOffset()
									* Utility.getRelativeTypeSign(relativeActivityPenalty.getRelativeType()));
							relativeActivity.setImmovable(newActivity.isImmovable());
							relativeActivity.setId(activityList.get(activityList.size() - 1).getId() + 1);

							scoreDirector.beforeEntityAdded(relativeActivity);
							activityList.add(relativeActivity);
							scoreDirector.afterEntityAdded(relativeActivity);
						}

					} else if (relativeActivityPenalty.getCategory() != null) { // else if this activity is
																				// relative to a category of
																				// activities

						int instances = 1;

						// get the imposed instances
						if (newActivity.getActivityType().getInstancesPerDay() != 0) {
							instances = newActivity.getActivityType().getInstancesPerDay() * 7;
						} else if (newActivity.getActivityType().getInstancesPerWeek() != 0) {
							instances = newActivity.getActivityType().getInstancesPerWeek();
						}

						// create instances of this type of activity
						for (int i = 0; i < instances; i++) {
							NormalRelativeActivity relativeActivity = new NormalRelativeActivity();
							relativeActivity.setActivityType(newActivity.getActivityType());
							relativeActivity.setOffset(((NormalRelativeActivity) newActivity).getOffset()
									* Utility.getRelativeTypeSign(relativeActivityPenalty.getRelativeType()));
							relativeActivity.setImmovable(newActivity.isImmovable());
							relativeActivity.setId(activityList.get(activityList.size() - 1).getId() + 1);

							scoreDirector.beforeEntityAdded(relativeActivity);
							activityList.add(relativeActivity);
							scoreDirector.afterEntityAdded(relativeActivity);
						}

					}

					// add relativeActivityPenalty fact to the solution
					relativeActivityPenalty
							.setId(relativeActivityPenaltyList.get(relativeActivityPenaltyList.size() - 1).getId() + 1);

					scoreDirector.beforeProblemFactAdded(relativeActivityPenalty);
					relativeActivityPenaltyList.add(relativeActivityPenalty);
					scoreDirector.afterProblemFactAdded(relativeActivityPenalty);

					scoreDirector.beforeProblemFactAdded(newActivity.getActivityType());
					activityTypeList.add(newActivity.getActivityType());
					scoreDirector.afterProblemFactAdded(newActivity.getActivityType());

					// *****trigger the listener*****
					// so the relative activity new created has its period set
					if (relativeActivityPenalty.getNormalActivityType() != null) {

						triggerListener(solutionBusiness, activityList, relativeActivityPenalty.getNormalActivityType(),
								null);

					} else if ((relativeActivityPenalty.getCategory() != null)) {

						triggerListener(solutionBusiness, activityList, null, relativeActivityPenalty.getCategory());

					}

					scoreDirector.triggerVariableListeners();
				});
			}

		}

		// add excludedTimePeriodsPenalty to the solution
		if (excludedTimePeriodsPenalty != null) {

			List<ExcludedTimePeriodsPenalty> excludedTimePeriodsPenaltyList = new ArrayList<>(
					activitySchedule.getExcludedTimePeriodsList());
			activitySchedule.setExcludedTimePeriodsList(excludedTimePeriodsPenaltyList);

			solutionBusiness.doProblemFactChange(scoreDirector -> {
				excludedTimePeriodsPenalty.setId(
						excludedTimePeriodsPenaltyList.get(excludedTimePeriodsPenaltyList.size() - 1).getId() + 1);

				scoreDirector.beforeProblemFactAdded(excludedTimePeriodsPenalty);
				excludedTimePeriodsPenaltyList.add(excludedTimePeriodsPenalty);
				scoreDirector.afterProblemFactAdded(excludedTimePeriodsPenalty);

				scoreDirector.triggerVariableListeners();
			});

		}

		solverAndPersistenceFrame.startSolveAction();
	}

	/**
	 * Find all activities with their name equals to normalActivityType and reset
	 * their period
	 * 
	 */
	public void triggerListener(SolutionBusiness<Solution_> solutionBusiness, List<Activity> activityList,
			String normalActivityType, String category) {

		solutionBusiness.doProblemFactChange(scoreDirector -> {
			if (normalActivityType != null) {

				for (Activity activity : activityList) {
					if (activity instanceof NormalActivity) {
						if (((NormalActivity) activity).getActivityTypeCode().equals(normalActivityType)) {

							scoreDirector.beforeVariableChanged(activity, "activityPeriod");
							((NormalActivity) activity).setActivityPeriod(activity.getActivityPeriod());
							scoreDirector.afterVariableChanged(activity, "activityPeriod");
						}
					}
				}

			} else if ((category != null)) {

				for (Activity activity : activityList) {
					if (activity instanceof NormalActivity) {
						if (((NormalActivity) activity).getActivityCategory().getCode() != null
								&& ((NormalActivity) activity).getActivityCategory().getCode().equals(category)) {

							scoreDirector.beforeVariableChanged(activity, "activityPeriod");
							((NormalActivity) activity).setActivityPeriod(activity.getActivityPeriod());
							scoreDirector.afterVariableChanged(activity, "activityPeriod");
						}
					}
				}

			}

			scoreDirector.triggerVariableListeners();
		});
	}

	public List<String> getChangedActivites(List<Activity> beforeAddActivityList, List<Activity> afterAddActivityList) {

		List<String> changedActivities = new ArrayList<>();
		boolean foundSameActivity = false;

		for (Activity activity1 : beforeAddActivityList) {
			foundSameActivity = false;
			for (Activity activity2 : afterAddActivityList) {

				if (activity1.getActivityTypeCode() == activity2.getActivityTypeCode()) {

					// if we found an activity with the same name in the same period
					// even if it has different id(this is the case of many-instances activities)
					if (!Utility.compareActivityPeriods(activity1, activity2))
						foundSameActivity = true;

				}
			}

			if (!foundSameActivity)
				changedActivities.add(activity1.getActivityTypeCode()); // add the name of the activity
		}

		return changedActivities;
	}

}
