package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author Bogdan
 *
 */
@XStreamAlias("ActivityType")
public class ActivityType extends AbstractPersistable {

	private String code;
	private int index;
	private int duration; // expressed in minutes
	private Difficulty difficulty; // EASY, HARD, MEDIUM
	private int calories; // expressed in kcal
	private int instancesPerDay, instancesPerWeek;
	private Timeslot imposedTimeslot, permittedTimeslot;
	private ActivityCategory activityCategory;
	private String description;

	public ActivityCategory getActivityCategory() {
		return activityCategory;
	}

	public void setActivityCategory(ActivityCategory activityCategory) {
		this.activityCategory = activityCategory;
	}

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

	public int getDuration() {
		if (duration == 0)
			return Integer.MIN_VALUE;
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Difficulty getDifficulty() {
		if (difficulty == null)
			return Difficulty.NODIFFICULTY;
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public int getCalories() {
		if (calories == 0)
			return Integer.MIN_VALUE;
		return calories;
	}

	public void setCalories(int calories) {
		this.calories = calories;
	}

	public int getInstancesPerDay() {
		return instancesPerDay;
	}

	public void setInstancesPerDay(int instancesPerDay) {
		this.instancesPerDay = instancesPerDay;
	}

	public int getInstancesPerWeek() {
		return instancesPerWeek;
	}

	public void setInstancesPerWeek(int instancesPerWeek) {
		this.instancesPerWeek = instancesPerWeek;
	}

	public Timeslot getImposedTimeslot() {
		return imposedTimeslot;
	}

	public void setImposedTimeslot(Timeslot imposedTimeslot) {
		this.imposedTimeslot = imposedTimeslot;
	}

	public Timeslot getPermittedTimeslot() {
		return permittedTimeslot;
	}

	public void setPermittedTimeslot(Timeslot permittedTimeslot) {
		this.permittedTimeslot = permittedTimeslot;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ActivityType [code=" + code + ", description=" + description + "]";
	}

}
