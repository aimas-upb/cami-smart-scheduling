package org.aimas.cami.scheduler.CAMIScheduler.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityCategory;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityDomain;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityType;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Difficulty;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ExcludedTimePeriodsPenalty;
import org.aimas.cami.scheduler.CAMIScheduler.domain.PeriodInterval;
import org.aimas.cami.scheduler.CAMIScheduler.domain.RelativeActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.RelativeActivityPenalty;
import org.aimas.cami.scheduler.CAMIScheduler.domain.RelativeType;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Time;
import org.aimas.cami.scheduler.CAMIScheduler.domain.TimeInterval;
import org.aimas.cami.scheduler.CAMIScheduler.domain.WeekDay;
import org.aimas.cami.scheduler.CAMIScheduler.utils.LoggingMain;
import org.aimas.cami.scheduler.CAMIScheduler.utils.SolutionDao;

/**
 * 
 * @author Bogdan
 *
 */
public class CAMITaskSchedulerGenerator extends LoggingMain {

	private static final int DAY_LIST_SIZE = 7;
	private static final int TIME_LIST_SIZE = 24;
	private static final Time[] TIMES = new Time[TIME_LIST_SIZE];
	private Map<String, Activity> staticActivities = new HashMap<>();

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

		createActivityDomainList(activitySchedule);
		createActivityList(activitySchedule);
		createTimeList(activitySchedule);
		createWeekDayList(activitySchedule);
		createActivityPeriodList(activitySchedule);
		setImposedActivities(activitySchedule);

