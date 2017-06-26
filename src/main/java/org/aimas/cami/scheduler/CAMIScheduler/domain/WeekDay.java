package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("WeekDay")
public class WeekDay extends AbstractPersistable{

	private WeekDays weekDays;
	private int dayIndex;

	public WeekDays getWeekDays() {
		return weekDays;
	}

	public void setWeekDays(WeekDays weekDays) {
		this.weekDays = weekDays;
	}

	public WeekDays getDay() {
		return WeekDays.values()[dayIndex];
	}

	public int getDayIndex() {
		return dayIndex;
	}

	public void setDayIndex(int dayIndex) {
		this.dayIndex = dayIndex;
	}

	@Override
	public String toString() {
		return "WeekDay [day=" + WeekDays.values()[dayIndex] + "]";
	}

}
