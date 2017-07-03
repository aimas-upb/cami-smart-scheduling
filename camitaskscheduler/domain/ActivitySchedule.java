package org.optaplanner.examples.camitaskscheduler.domain;

import java.util.List;

import org.optaplanner.examples.common.domain.AbstractPersistable;

public class ActivitySchedule extends AbstractPersistable {
	// Solution class
	private List<Activity> activityList;
	private List<ActivityCategory> activityCategoryList;
	private List<ActivityPeriod> activityPeriodList;
	private List<ActivitySubcategory> activitySubcategoryList;
	private List<ActivityType> activityTypeList;
	private List<Timeslot> timeslotList;
	private List<WeekDay> weekdayList;

	public List<Activity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<Activity> activityList) {
		this.activityList = activityList;
	}

	public List<ActivityCategory> getActivityCategoryList() {
		return activityCategoryList;
	}

	public void setActivityCategoryList(List<ActivityCategory> activityCategoryList) {
		this.activityCategoryList = activityCategoryList;
	}

	public List<ActivityPeriod> getActivityPeriodList() {
		return activityPeriodList;
	}

	public void setActivityPeriodList(List<ActivityPeriod> activityPeriodList) {
		this.activityPeriodList = activityPeriodList;
	}

	public List<ActivitySubcategory> getActivitySubcategoryList() {
		return activitySubcategoryList;
	}

	public void setActivitySubcategoryList(List<ActivitySubcategory> activitySubcategoryList) {
		this.activitySubcategoryList = activitySubcategoryList;
	}

	public List<ActivityType> getActivityTypeList() {
		return activityTypeList;
	}

	public void setActivityTypeList(List<ActivityType> activityTypeList) {
		this.activityTypeList = activityTypeList;
	}

	public List<Timeslot> getTimeslotList() {
		return timeslotList;
	}

	public void setTimeslotList(List<Timeslot> timeslotList) {
		this.timeslotList = timeslotList;
	}

	public List<WeekDay> getWeekdayList() {
		return weekdayList;
	}

	public void setWeekdayList(List<WeekDay> weekdayList) {
		this.weekdayList = weekdayList;
	}
	
	// ************************************************************************
    // Complex methods
    // ************************************************************************

}
