package org.aimas.cami.scheduler.CAMIScheduler.domain;

import java.util.List;
import java.util.Map;

import org.aimas.cami.scheduler.CAMIScheduler.domain.solver.TimeWeightFactory;
import org.aimas.cami.scheduler.CAMIScheduler.postpone.Postpone;
import org.aimas.cami.scheduler.CAMIScheduler.solver.move.MovableActivitySelectionFilter;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AdjustActivityPeriod;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author Bogdan
 *
 */
@PlanningEntity(movableEntitySelectionFilter = MovableActivitySelectionFilter.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Activity extends AbstractPersistable {

	// planning variable
	// set "immovable" to true, so its period can't be modified
	protected ActivityPeriod activityPeriod;

	private ActivityType activityType;

	private Postpone postpone;
	Map<String, Long> assignedToRelativeActivityMap;

	// if an activity is immovable or not
	private boolean immovable;
	private int index;

	public Activity() {
	}

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

	public Map<String, Long> getAssignedToRelativeActivityMap() {
		return assignedToRelativeActivityMap;
	}

	public void setAssignedToRelativeActivityMap(Map<String, Long> assignedRelativeActivityList) {
		this.assignedToRelativeActivityMap = assignedRelativeActivityList;
	}

	public void setPostpone(Postpone postpone) {
		this.postpone = postpone;
	}

	@JsonIgnore
	public ActivityPeriod getActivityEndPeriod() {
		if (activityPeriod == null) {
			return null;
		}
		return AdjustActivityPeriod.getAdjustedPeriod(activityPeriod, activityType.getDuration());
	}

	@JsonIgnore
	public Time getActivityEndPeriodTime() {
		if (activityPeriod == null) {
			return null;
		}
		return AdjustActivityPeriod.getAdjustedPeriod(activityPeriod, activityType.getDuration()).getTime();
	}

	@JsonIgnore
	public ActivityCategory getActivityCategory() {
		return activityType.getActivityCategory();
	}

	@JsonIgnore
	public int getActivityDuration() {
		return activityType.getDuration();
	}

	@JsonIgnore
	public int getInstancesPerDay() {
		return activityType.getInstancesPerDay();
	}

	@JsonIgnore
	public int getInstancesPerWeek() {
		return activityType.getInstancesPerWeek();
	}

	@JsonIgnore
	public ActivityPeriod getImposedPeriod() {
		return activityType.getImposedPeriod();
	}

	@JsonIgnore
	public List<TimeInterval> getPermittedInterval() {
		return activityType.getPermittedIntervals();
	}

	@JsonIgnore
	public Time getActivityPeriodTime() {
		if (activityPeriod == null)
			return null;
		return activityPeriod.getTime();
	}

	@JsonIgnore
	public WeekDay getActivityPeriodWeekday() {
		if (activityPeriod == null)
			return null;
		return activityPeriod.getWeekDay();
	}

	@Override
	public String toString() {
		return "Activity [activityType=" + activityType + ", activityPeriod=" + activityPeriod + "]";
	}

	@JsonIgnore
	public String getActivityTypeCode() {
		return activityType.getCode();
	}

}
