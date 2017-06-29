package org.aimas.cami.scheduler.CAMIScheduler.domain;

import java.util.Arrays;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Timeslot")
public class Timeslot extends AbstractPersistable {
	
	// min left, max right;
	// if right-left = duration, then the interval is fixed
	// else is variable
	private int[] timeslot = new int[2];

	public int[] getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(int[] timeslot) {
		this.timeslot = timeslot;
	}

	@Override
	public String toString() {
		return "Timeslot [timeslot=" + Arrays.toString(timeslot) + "]";
	}

}
