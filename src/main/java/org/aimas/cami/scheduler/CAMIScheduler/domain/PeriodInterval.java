package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author Bogdan
 *
 */
@XStreamAlias("PeriodInterval")
public class PeriodInterval extends AbstractPersistable {

	ActivityPeriod startPeriod, endPeriod;

	public ActivityPeriod getStartPeriod() {
		return startPeriod;
	}

	public void setStartPeriod(ActivityPeriod startPeriod) {
		this.startPeriod = startPeriod;
	}

	public ActivityPeriod getEndPeriod() {
		return endPeriod;
	}

	public void setEndPeriod(ActivityPeriod endPeriod) {
		this.endPeriod = endPeriod;
	}

	@Override
	public String toString() {
		return "PeriodInterval [startPeriod=" + startPeriod + ", endPeriod=" + endPeriod + "]";
	}

}
