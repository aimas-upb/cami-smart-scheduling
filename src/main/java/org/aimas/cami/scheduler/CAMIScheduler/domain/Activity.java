package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.aimas.cami.scheduler.CAMIScheduler.domain.solver.TimeWeightFactory;
import org.aimas.cami.scheduler.CAMIScheduler.solver.move.MovableActivitySelectionFilter;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AdjustActivityPeriod;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamInclude;

/**
 * 
 * @author Bogdan
 *
 */
@PlanningEntity(movableEntitySelectionFilter = MovableActivitySelectionFilter.class)
@XStreamAlias("Activity")
@XStreamInclude({
	RelativeActivity.class
})
public class Activity extends AbstractPersistable {

	// planning variable
	// a planning variable is initially uninitialized(unspecified in the XML)
	// it can be initialized in the XML by specifying a reference to a period
	// set "immovable" to true, so its period can't be modified
	private ActivityPeriod activityPeriod;

	private ActivityType activityType;

	// RelativeActivity class is used to update the period of an 
	// activity relative(RelativeActivity.class) to a "normal" activity(Activity.class)
	// the update is made in RelativeActivityPeriodUpdateListener.class
	private RelativeActivity relativeActivity;

	// if an activity is immovable or not
	private boolean immovable;
	private int index;

	// (optional) it can be set to null
	// by specifying @PlanningVariable(..., nullable = true)
	@PlanningVariable(valueRangeProviderRefs = { "activityPeriodRange" }, 
			strengthWeightFactoryClass = TimeWeightFactory.class)
	public ActivityPeriod getActivityPeriod() {
		return activityPeriod;
	}

	public void setActivityPeriod(ActivityPeriod activityPeriod) {
		this.activityPeriod = activityPeriod;
	}

	/*
	 * public void setStartActivityPeriod(int hour, int minutes) { Time[]
	 * timeslot = { new Time(hour, minutes), null };
	 * 
	 * activityPeriod.setTimeslot(new Timeslot(timeslot)); }
	 */

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public RelativeActivity getRelativeActivity() {
		return relativeActivity;
	}

	public void setRelativeActivity(RelativeActivity relativeActivity) {
		this.relativeActivity = relativeActivity;
	}

	public boolean isImmovable() {
		return immovable;
	}

	public void setImmovable(boolean immovable) {
		this.immovable = immovable;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	// other useful methods

	/*
	 * public ActivityPeriod getAdjustedActivityPeriod() { if
	 * (activityType.getPermittedTimeslot() != null &&
	 * activityPeriod.getTimeslot() == null) { return
	 * AdjustActivityPeriod.SetActivityPeriodInPermittedTimeslot(activityPeriod,
	 * activityType.getPermittedTimeslot(), activityType.getDuration()); } else
	 * {
	 * 
	 * } return null; }
	 */
	
	public ActivityPeriod getActivityEndPeriod() {
		if (activityPeriod == null) {
			return null;
		}
		return AdjustActivityPeriod.getAdjustedPeriod(activityPeriod, activityType.getDuration());
	}
	
	public Time getActivityEndPeriodTime() {
		if (activityPeriod == null) {
			return null;
		}
		return AdjustActivityPeriod.getAdjustedPeriod(activityPeriod, activityType.getDuration()).getTime();
	}

	public ActivityCategory getActivityCategory() {
		return activityType.getActivityCategory();
	}

	public int getActivityDuration() {
		return activityType.getDuration();
	}

	public int getInstancesPerDay() {
		return activityType.getInstancesPerDay();
	}

	public int getInstancesPerWeek() {
		return activityType.getInstancesPerWeek();
	}

	public Time getImposedTime() {
		return activityType.getImposedTime();
	}

	public TimeInterval getPermittedInterval() {
		return activityType.getPermittedInterval();
	}

	public Time getActivityPeriodTime() {
		if (activityPeriod == null)
			return null;
		return activityPeriod.getTime();
	}

	public WeekDay getActivityPeriodWeekday() {
		if (activityPeriod == null)
			return null;
		return activityPeriod.getWeekDay();
	}

	@Override
	public String toString() {
		return "Activity [activityType=" + activityType + ", activityPeriod=" + activityPeriod + "]";
	}
	
	public String getLabel() {
		return activityType.getCode();
	}

}
