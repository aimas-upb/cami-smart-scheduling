package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ExcludedTimeslots")
public class ExcludedTimeslotsPenalty extends AbstractPersistable {

	ActivityType activityType;
	Timeslot excludedTimeslot;

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public Timeslot getExcludedTimeslot() {
		return excludedTimeslot;
	}

	public void setExcludedTimeslot(Timeslot excludedTimeslot) {
		this.excludedTimeslot = excludedTimeslot;
	}

}
