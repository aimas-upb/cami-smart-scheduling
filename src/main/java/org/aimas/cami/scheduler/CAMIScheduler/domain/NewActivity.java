package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Add a new activity from an xml input using this serializer.
 * 
 * @author Bogdan
 *
 */
@XStreamAlias("NewActivity")
public class NewActivity extends AbstractPersistable {

	private Activity activity;
	private ExcludedTimePeriodsPenalty excludedTimePeriodsPenalty;
	private RelativeActivityPenalty relativeActivityPenalty;

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public ExcludedTimePeriodsPenalty getExcludedTimePeriodsPenalty() {
		return excludedTimePeriodsPenalty;
	}

	public void setExcludedTimePeriodsPenalty(ExcludedTimePeriodsPenalty excludedTimePeriodsPenalty) {
		this.excludedTimePeriodsPenalty = excludedTimePeriodsPenalty;
	}

	public RelativeActivityPenalty getRelativeActivityPenalty() {
		return relativeActivityPenalty;
	}

	public void setRelativeActivityPenalty(RelativeActivityPenalty relativeActivityPenalty) {
		this.relativeActivityPenalty = relativeActivityPenalty;
	}

}
