package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author Bogdan
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Time extends AbstractPersistable {

	private int hour;
	private int minutes;

	public Time() {
	}

	public Time(int hour, int minutes) {
		super();
		this.hour = hour;
		this.minutes = minutes;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	@Override
	public String toString() {
		return "Time [hour=" + hour + ", minutes=" + minutes + "]";
	}

	public String getLabel() {
		String hour = "";
		String minutes = "";
		hour = this.hour < 10 ? ("0" + this.hour) : (this.hour + "");
		minutes = this.minutes < 10 ? ("0" + this.minutes) : (this.minutes + "");
		return hour + ":" + minutes;
	}

}
