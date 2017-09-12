package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author Bogdan
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PeriodInterval extends AbstractPersistable {

	ActivityPeriod startPeriod, endPeriod;

	public PeriodInterval() {

	}

	public PeriodInterval(ActivityPeriod startPeriod, ActivityPeriod endPeriod) {
		super();
		this.startPeriod = startPeriod;
		this.endPeriod = endPeriod;
	}

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
