package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * How time is represented in our schedule.
 * 
 * @author Bogdan
 *
 */
@XStreamAlias("Time")
public class Time extends AbstractPersistable {

	private int time;

	public Time(int time) {
		super();
		this.time = time;
	}

	public Time(int hour, int minutes) {
		super();
		this.time = hour * 60 + minutes;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getHour() {
		return time / 60;
	}

	public int getMinutes() {
		return time % 60;
	}

	public String getLabel() {
		String hour = "";
		String minutes = "";
		int hourTime = getHour();
		int minutesTime = getMinutes();
		hour = hourTime < 10 ? ("0" + hourTime) : (hourTime + "");
		minutes = minutesTime < 10 ? ("0" + minutesTime) : (minutesTime + "");
		return hour + ":" + minutes;
	}

}
