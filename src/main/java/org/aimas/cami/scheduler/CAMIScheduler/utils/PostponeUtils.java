package org.aimas.cami.scheduler.CAMIScheduler.utils;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Time;
import org.aimas.cami.scheduler.CAMIScheduler.domain.WeekDay;
import org.aimas.cami.scheduler.CAMIScheduler.marshal.PostponedActivity;
import org.aimas.cami.scheduler.CAMIScheduler.postpone.PostponeType;
import org.aimas.cami.scheduler.CAMIScheduler.solver.solver.ProblemSolver;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class PostponeUtils<Solution_> {

	private PostponedActivity postponedActivityDAO;

	// Or postpone multiple activities at the same time
	public void postponeActivity(SolutionBusiness<Solution_> solutionBusiness, Object jsonPostpone,
			ProblemSolver problemSolver) {

		ActivitySchedule activitySchedule = (ActivitySchedule) solutionBusiness.getSolution();

		XStream xStream = new XStream(new JettisonMappedXmlDriver());
		xStream.alias("PostponedActivity", PostponedActivity.class);
		xStream.setMode(XStream.NO_REFERENCES);
		xStream.autodetectAnnotations(true);

		if (jsonPostpone instanceof String) {
			postponedActivityDAO = (PostponedActivity) xStream.fromXML((String) jsonPostpone);
		} else {
			postponedActivityDAO = (PostponedActivity) xStream.fromXML((Reader) jsonPostpone);
		}

		solutionBusiness.doProblemFactChange(scoreDirector -> {
			postpone(scoreDirector, activitySchedule, postponedActivityDAO, problemSolver);
		});

		problemSolver.startSolveAction();

	}

	private void postpone(ScoreDirector<Solution_> scoreDirector, ActivitySchedule activitySchedule,
			PostponedActivity postponedActivity, ProblemSolver problemSolver) {

		Activity activity = findActivity(activitySchedule, postponedActivity.getName(), postponedActivity.getUuid());
		
		if (activity == null)
			return;

		Activity workingActivity = scoreDirector.lookUpWorkingObject(activity);
		PostponeType postponeType = postponedActivity.getPostponeType();

		if (workingActivity.getActivityPeriod() != null) {

			if (postponeType == PostponeType.POSTPONE_LATER_THIS_WEEK) {

				// *in real time rescheduling these will be updated relative to the current
				// period of the week*

				// the future activities have their value range changed
				setValueRangeForFutureActivities(scoreDirector, activitySchedule, workingActivity.getActivityPeriod());

				// the past activities have their value range changed
				setValueRangeForPastActivities(scoreDirector, activitySchedule, workingActivity.getActivityPeriod());

				setValueRangeForLaterThisWeekPostponedActivity(scoreDirector, activitySchedule, workingActivity);

				scoreDirector.beforeVariableChanged(workingActivity, "activityPeriod");
				workingActivity.setActivityPeriod(null);
				scoreDirector.afterVariableChanged(workingActivity, "activityPeriod");

			} else if (postponeType == PostponeType.POSTPONE_LATER_THIS_DAY) {

				setValueRangeForFutureActivities(scoreDirector, activitySchedule, workingActivity.getActivityPeriod());
				setValueRangeForPastActivities(scoreDirector, activitySchedule, workingActivity.getActivityPeriod());

				setValueRangeForLaterThisDayPostponedActivity(scoreDirector, activitySchedule, workingActivity);

				scoreDirector.beforeVariableChanged(workingActivity, "activityPeriod");
				workingActivity.setActivityPeriod(null);
				scoreDirector.afterVariableChanged(workingActivity, "activityPeriod");

			} else if (postponeType == PostponeType.POSTPONE_15MIN) {

				setValueRangeForFutureActivities(scoreDirector, activitySchedule, workingActivity.getActivityPeriod());
				setValueRangeForPastActivities(scoreDirector, activitySchedule, workingActivity.getActivityPeriod());

				ActivityPeriod postponedPeriod = AdjustActivityPeriod
						.getAdjustedPeriod(workingActivity.getActivityPeriod(), 15);

				scoreDirector.beforeProblemPropertyChanged(workingActivity);
				workingActivity.setPeriodDomainRangeList(new ArrayList<ActivityPeriod>(Arrays.asList(postponedPeriod)));
				scoreDirector.afterProblemPropertyChanged(workingActivity);

				scoreDirector.beforeVariableChanged(workingActivity, "activityPeriod");
				workingActivity.setActivityPeriod(postponedPeriod);
				scoreDirector.afterVariableChanged(workingActivity, "activityPeriod");

			} else if (postponeType == PostponeType.POSTPONE_30MIN) {

				setValueRangeForFutureActivities(scoreDirector, activitySchedule, workingActivity.getActivityPeriod());
				setValueRangeForPastActivities(scoreDirector, activitySchedule, workingActivity.getActivityPeriod());

				ActivityPeriod postponedPeriod = AdjustActivityPeriod
						.getAdjustedPeriod(workingActivity.getActivityPeriod(), 30);

				scoreDirector.beforeProblemPropertyChanged(workingActivity);
				workingActivity.setPeriodDomainRangeList(new ArrayList<ActivityPeriod>(Arrays.asList(postponedPeriod)));
				scoreDirector.afterProblemPropertyChanged(workingActivity);

				scoreDirector.beforeVariableChanged(workingActivity, "activityPeriod");
				workingActivity.setActivityPeriod(postponedPeriod);
				scoreDirector.afterVariableChanged(workingActivity, "activityPeriod");

			} else if (postponeType == PostponeType.POSTPONE_1HOUR) {

				setValueRangeForFutureActivities(scoreDirector, activitySchedule, workingActivity.getActivityPeriod());
				setValueRangeForPastActivities(scoreDirector, activitySchedule, workingActivity.getActivityPeriod());

				ActivityPeriod postponedPeriod = AdjustActivityPeriod
						.getAdjustedPeriod(workingActivity.getActivityPeriod(), 60);

				scoreDirector.beforeProblemPropertyChanged(workingActivity);
				workingActivity.setPeriodDomainRangeList(new ArrayList<ActivityPeriod>(Arrays.asList(postponedPeriod)));
				scoreDirector.afterProblemPropertyChanged(workingActivity);

				scoreDirector.beforeVariableChanged(workingActivity, "activityPeriod");
				workingActivity.setActivityPeriod(postponedPeriod);
				scoreDirector.afterVariableChanged(workingActivity, "activityPeriod");

			}

			scoreDirector.triggerVariableListeners();
			
			problemSolver.resetScreen();

		}

	}

	private void setValueRangeForPastActivities(ScoreDirector<Solution_> scoreDirector,
			ActivitySchedule activitySchedule, ActivityPeriod activityPeriod) {

		for (Activity activity : activitySchedule.getActivityList()) {
			if (activity.getActivityPeriod() != null) {
				if (activity.getActivityPeriodWeekday().getDayIndex() == activityPeriod.getWeekDayIndex()) {
					if (Utility.exclusiveBefore(activity.getActivityPeriod().getTime(), activityPeriod.getTime())) {

						scoreDirector.beforeProblemPropertyChanged(activity);
						activity.setPeriodDomainRangeList(
								new ArrayList<ActivityPeriod>(Arrays.asList(activity.getActivityPeriod())));
						scoreDirector.afterProblemPropertyChanged(activity);
					}
				} else if (activity.getActivityPeriodWeekday().getDayIndex() < activityPeriod.getWeekDayIndex()) {

					scoreDirector.beforeProblemPropertyChanged(activity);
					activity.setPeriodDomainRangeList(
							new ArrayList<ActivityPeriod>(Arrays.asList(activity.getActivityPeriod())));
					scoreDirector.afterProblemPropertyChanged(activity);
				}
			}
		}

	}

	private void setValueRangeForFutureActivities(ScoreDirector<Solution_> scoreDirector,
			ActivitySchedule activitySchedule, ActivityPeriod activityPeriod) {

		List<ActivityPeriod> restrictedPeriodDomain = getRestrictedPeriodDomainForFutureActivities(activitySchedule,
				activityPeriod);

		for (Activity activity : activitySchedule.getActivityList()) {
			if (activity.getActivityPeriod() != null) {
				if (activity.getActivityPeriodWeekday().getDayIndex() == activityPeriod.getWeekDayIndex()) {
					if (Utility.after(activityPeriod.getTime(), activity.getActivityPeriod().getTime())) {
						scoreDirector.beforeProblemPropertyChanged(activity);
						activity.setPeriodDomainRangeList(restrictedPeriodDomain);
						scoreDirector.afterProblemPropertyChanged(activity);
					}
				} else if (activity.getActivityPeriodWeekday().getDayIndex() > activityPeriod.getWeekDayIndex()) {
					scoreDirector.beforeProblemPropertyChanged(activity);
					activity.setPeriodDomainRangeList(restrictedPeriodDomain);
					scoreDirector.afterProblemPropertyChanged(activity);
				}
			}
		}

	}

	private List<ActivityPeriod> getRestrictedPeriodDomainForPastActivities(ActivitySchedule activitySchedule,
			ActivityPeriod activityPeriod) {

		List<ActivityPeriod> restrictedPeriodDomain = new ArrayList<>();

		for (ActivityPeriod period : activitySchedule.getActivityPeriodList()) {
			if (period.getWeekDayIndex() == activityPeriod.getWeekDayIndex()
					&& Utility.exclusiveBefore(period.getTime(), activityPeriod.getTime())) {
				restrictedPeriodDomain.add(period);
			} else if (period.getWeekDayIndex() < activityPeriod.getWeekDayIndex()) {
				restrictedPeriodDomain.add(period);
			}
		}
		return restrictedPeriodDomain;
	}

	private List<ActivityPeriod> getRestrictedPeriodDomainForFutureActivities(ActivitySchedule activitySchedule,
			ActivityPeriod activityPeriod) {

		List<ActivityPeriod> restrictedPeriodDomain = new ArrayList<>();

		for (ActivityPeriod period : activitySchedule.getActivityPeriodList()) {
			if (period.getWeekDayIndex() == activityPeriod.getWeekDayIndex()
					&& Utility.after(activityPeriod.getTime(), period.getTime())) {
				restrictedPeriodDomain.add(period);
			} else if (period.getWeekDayIndex() > activityPeriod.getWeekDayIndex()) {
				restrictedPeriodDomain.add(period);
			}
		}
		return restrictedPeriodDomain;
	}

	private List<ActivityPeriod> getRestrictedPeriodDomainForLaterThisDayPostpone(ActivitySchedule activitySchedule,
			ActivityPeriod activityPeriod) {

		List<ActivityPeriod> restrictedPeriodDomain = new ArrayList<>();

		for (ActivityPeriod period : activitySchedule.getActivityPeriodList()) {
			if (period.getWeekDayIndex() == activityPeriod.getWeekDayIndex()
					&& Utility.exclusiveAfter(activityPeriod.getTime(), period.getTime())) {
				restrictedPeriodDomain.add(period);
			}
		}
		return restrictedPeriodDomain;
	}

	private void setValueRangeForLaterThisDayPostponedActivity(ScoreDirector<Solution_> scoreDirector,
			ActivitySchedule activitySchedule, Activity workingActivity) {

		// restrict the value range for the postponed activity -> later this
		// day means
		// at least over 1 hour
		List<ActivityPeriod> restrictedPeriodDomain = getRestrictedPeriodDomainForLaterThisDayPostpone(activitySchedule,
				AdjustActivityPeriod.getAdjustedPeriod(workingActivity.getActivityPeriod(), 60));

		for (Activity activity : activitySchedule.getActivityList()) {
			if (activity.getActivityTypeCode() == workingActivity.getActivityTypeCode()
					&& activity.getActivityPeriod() != null
					&& activity.getActivityPeriodWeekday().getDayIndex() == workingActivity.getActivityPeriodWeekday()
							.getDayIndex()
					&& Utility.after(workingActivity.getActivityPeriodTime(), activity.getActivityPeriodTime())) {

				scoreDirector.beforeProblemPropertyChanged(activity);
				activity.setPeriodDomainRangeList(restrictedPeriodDomain.size() != 0 ? restrictedPeriodDomain
						: new ArrayList<ActivityPeriod>(Arrays.asList(activity.getActivityPeriod())));
				scoreDirector.afterProblemPropertyChanged(activity);

			}
		}

	}

	private void setValueRangeForLaterThisWeekPostponedActivity(ScoreDirector<Solution_> scoreDirector,
			ActivitySchedule activitySchedule, Activity workingActivity) {

		List<ActivityPeriod> futureRestrictedPeriodDomain = getRestrictedPeriodDomainForFutureActivities(
				activitySchedule, new ActivityPeriod(new Time(0, 0),
						new WeekDay(workingActivity.getActivityPeriodWeekday().getDayIndex() + 1)));

		for (Activity activity : activitySchedule.getActivityList()) {
			if (activity.getActivityTypeCode() == workingActivity.getActivityTypeCode()
					&& activity.getActivityPeriod() != null) {

				if (activity.getActivityPeriodWeekday().getDayIndex() >= workingActivity.getActivityPeriodWeekday()
						.getDayIndex()) {

					scoreDirector.beforeProblemPropertyChanged(activity);
					activity.setPeriodDomainRangeList(
							futureRestrictedPeriodDomain.size() != 0 ? futureRestrictedPeriodDomain
									: new ArrayList<ActivityPeriod>(Arrays.asList(activity.getActivityPeriod())));
					scoreDirector.afterProblemPropertyChanged(activity);

				}

			}
		}

	}

	private Activity findActivity(ActivitySchedule activitySchedule, String name, String uuid) {

		for (Activity activity : activitySchedule.getActivityList()) {
			if (activity.getActivityTypeCode().equals(postponedActivityDAO.getName())
					&& activity.getUuid().equals(postponedActivityDAO.getUuid()))
				return activity;
		}

		return null;

	}

}
