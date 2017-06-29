package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ActivityPeriod")
public class ActivityPeriod extends AbstractPersistable {

	private Timeslot timeslot;
	private WeekDay weekDay;

	

	public WeekDay getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(WeekDay weekDay) {
		this.weekDay = weekDay;
	}

	@Override
	public String toString() {
		return "ActivityPeriod [weekDay=" + weekDay + ", fixedTimeslot=" + fixedTimeslot + "]";
	}

}