		return activitySchedule;
	}

	private void createActivityDomainList(ActivitySchedule activitySchedule) {
		List<ActivityDomain> activityDomainList = new ArrayList<>();
		long id = 0L;

		ActivityDomain activityDomain1 = new ActivityDomain("Health Related Activities");
		ActivityDomain activityDomain2 = new ActivityDomain("Leisure activities");
		activityDomain1.setId(id++);
		activityDomain2.setId(id++);

		List<ActivityCategory> activityCategoryList1 = new ArrayList<>();
		List<ActivityCategory> activityCategoryList2 = new ArrayList<>();
		List<ActivityCategory> activityCategoryListAll = new ArrayList<>();

		activityCategoryList1.add(new ActivityCategory("Indoor physical exercies", activityDomain1, 0L));
		activityCategoryList1.add(new ActivityCategory("Outdoor physical exercies", activityDomain1, 1L));
		activityCategoryList1.add(new ActivityCategory("Imposed/Suggested Health measurements", activityDomain1, 2L));
		activityCategoryList1.add(new ActivityCategory("Medication intake", activityDomain1, 3L));
		activityCategoryList2.add(new ActivityCategory("Leisure activities", activityDomain2, 4L));

		activityCategoryListAll.addAll(activityCategoryList1);
		activityCategoryListAll.addAll(activityCategoryList2);

		activityDomain1.setCategories(activityCategoryList1);

		activityDomainList.add(activityDomain1);

		activityDomain2.setCategories(activityCategoryList2);

		activityDomainList.add(activityDomain2);

		activitySchedule.setActivityCategoryList(activityCategoryListAll);

		activitySchedule.setActivityDomainList(activityDomainList);
	}

	private void createActivityList(ActivitySchedule activitySchedule) {
		List<Activity> activityList = new ArrayList<>();
		long id = 0L;
		long typeId = 0L;
		long exccludedId = 0L;
		long relativeId = 0L;
		long relativeActivityPenaltyId = 0L;

		List<ExcludedTimePeriodsPenalty> excludedTimePeriodsPenaltyList = new ArrayList<>();
		List<RelativeActivityPenalty> relativeActivityPenaltyList = new ArrayList<>();
		List<ActivityType> activityTypeList = new ArrayList<>();
		activitySchedule.setActivityTypeList(activityTypeList);

		ActivityType breakfast = new ActivityType();
		breakfast.setCode("Breakfast");
		breakfast.setDuration(30);
		breakfast.setId(typeId++);
		breakfast.setInstancesPerDay(1);
		List<TimeInterval> permittedIntervalListBreakfast = new ArrayList<>();
		permittedIntervalListBreakfast.add(new TimeInterval(new Time(7, 0), new Time(10, 0)));
		breakfast.setPermittedInterval(permittedIntervalListBreakfast);

		activitySchedule.getActivityTypeList().add(breakfast);

		for (int i = 0; i < breakfast.getInstancesPerDay() * 7; i++) {
			Activity activity = new Activity();
			activity.setActivityType(breakfast);
			activity.setId(id++);
			activity.setImmovable(false);
			activity.setIndex(i);
			activityList.add(activity);
		}

		ActivityType lunch = new ActivityType();
		lunch.setCode("Lunch");
		lunch.setDuration(30);
		lunch.setId(typeId++);
		lunch.setInstancesPerDay(1);
		List<TimeInterval> permittedIntervalListLunch = new ArrayList<>();
		permittedIntervalListLunch.add(new TimeInterval(new Time(12, 0), new Time(15, 0)));
		lunch.setPermittedInterval(permittedIntervalListLunch);

		activitySchedule.getActivityTypeList().add(lunch);

		for (int i = 0; i < lunch.getInstancesPerDay() * 7; i++) {
			Activity activity = new Activity();
			activity.setActivityType(lunch);
			activity.setId(id++);
			activity.setImmovable(false);
			activity.setIndex(i);
			activityList.add(activity);
		}

		ActivityType dinner = new ActivityType();
		dinner.setCode("Dinner");
		dinner.setDuration(30);
		dinner.setId(typeId++);
		dinner.setInstancesPerDay(1);
		List<TimeInterval> permittedIntervalListDinner = new ArrayList<>();
		permittedIntervalListDinner.add(new TimeInterval(new Time(19, 0), new Time(22, 0)));
		dinner.setPermittedInterval(permittedIntervalListDinner);

		activitySchedule.getActivityTypeList().add(dinner);

		for (int i = 0; i < dinner.getInstancesPerDay() * 7; i++) {
			Activity activity = new Activity();
			activity.setActivityType(dinner);
			activity.setId(id++);
			activity.setImmovable(false);
			activity.setIndex(i);
			activityList.add(activity);
		}

		{
			ActivityType stretches = new ActivityType();
			stretches.setActivityCategory(activitySchedule.getActivityCategoryList().get(0));
			stretches.setCode("Stretches");
			stretches.setDifficulty(Difficulty.MEDIUM);
			stretches.setCalories(100);
			stretches.setDuration(10);
			stretches.setId(typeId++);
			stretches.setInstancesPerWeek(3);

			List<TimeInterval> permittedIntervalList = new ArrayList<>();
			permittedIntervalList.add(new TimeInterval(new Time(8, 0), new Time(10, 0)));
			permittedIntervalList.add(new TimeInterval(new Time(18, 0), new Time(20, 0)));
			stretches.setPermittedInterval(permittedIntervalList);
			activitySchedule.getActivityTypeList().add(stretches);

			for (int i = 0; i < stretches.getInstancesPerWeek(); i++) {
				Activity activity = new Activity();
				activity.setActivityType(stretches);
				activity.setId(id++);
				activity.setImmovable(false);
				activityList.add(activity);
			}

			ExcludedTimePeriodsPenalty etpp = new ExcludedTimePeriodsPenalty();
			etpp.setActivityType(stretches);

			etpp.setId(exccludedId++);
			PeriodInterval pi = new PeriodInterval();
			WeekDay wd = new WeekDay(6);
			pi.setStartPeriod(new ActivityPeriod(new Time(0, 0), wd));
			pi.setEndPeriod(new ActivityPeriod(new Time(24, 0), wd));
			List<PeriodInterval> excludedActivityPeriods = new ArrayList<>();

			excludedActivityPeriods.add(pi);
			etpp.setExcludedActivityPeriods(excludedActivityPeriods);

			excludedTimePeriodsPenaltyList.add(etpp);
		}

		// ---------------------------------------------------//

		{
			ActivityType armRotations = new ActivityType();
			armRotations.setActivityCategory(activitySchedule.getActivityCategoryList().get(0));
			armRotations.setCode("Arm rotations");
			armRotations.setDifficulty(Difficulty.MEDIUM);
			armRotations.setCalories(50);
			armRotations.setDuration(5);
			armRotations.setId(typeId++);
			armRotations.setInstancesPerWeek(3);

			activitySchedule.getActivityTypeList().add(armRotations);

			for (int i = 0; i < armRotations.getInstancesPerWeek(); i++) {
				Activity activity = new Activity();
				activity.setActivityType(armRotations);
				activity.setId(id++);
				activity.setImmovable(false);
				activityList.add(activity);
			}

			ExcludedTimePeriodsPenalty etpp = new ExcludedTimePeriodsPenalty();
			etpp.setActivityType(armRotations);

			etpp.setId(exccludedId++);
			PeriodInterval pi = new PeriodInterval();
			WeekDay wd = new WeekDay(6);
			pi.setStartPeriod(new ActivityPeriod(new Time(0, 0), wd));
			pi.setEndPeriod(new ActivityPeriod(new Time(24, 0), wd));
			List<PeriodInterval> excludedActivityPeriods = new ArrayList<>();

			excludedActivityPeriods.add(pi);
			etpp.setExcludedActivityPeriods(excludedActivityPeriods);

			excludedTimePeriodsPenaltyList.add(etpp);
		}

		// ---------------------------------------------------//

		{
			ActivityType bodyWorkout = new ActivityType();
			bodyWorkout.setActivityCategory(activitySchedule.getActivityCategoryList().get(0));
			bodyWorkout.setCode("Full body workout");
			bodyWorkout.setDifficulty(Difficulty.HARD);
			bodyWorkout.setCalories(300);
			bodyWorkout.setDuration(15);
			bodyWorkout.setId(typeId++);
			bodyWorkout.setInstancesPerWeek(2);

			activitySchedule.getActivityTypeList().add(bodyWorkout);

			for (int i = 0; i < bodyWorkout.getInstancesPerWeek(); i++) {
				Activity activity = new Activity();
				activity.setActivityType(bodyWorkout);
				activity.setId(id++);
				activity.setImmovable(false);
				activityList.add(activity);
			}

			ExcludedTimePeriodsPenalty etpp = new ExcludedTimePeriodsPenalty();
			etpp.setActivityType(bodyWorkout);

			etpp.setId(exccludedId++);
			PeriodInterval pi1 = new PeriodInterval();
			pi1.setStartPeriod(new ActivityPeriod(new Time(0, 0), new WeekDay(5)));
			pi1.setEndPeriod(new ActivityPeriod(new Time(24, 0), new WeekDay(5)));

			PeriodInterval pi2 = new PeriodInterval();
			pi2.setStartPeriod(new ActivityPeriod(new Time(0, 0), new WeekDay(6)));
			pi2.setEndPeriod(new ActivityPeriod(new Time(24, 0), new WeekDay(6)));

			List<PeriodInterval> excludedActivityPeriods = new ArrayList<>();

			excludedActivityPeriods.add(pi1);
			excludedActivityPeriods.add(pi2);
			etpp.setExcludedActivityPeriods(excludedActivityPeriods);

			excludedTimePeriodsPenaltyList.add(etpp);
		}

		// ---------------------------------------------------//

		{
			ActivityType walkInPark1 = new ActivityType();
			walkInPark1.setActivityCategory(activitySchedule.getActivityCategoryList().get(1));
			walkInPark1.setCode("Walk in park");
			walkInPark1.setCalories(100);
			walkInPark1.setDuration(120);
			walkInPark1.setImposedPeriod(new ActivityPeriod(new Time(11, 0), new WeekDay(1)));
			walkInPark1.setId(typeId++);

			activitySchedule.getActivityTypeList().add(walkInPark1);

			Activity activity = new Activity();
			activity.setActivityType(walkInPark1);
			activity.setId(id++);
			activity.setImmovable(true);
			activityList.add(activity);
		}

		{
			ActivityType walkInPark2 = new ActivityType();
			walkInPark2.setActivityCategory(activitySchedule.getActivityCategoryList().get(1));
			walkInPark2.setCode("Walk in park");
			walkInPark2.setCalories(100);
			walkInPark2.setDuration(120);
			walkInPark2.setImposedPeriod(new ActivityPeriod(new Time(17, 0), new WeekDay(3)));
			walkInPark2.setId(typeId++);

			activitySchedule.getActivityTypeList().add(walkInPark2);

			Activity activity = new Activity();
			activity.setActivityType(walkInPark2);
			activity.setId(id++);
			activity.setImmovable(true);
			activityList.add(activity);
		}

		{
			ActivityType walkInPark3 = new ActivityType();
			walkInPark3.setActivityCategory(activitySchedule.getActivityCategoryList().get(1));
			walkInPark3.setCode("Walk in park");
			walkInPark3.setCalories(100);
			walkInPark3.setDuration(120);
			walkInPark3.setImposedPeriod(new ActivityPeriod(new Time(11, 0), new WeekDay(4)));
			walkInPark3.setId(typeId++);

			activitySchedule.getActivityTypeList().add(walkInPark3);

			Activity activity = new Activity();
			activity.setActivityType(walkInPark3);
			activity.setId(id++);
			activity.setImmovable(true);
			activityList.add(activity);
		}

		{
			ActivityType bikeRide = new ActivityType();
			bikeRide.setActivityCategory(activitySchedule.getActivityCategoryList().get(1));
			bikeRide.setCode("Bike ride");
			bikeRide.setCalories(250);
			bikeRide.setDuration(180);
			bikeRide.setImposedPeriod(new ActivityPeriod(new Time(11, 0), new WeekDay(5)));
			bikeRide.setId(typeId++);

			activitySchedule.getActivityTypeList().add(bikeRide);

			Activity activity = new Activity();
			activity.setActivityType(bikeRide);
			activity.setId(id++);
			activity.setImmovable(true);
			activityList.add(activity);
		}

		// ---------------------------------------------------//

		{
			ActivityType weightMeasurement = new ActivityType();
			weightMeasurement.setActivityCategory(activitySchedule.getActivityCategoryList().get(2));
			weightMeasurement.setCode("Weight measurement");
			weightMeasurement.setDuration(5);
			weightMeasurement.setId(typeId++);
			weightMeasurement.setInstancesPerDay(1);
			List<TimeInterval> permittedIntervalList = new ArrayList<>();
			permittedIntervalList.add(new TimeInterval(new Time(7, 0), new Time(10, 0)));
			weightMeasurement.setPermittedInterval(permittedIntervalList);

			activitySchedule.getActivityTypeList().add(weightMeasurement);

			// pot sa fac un select pentru activitatea statica care ma intereseaza for
			for (int i = 0; i < weightMeasurement.getInstancesPerDay() * 7; i++) {
				RelativeActivity relativeActivity = new RelativeActivity();
				relativeActivity.setActivityType(weightMeasurement);
				relativeActivity.setOffset(-15);
				relativeActivity.setId(relativeId++);
				relativeActivity.setIndex(i);

				activityList.add(relativeActivity);

				RelativeActivityPenalty relativeActivityPenalty = new RelativeActivityPenalty();
				relativeActivityPenalty.setRelativeType(RelativeType.BEFORE);
				relativeActivityPenalty.setFirstActivityType(weightMeasurement);
				relativeActivityPenalty.setSecondActivityType(breakfast);
				relativeActivityPenalty.setId(relativeActivityPenaltyId++);
				relativeActivityPenaltyList.add(relativeActivityPenalty);
			}
		}

		// ---------------------------------------------------//

		{
			ActivityType bloodPressurehMeasurement = new ActivityType();
			bloodPressurehMeasurement.setActivityCategory(activitySchedule.getActivityCategoryList().get(2));
			bloodPressurehMeasurement.setCode("Blood pressure measurement");
			bloodPressurehMeasurement.setDuration(3);
			bloodPressurehMeasurement.setId(typeId++);
			bloodPressurehMeasurement.setInstancesPerDay(1);

			activitySchedule.getActivityTypeList().add(bloodPressurehMeasurement);

			for (int i = 0; i < bloodPressurehMeasurement.getInstancesPerDay() * 7; i++) {
				Activity activity = new Activity();
				activity.setActivityType(bloodPressurehMeasurement);
				activity.setId(id++);
				activity.setImmovable(false);
				activityList.add(activity);
			}
		}

		// ---------------------------------------------------//

		{
			ActivityType heartMedication = new ActivityType();
			heartMedication.setActivityCategory(activitySchedule.getActivityCategoryList().get(3));
			heartMedication.setCode("Heart medication");
			heartMedication.setDuration(10);
			heartMedication.setId(typeId++);
			heartMedication.setInstancesPerDay(1);
			List<TimeInterval> permittedIntervalList = new ArrayList<>();
			permittedIntervalList.add(new TimeInterval(new Time(7, 0), new Time(10, 0)));
			heartMedication.setPermittedInterval(permittedIntervalList);

			activitySchedule.getActivityTypeList().add(heartMedication);

			for (int i = 0; i < heartMedication.getInstancesPerDay() * 7; i++) {
				RelativeActivity relativeActivity = new RelativeActivity();
				relativeActivity.setActivityType(heartMedication);
				relativeActivity.setOffset(15);
				relativeActivity.setId(relativeId++);
				relativeActivity.setIndex(i);

				activityList.add(relativeActivity);

				RelativeActivityPenalty relativeActivityPenalty = new RelativeActivityPenalty();
				relativeActivityPenalty.setRelativeType(RelativeType.AFTER);
				relativeActivityPenalty.setFirstActivityType(heartMedication);
				relativeActivityPenalty.setSecondActivityType(breakfast);
				relativeActivityPenalty.setId(relativeActivityPenaltyId++);
				relativeActivityPenaltyList.add(relativeActivityPenalty);
			}
		}

		// ---------------------------------------------------//

		{
			ActivityType antibiotic = new ActivityType();
			antibiotic.setActivityCategory(activitySchedule.getActivityCategoryList().get(3));
			antibiotic.setCode("Antibiotic");
			antibiotic.setDuration(2);
			antibiotic.setId(typeId++);
			antibiotic.setInstancesPerDay(2);

			activitySchedule.getActivityTypeList().add(antibiotic);

			for (int i = 0; i < antibiotic.getInstancesPerDay() * 7; i++) {
				RelativeActivity relativeActivity = new RelativeActivity();
				relativeActivity.setActivityType(antibiotic);
				relativeActivity.setOffset(15);
				relativeActivity.setId(relativeId++);
				relativeActivity.setIndex(i);

				activityList.add(relativeActivity);
			}

			ExcludedTimePeriodsPenalty etpp = new ExcludedTimePeriodsPenalty();
			etpp.setActivityType(antibiotic);

			etpp.setId(exccludedId++);
			PeriodInterval pi1 = new PeriodInterval();
			pi1.setStartPeriod(new ActivityPeriod(new Time(0, 0), null));
			pi1.setEndPeriod(new ActivityPeriod(new Time(6, 0), null));

			PeriodInterval pi2 = new PeriodInterval();
			pi2.setStartPeriod(new ActivityPeriod(new Time(22, 0), null));
			pi2.setEndPeriod(new ActivityPeriod(new Time(23, 59), null));

			List<PeriodInterval> excludedActivityPeriods = new ArrayList<>();

			excludedActivityPeriods.add(pi1);
			excludedActivityPeriods.add(pi2);
			etpp.setExcludedActivityPeriods(excludedActivityPeriods);

			excludedTimePeriodsPenaltyList.add(etpp);
		}

		// ---------------------------------------------------//
		{
			ActivityType tvSeries = new ActivityType();
			tvSeries.setCode("Favorite TV Series 1");
			tvSeries.setDuration(60);
			tvSeries.setId(typeId++);
			tvSeries.setImposedPeriod(new ActivityPeriod(new Time(20, 0), new WeekDay(3)));

			activitySchedule.getActivityTypeList().add(tvSeries);

			Activity activity = new Activity();
			activity.setActivityType(tvSeries);
			activity.setId(id++);
			activity.setImmovable(true);
			activityList.add(activity);
		}

		// ---------------------------------------------------//

		{

			for (int i = 0; i < 7; i++) {
				ActivityType tvSeries = new ActivityType();
				tvSeries.setCode("Favorite TV Series 2");
				tvSeries.setDuration(60);
				tvSeries.setId(typeId++);
				tvSeries.setImposedPeriod(new ActivityPeriod(new Time(16, 0), new WeekDay(i)));

				activitySchedule.getActivityTypeList().add(tvSeries);
				Activity activity = new Activity();
				activity.setActivityType(tvSeries);
				activity.setId(id++);
				activity.setImmovable(true);
				activityList.add(activity);
			}
		}

		// ---------------------------------------------------//

		{
			ActivityType hairCuttingAppointment = new ActivityType();
			hairCuttingAppointment.setCode("Hair cutting appointment");
			hairCuttingAppointment.setDuration(90);
			hairCuttingAppointment.setId(typeId++);
			hairCuttingAppointment.setImposedPeriod(new ActivityPeriod(new Time(11, 0), new WeekDay(0)));

			activitySchedule.getActivityTypeList().add(hairCuttingAppointment);

			Activity activity = new Activity();
			activity.setActivityType(hairCuttingAppointment);
			activity.setId(id++);
			activity.setImmovable(true);
			activityList.add(activity);
		}

		activitySchedule.setActivityList(activityList);
		activitySchedule.setExcludedTimePeriodsList(excludedTimePeriodsPenaltyList);
		activitySchedule.setRelativeActivityPenaltyList(relativeActivityPenaltyList);
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

	private void setImposedActivities(ActivitySchedule activitySchedule) {
		for (Activity activity : activitySchedule.getActivityList())
			if (activity.getImposedPeriod() != null)
				activity.setActivityPeriod(activity.getImposedPeriod());
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

	private void createWeekDayList(ActivitySchedule activitySchedule) {
		List<WeekDay> weekDayList = new ArrayList<>(DAY_LIST_SIZE);

		for (int i = 0; i < DAY_LIST_SIZE; i++) {
			WeekDay weekDay = new WeekDay(i);
			weekDay.setId((long) i);
			weekDayList.add(weekDay);
		}

		activitySchedule.setWeekdayList(weekDayList);
	}

	private void fillTimes() {
		for (int i = 0; i < TIME_LIST_SIZE; i++) {
			TIMES[i] = new Time(0 + i, 0);
		}
	}

}