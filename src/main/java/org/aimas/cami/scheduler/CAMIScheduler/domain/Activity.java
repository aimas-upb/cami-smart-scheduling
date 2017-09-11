package org.aimas.cami.scheduler.CAMIScheduler.domain;

import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.domain.solver.TimeWeightFactory;
import org.aimas.cami.scheduler.CAMIScheduler.postpone.Postpone;
import org.aimas.cami.scheduler.CAMIScheduler.solver.move.MovableActivitySelectionFilter;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AdjustActivityPeriod;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamInclude;

/**
 * 
 * @author Bogdan
 *
 */
@PlanningEntity(movableEntitySelectionFilter = MovableActivitySelectionFilter.class)
@XStreamAlias("Activity")
@XStreamInclude({ RelativeActivity.class })
public class Activity extends AbstractPersistable {

	// planning variable
	// set "immovable" to true, so its period can't be modified
	protected ActivityPeriod activityPeriod;

	private ActivityType activityType;

	private Postpone postpone;

	// if an activity is immovable or not
	private boolean immovable;
	private int index;

	// (optional) it can be set to null
	// by specifying @PlanningVariable(..., nullable = true)
	@PlanningVariable(valueRangeProviderRefs = {
			"activityPeriodRange" }, strengthWeightFactoryClass = TimeWeightFactory.class)
	public ActivityPeriod getActivityPeriod() {
		return activityPeriod;
	}

	public void setActivityPeriod(ActivityPeriod activityPeriod) {
		this.activityPeriod = activityPeriod;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
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

	public Postpone getPostpone() {
		return postpone;
	}

	public void setPostpone(Postpone postpone) {
		this.postpone = postpone;
	}

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

	public ActivityPeriod getImposedPeriod() {
		return activityType.getImposedPeriod();
	}

	public List<TimeInterval> getPermittedInterval() {
		return activityType.getPermittedIntervals();
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

	public String getActivityTypeCode() {
		return activityType.getCode();
	}

}
