package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.swingui.Labeled;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author Bogdan
 *
 */
@XStreamAlias("ActivityPeriod")
public class ActivityPeriod extends AbstractPersistable implements Labeled {

	private Time time;
	private WeekDay weekDay;

	public ActivityPeriod() {

	}

	public ActivityPeriod(Time time, WeekDay weekDay) {
		super();
		this.time = time;
		this.weekDay = weekDay;
	}

	public WeekDay getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(WeekDay weekDay) {
		this.weekDay = weekDay;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	// helpful methods
	public int getPeriodHour() {
		return time.getHour();
	}

	public int getPeriodMinutes() {
		return time.getMinutes();
	}

	public Integer getWeekDayIndex() {
		return weekDay == null ? null : weekDay.getDayIndex();
	}

	@Override
	public String toString() {
		return "ActivityPeriod [weekDay=" + weekDay + ", time=" + time + "]";
	}

	@Override
	public String getLabel() {
		return weekDay.getLabel() + " " + time.getLabel();
	}

}
