package org.optaplanner.examples.camitaskscheduler.domain;

import org.optaplanner.examples.common.domain.AbstractPersistable;
import org.optaplanner.examples.common.swingui.components.Labeled;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ActivityPeriod")
public class ActivityPeriod extends AbstractPersistable {

	private Timeslot timeslot;
	private WeekDay weekDay;

	public Timeslot getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(Timeslot timeslot) {
		this.timeslot = timeslot;
	}

	public WeekDay getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(WeekDay weekDay) {
		this.weekDay = weekDay;
	}

	@Override
	public String toString() {
		return "ActivityPeriod [weekDay=" + weekDay + ", timeslot=" + timeslot + "]";
	}

}