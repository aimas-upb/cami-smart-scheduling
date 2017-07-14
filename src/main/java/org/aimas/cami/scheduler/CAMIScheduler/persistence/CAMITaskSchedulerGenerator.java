package org.aimas.cami.scheduler.CAMIScheduler.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityCategory;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityDomain;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityType;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ExcludedTimePeriodsPenalty;
import org.aimas.cami.scheduler.CAMIScheduler.domain.PeriodInterval;
import org.aimas.cami.scheduler.CAMIScheduler.domain.TimeInterval;
import org.aimas.cami.scheduler.CAMIScheduler.domain.RelativeActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.RelativeActivityPenalty;
import org.aimas.cami.scheduler.CAMIScheduler.domain.RelativeType;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Time;
import org.aimas.cami.scheduler.CAMIScheduler.domain.WeekDay;
import org.aimas.cami.scheduler.CAMIScheduler.domain.WeekDays;
import org.aimas.cami.scheduler.CAMIScheduler.utils.LoggingMain;
import org.aimas.cami.scheduler.CAMIScheduler.utils.SolutionDao;

/**
 * 
 * @author Bogdan
 *
 */
public class CAMITaskSchedulerGenerator extends LoggingMain {

	private static final int DAY_LIST_SIZE = 7;
	private static final int TIME_LIST_SIZE = 17;
	private static final Time[] TIMES = new Time[TIME_LIST_SIZE];

	public static void main(String[] args) {
		CAMITaskSchedulerGenerator camiTSG = new CAMITaskSchedulerGenerator();
		camiTSG.writeActivitySchedule();
	}

	protected final SolutionDao solutionDao;
	protected final File outputDir;

	public CAMITaskSchedulerGenerator() {
		solutionDao = new CAMITaskSchedulerDao();
		outputDir = new File(solutionDao.getDataDir(), "unsolved");
	}

	private void writeActivitySchedule() {
		String filename = "simple-activity";
		File outputFile = new File(outputDir, filename + ".xml");
		logger.info("Create an Activity Schedule");
		ActivitySchedule activitySchedule = createActivitySchedule();
		solutionDao.writeSolution(activitySchedule, outputFile);
	}

	private ActivitySchedule createActivitySchedule() {
		ActivitySchedule activitySchedule = new ActivitySchedule();
		activitySchedule.setId(0L);

		int domainListSize = 1;
		int categoryPerDomainSize = 1;

		createActivityDomainList(activitySchedule, domainListSize, categoryPerDomainSize);
		createActivityCategoryList(activitySchedule, domainListSize, categoryPerDomainSize);
		createActivityTypeList(activitySchedule);
		createActivityList(activitySchedule);
		createTimeList(activitySchedule);
		createExcludedTimeslotsList(activitySchedule);
		createWeekDayList(activitySchedule);
		createActivityPeriodList(activitySchedule);
		createRelativeActivityPenaltyList(activitySchedule);

		return activitySchedule;
	}

	private void createActivityDomainList(ActivitySchedule activitySchedule, int domainListSize,
			int categoriesPerDomainSize) {
		List<ActivityDomain> activityDomainList = new ArrayList<>(1);

		ActivityDomain activityDomain = new ActivityDomain();
		activityDomain.setCode("Health Related Activities");
		activityDomain.setId(0L);

		List<ActivityCategory> activityCategoryList = new ArrayList<>(categoriesPerDomainSize);
		ActivityCategory activityCategory = new ActivityCategory();

		activityCategory.setCode("Indoor physical exercies");
		activityCategory.setDomain(activityDomain);
		activityCategoryList.add(activityCategory);

		activityDomain.setCategories(activityCategoryList);

		activityDomainList.add(activityDomain);

		activitySchedule.setActivityDomainList(activityDomainList);
	}

