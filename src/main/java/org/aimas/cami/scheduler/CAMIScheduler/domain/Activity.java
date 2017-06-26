package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@PlanningEntity
@XStreamAlias("Activity")
public class Activity extends AbstractPersistable {

	// planning variable
	private ActivityPeriod activityPeriod;

	private ActivityType activityType;
	private int index;

	@PlanningVariable(valueRangeProviderRefs = {"periodRange"})
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "Activity [activityType=" + activityType + ", activityPeriod=" + activityPeriod + "]";
	}

}