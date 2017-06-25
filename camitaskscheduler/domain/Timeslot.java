package org.optaplanner.examples.camitaskscheduler.domain;

import java.util.Arrays;

import org.optaplanner.examples.common.domain.AbstractPersistable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Timeslot")
public class Timeslot extends AbstractPersistable {
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
