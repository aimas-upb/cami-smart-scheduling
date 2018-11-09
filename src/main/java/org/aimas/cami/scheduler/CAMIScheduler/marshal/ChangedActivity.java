package org.aimas.cami.scheduler.CAMIScheduler.marshal;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author Bogdan
 *
 */
public class ChangedActivity {

	private static final AtomicInteger COUNTER = new AtomicInteger();

	private final int id;

	private String activity;
	private String uuid;
	private long oldActivityPeriod;
	private long newActivityPeriod;
	private int activityDurationInMinutes;

	public ChangedActivity(String activity, String uuid, long oldActivityPeriod, long newActivityPeriod,
			int activityDurationInMinutes) {
		super();
		this.id = COUNTER.getAndIncrement();
		this.activity = activity;
		this.uuid = uuid;
		this.oldActivityPeriod = oldActivityPeriod;
		this.newActivityPeriod = newActivityPeriod;
		this.activityDurationInMinutes = activityDurationInMinutes;
	}

	public ChangedActivity() {
		this.id = COUNTER.getAndIncrement();
	}

	public long getOldActivityPeriod() {
		return oldActivityPeriod;
	}

	public void setOldActivityPeriod(long oldActivityPeriod) {
		this.oldActivityPeriod = oldActivityPeriod;
	}

	public long getNewActivityPeriod() {
		return newActivityPeriod;
	}

	public void setNewActivityPeriod(long newActivityPeriod) {
		this.newActivityPeriod = newActivityPeriod;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getActivityDurationInMinutes() {
		return activityDurationInMinutes;
	}

	public void setActivityDurationInMinutes(int activityDurationInMinutes) {
		this.activityDurationInMinutes = activityDurationInMinutes;
	}

	public int getId() {
		return id;
	}

}
