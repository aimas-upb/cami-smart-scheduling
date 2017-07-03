package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@PlanningEntity
@XStreamAlias("LeadingActivity")
public class LeadingActivity extends Activity {

	private ActivityPeriod delayPeriod;


	@PlanningVariable
	public ActivityPeriod getDelayPeriod() {
		return delayPeriod;
	}

	public void setDelayPeriod(ActivityPeriod delayPeriod) {
		this.delayPeriod = delayPeriod;
	}

	@Override
	public String toString() {
		return "LeadingActivity [followingActivity=" + ", delayPeriod=" + delayPeriod + "]";
	}

}
