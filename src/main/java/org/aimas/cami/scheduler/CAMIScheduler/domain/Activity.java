package org.aimas.cami.scheduler.CAMIScheduler.domain;

import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.postpone.Postpone;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AdjustActivityPeriod;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamInclude;

/**
 * Class that abstract an "activity".
 * 
 * @author Bogdan
 *
 */
@XStreamAlias("Activity")
@XStreamInclude({ NormalActivity.class, NormalRelativeActivity.class })
public abstract class Activity extends AbstractPersistable {

	private ActivityType activityType;

	private Postpone postpone;

	// if an activity is immovable or not
	private boolean immovable;

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public Postpone getPostpone() {
		return postpone;
	}

	public void setPostpone(Postpone postpone) {
		this.postpone = postpone;
	}

	public boolean isImmovable() {
		return immovable;
	}

	public void setImmovable(boolean immovable) {
		this.immovable = immovable;
	}

	// other useful methods

	public abstract ActivityPeriod getActivityPeriod();

	public ActivityPeriod getActivityEndPeriod() {
		ActivityPeriod activityPeriod = getActivityPeriod();
		if (activityPeriod == null) {
			return null;
		}
		return AdjustActivityPeriod.getAdjustedPeriod(activityPeriod, getActivityType().getDuration());
	}

	public Time getActivityEndPeriodTime() {
		ActivityPeriod activityPeriod = getActivityPeriod();
		if (activityPeriod == null) {
			return null;
		}
		return AdjustActivityPeriod.getAdjustedPeriod(activityPeriod, getActivityType().getDuration()).getTime();
	}

	public ActivityCategory getActivityCategory() {
		return getActivityType().getActivityCategory();
	}

	public int getActivityDuration() {
		return getActivityType().getDuration();
	}

	public int getInstancesPerDay() {
		return getActivityType().getInstancesPerDay();
	}

	public int getInstancesPerWeek() {
		return getActivityType().getInstancesPerWeek();
	}

	public ActivityPeriod getImposedPeriod() {
		return getActivityType().getImposedPeriod();
	}

	public List<TimeInterval> getPermittedInterval() {
		return getActivityType().getPermittedIntervals();
	}

	public Time getActivityPeriodTime() {
		ActivityPeriod activityPeriod = getActivityPeriod();
		if (activityPeriod == null)
			return null;
		return activityPeriod.getTime();
	}

	public WeekDay getActivityPeriodWeekday() {
		ActivityPeriod activityPeriod = getActivityPeriod();
		if (activityPeriod == null)
			return null;
		return activityPeriod.getWeekDay();
	}

	public String getActivityTypeCode() {
		return activityType.getCode();
	}

}
