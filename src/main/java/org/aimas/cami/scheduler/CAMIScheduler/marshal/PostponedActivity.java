package org.aimas.cami.scheduler.CAMIScheduler.marshal;

import org.aimas.cami.scheduler.CAMIScheduler.postpone.PostponeType;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Represents an activity from the schedule that has to be postponed.
 * 
 * @author Bogdan
 *
 */
@XStreamAlias("PostponedActivity")
public class PostponedActivity extends AbstractPersistable {
	private String name;
	private String uuid;
	private PostponeType postponeType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public PostponeType getPostponeType() {
		return postponeType;
	}

	public void setPostponeType(PostponeType postponeType) {
		this.postponeType = postponeType;
	}

}
