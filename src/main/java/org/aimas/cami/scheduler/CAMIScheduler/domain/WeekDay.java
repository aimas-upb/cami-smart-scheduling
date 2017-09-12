package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.swingui.Labeled;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author Bogdan
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeekDay extends AbstractPersistable implements Labeled {

	private int dayIndex;

	public WeekDay() {
	}

	public WeekDay(int dayIndex) {
		super();
		this.dayIndex = dayIndex;
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

	@Override
	public String getLabel() {
		return WeekDays.values()[dayIndex].toString();
	}

}
