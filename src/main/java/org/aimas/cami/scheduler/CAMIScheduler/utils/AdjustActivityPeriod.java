package org.aimas.cami.scheduler.CAMIScheduler.utils;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Time;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Timeslot;

/**
 * Shift a period(to left or to right).
 * 
 * @author Bogdan
 *
 */
public class AdjustActivityPeriod {

	public static ActivityPeriod setPeriod(ActivityPeriod period, int offsetTime) {
		Timeslot timeslot = period.getTimeslot();
		Time[] newTimeslotInterval = new Time[2];

		int leftIntervalTimeslotMinutes = timeslot.getTimeslot()[0].getMinutes();
		int rightIntervalTimeslotMinutes = timeslot.getTimeslot()[1].getMinutes();

		// convert "offsetTime" minutes to Time format
		int hours = offsetTime / 60;
		int minutes = offsetTime % 60;

		// adjust left and right intervals
		if (leftIntervalTimeslotMinutes + minutes >= 60) {
			hours++;
			minutes = (leftIntervalTimeslotMinutes + minutes) % 60;
		} else if (leftIntervalTimeslotMinutes + minutes < 0) {
			hours--;
			minutes = leftIntervalTimeslotMinutes + minutes;
		}

		newTimeslotInterval[0] = new Time(timeslot.getTimeslot()[0].getHour() + hours, leftIntervalTimeslotMinutes + minutes);

		// reset
		hours = offsetTime / 60;
		minutes = offsetTime % 60;

		if (rightIntervalTimeslotMinutes + minutes >= 60) {
			hours++;
			minutes = (rightIntervalTimeslotMinutes + minutes) % 60;
		} else if (rightIntervalTimeslotMinutes + minutes < 0) {
			hours--;
			minutes = rightIntervalTimeslotMinutes + minutes;
		}

		newTimeslotInterval[1] = new Time(timeslot.getTimeslot()[1].getHour() + hours, rightIntervalTimeslotMinutes + minutes);

		return new ActivityPeriod(new Timeslot(newTimeslotInterval), period.getWeekDay());
	}

	/*
	public static ActivityPeriod SetActivityPeriodInPermittedTimeslot(ActivityPeriod activityPeriod,
			Timeslot permittedTimeslot, int duration) {
		Time[] timeslotInterval = new Time[2];
		timeslotInterval[0] = permittedTimeslot.getTimeslot()[0];

		int hours = duration / 60;
		int minutes = duration % 60;

		if (timeslotInterval[0].getMinutes() + minutes >= 60) {
			hours++;
			minutes = (timeslotInterval[0].getMinutes() + minutes) % 60;
		}

		timeslotInterval[1] = new Time(timeslotInterval[0].getHour() + hours, minutes);

		Timeslot timeslot = new Timeslot(timeslotInterval);
		activityPeriod.setTimeslot(timeslot);

		return activityPeriod;
	}
	*/
}
