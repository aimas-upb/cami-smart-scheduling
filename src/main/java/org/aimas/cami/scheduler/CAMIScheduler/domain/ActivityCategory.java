package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ActivityCategory")
public class ActivityCategory extends AbstractPersistable {

	private String code;
	private int index;
	private Activity activity;

	/*
	 * used when need to compare the domain when are overlapped activities(e.g.
	 * leisure vs health, can overlap)
	 */
	private ActivityDomain domain;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Activity getActivity() {
		return activity;
	}

	public ActivityDomain getDomain() {
		return domain;
	}

	public void setDomain(ActivityDomain domain) {
		this.domain = domain;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
