package org.aimas.cami.scheduler.CAMIScheduler.postpone;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Used to postpone an activity using drools.
 * 
 * @author Bogdan
 *
 */
@XStreamAlias("Postpone")
public class Postpone extends AbstractPersistable {

	private ActivityPeriod postponePeriod;
	private PostponeType postponeType;

	public ActivityPeriod getPostponePeriod() {
		return postponePeriod;
	}

	public void setPostponePeriod(ActivityPeriod postponePeriod) {
		this.postponePeriod = postponePeriod;
	}

	public PostponeType getPostponeType() {
		return postponeType;
	}

	public void setPostponeType(PostponeType postponeType) {
		this.postponeType = postponeType;
	}

	@Override
	public String toString() {
		return "Postpone [postponePeriod=" + postponePeriod + ", postponeType=" + postponeType + "]";
	}

}
