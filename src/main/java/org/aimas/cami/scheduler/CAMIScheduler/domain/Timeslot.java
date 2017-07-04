package org.aimas.cami.scheduler.CAMIScheduler.domain;

import java.util.Arrays;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author Bogdan
 *
 */
@XStreamAlias("Timeslot")
public class Timeslot extends AbstractPersistable {

	// min left, max right;
	private Time[] timeslot = new Time[2];

	public Timeslot(Time[] timeslot) {
		super();
		this.timeslot = timeslot;
	}

	public Time[] getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(Time[] timeslot) {
		this.timeslot = timeslot;
	}

	@Override
	public String toString() {
		return "Timeslot [timeslot=" + Arrays.toString(timeslot) + "]";
	}

}