	private void createActivityCategoryList(ActivitySchedule activitySchedule, int domainListSize,
			int categoriesPerDomainSize) {
		List<ActivityCategory> activityCategoryList = new ArrayList<>();

		for (int i = 0; i < domainListSize; i++) {
			for (int j = 0; j < categoriesPerDomainSize; j++) {
				activitySchedule.getActivityDomainList().get(i).getCategories().get(j).setId(0L);
				activityCategoryList.add(activitySchedule.getActivityDomainList().get(i).getCategories().get(j));
			}

		}

		activitySchedule.setActivityCategoryList(activityCategoryList);
	}

	private void createActivityList(ActivitySchedule activitySchedule) {
		List<Activity> activityList = new ArrayList<>();
		long id = 0L;
		long typeId = 0L;

		ActivityType activityType = new ActivityType();

		activityType.setActivityCategory(activitySchedule.getActivityCategoryList().get(0));
		activityType.setCode("Yoga");
		activityType.setDuration(30);
		activityType.setId(typeId++);
		activityType.setInstancesPerWeek(3);

		TimeInterval permittedInterval = new TimeInterval(new Time(8, 0), new Time(10, 0));

		activityType.setPermittedInterval(permittedInterval);

		activitySchedule.getActivityTypeList().add(activityType);

		for (int i = 0; i < 3; i++) {

			/*
			 * ActivityType activityType1 = new ActivityType();
			 * 
			 * activityType1.setActivityCategory(activitySchedule.
			 * getActivityCategoryList().get(0));
			 * activityType1.setCode("Stair Jumping");
			 * activityType1.setDuration(25); activityType1.setId(typeId++);
			 * activityType1.setInstancesPerWeek(1);
			 * 
			 * RelativeActivity relativeActivity = new RelativeActivity();
			 * relativeActivity.setActivityType(activityType1);
			 * relativeActivity.setOffset(15); relativeActivity.setId(0L);
			 * 
			 * activitySchedule.getActivityTypeList().add(activityType1);
			 */

			Activity activity = new Activity();

			activity.setActivityType(activityType);
			activity.setId(id++);
			activity.setImmovable(false);
			// activity.setRelativeActivity(relativeActivity);

			activityList.add(activity);
			// activityList.add(relativeActivity);
		}

		ActivityType activityType1 = new ActivityType();

		activityType1.setActivityCategory(activitySchedule.getActivityCategoryList().get(0));
		activityType1.setCode("CoD VR");
		activityType1.setDuration(30);
		activityType1.setId(typeId++);
		activityType1.setInstancesPerWeek(1);
		// activityType1.setImposedTime(new Time(23, 0));

		activitySchedule.getActivityTypeList().add(activityType1);

		Activity activity1 = new Activity();

		activity1.setActivityType(activityType1);
		activity1.setId(id++);
		activity1.setImmovable(false);
		// activity.setRelativeActivity(relativeActivity);

		activityList.add(activity1);

		String[] ex = { "Chess", "FIFA Kinect", "Wing Chun", "Ninjutsu", "Aikido", "Kenpo" };

		for (int i = 0; i < 6; i++) {
			ActivityType activityType2 = new ActivityType();

			activityType2.setActivityCategory(activitySchedule.getActivityCategoryList().get(0));
			activityType2.setCode(ex[i]);
			activityType2.setDuration(30);
			activityType2.setId(typeId++);
			activityType2.setInstancesPerWeek(1);
			activityType2.setPermittedInterval(new TimeInterval(new Time(8, 0), new Time(9, 0)));

			activitySchedule.getActivityTypeList().add(activityType2);
			
			Activity activity = new Activity();

			activity.setActivityType(activityType2);
			activity.setId(id++);
			activity.setImmovable(false);
			
			activityList.add(activity);
		}

		activitySchedule.setActivityList(activityList);
	}

	private void createActivityTypeList(ActivitySchedule activitySchedule) {
		List<ActivityType> activityTypeList = new ArrayList<>();
		long id = 0L;

		/*
		 * for (Activity activity : activitySchedule.getActivityList()) {
		 * activityTypeList.add(activity.getActivityType()); }
		 */

		activitySchedule.setActivityTypeList(activityTypeList);
	}

