package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.domain.solver.RelativeActivityPeriodUpdateListener;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableReference;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author Bogdan
 *
 */
@PlanningEntity
@XStreamAlias("RelativeActivity")
public class RelativeActivity extends Activity {

	// private Activity staticActivity;

	// shadow variable (it depends on a planning variable)
	// if the planning variable changes, then the shadow variable adjusts
	// according to it
	private ActivityPeriod relativeActivityPeriod;

	private int offset; // in minutes

	/*
	 * public Activity getStaticActivity() { return staticActivity; }
	 * 
	 * public void setStaticActivity(Activity staticActivity) {
	 * this.staticActivity = staticActivity; }
	 */

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@CustomShadowVariable(variableListenerClass = RelativeActivityPeriodUpdateListener.class, sources = {
			@PlanningVariableReference(entityClass = Activity.class, variableName = "activityPeriod") })
	public ActivityPeriod getRelativeActivityPeriod() {
		return relativeActivityPeriod;
	}

	public void setRelativeActivityPeriod(ActivityPeriod relativeActivityPeriod) {
		this.relativeActivityPeriod = relativeActivityPeriod;
	}

	@Override
	public String toString() {
		return "RelativeActivity [getActivityType()=" + getActivityType() + ", relativeActivityPeriod=" + relativeActivityPeriod
				+ ", offset=" + offset + "]";
	}

}