	private void createActivityPeriodList(ActivitySchedule activitySchedule) {
		List<ActivityPeriod> activityPeriodList = new ArrayList<>();
		long id = 0L;

		for (WeekDay weekDay : activitySchedule.getWeekdayList()) {
			for (Time time : activitySchedule.getTimeList()) {
				ActivityPeriod activityPeriod = new ActivityPeriod(time, weekDay);
				activityPeriod.setId(id++);
				activityPeriodList.add(activityPeriod);
			}
		}

		activitySchedule.setActivityPeriodList(activityPeriodList);
	}

	private void createTimeList(ActivitySchedule activitySchedule) {
		List<Time> timeList = new ArrayList<>(TIME_LIST_SIZE);
		fillTimes();

		for (int i = 0; i < TIME_LIST_SIZE; i++) {

			Time time = TIMES[i];
			time.setId((long) i);
			timeList.add(time);
		}

		activitySchedule.setTimeList(timeList);
	}

	private void createExcludedTimeslotsList(ActivitySchedule activitySchedule) {
		List<ExcludedTimePeriodsPenalty> excludedTimePeriodsPenaltyList = new ArrayList<>();

		ExcludedTimePeriodsPenalty etpp = new ExcludedTimePeriodsPenalty();

		etpp.setActivityType(activitySchedule.getActivityTypeList().get(1));
		etpp.setId(0L);
		PeriodInterval pi = new PeriodInterval();
		WeekDay wd = new WeekDay();
		wd.setDayIndex(0);
		pi.setStartPeriod(new ActivityPeriod(new Time(15, 0), wd));
		pi.setEndPeriod(new ActivityPeriod(new Time(18, 0), wd));
		List<PeriodInterval> excludedActivityPeriods = new ArrayList<>();

		excludedActivityPeriods.add(pi);
		etpp.setExcludedActivityPeriods(excludedActivityPeriods);

		excludedTimePeriodsPenaltyList.add(etpp);

		activitySchedule.setExcludedTimePeriodsList(excludedTimePeriodsPenaltyList);
	}

	private void createRelativeActivityPenaltyList(ActivitySchedule activitySchedule) {
		List<RelativeActivityPenalty> relativeActivityPenaltyList = new ArrayList<>();

		/*
		 * RelativeActivityPenalty relativeActivityPenalty = new
		 * RelativeActivityPenalty();
		 * relativeActivityPenalty.setRelativeType(RelativeType.AFTER); if
		 * (activitySchedule.getActivityTypeList().get(0).getCode().
		 * equals("Stair Jumping")) {
		 * relativeActivityPenalty.setFirstActivityType(activitySchedule.
		 * getActivityTypeList().get(0));
		 * relativeActivityPenalty.setSecondActivityType(activitySchedule.
		 * getActivityTypeList().get(1)); } else {
		 * relativeActivityPenalty.setFirstActivityType(activitySchedule.
		 * getActivityTypeList().get(1));
		 * relativeActivityPenalty.setSecondActivityType(activitySchedule.
		 * getActivityTypeList().get(0)); }
		 * 
		 * relativeActivityPenalty.setId(0L);
		 * 
		 * relativeActivityPenaltyList.add(relativeActivityPenalty);
		 */
		activitySchedule.setRelativeActivityPenaltyList(relativeActivityPenaltyList);
	}

	private void createWeekDayList(ActivitySchedule activitySchedule) {
		List<WeekDay> weekDayList = new ArrayList<>(DAY_LIST_SIZE);

		for (int i = 0; i < DAY_LIST_SIZE; i++) {
			WeekDay weekDay = new WeekDay();
			weekDay.setId((long) i);
			weekDay.setDayIndex(i);
			weekDayList.add(weekDay);
		}

		activitySchedule.setWeekdayList(weekDayList);
	}

	private void fillTimes() {
		for (int i = 0; i < TIME_LIST_SIZE; i++) {
			TIMES[i] = new Time(8 + i, 0);
		}
	}

}