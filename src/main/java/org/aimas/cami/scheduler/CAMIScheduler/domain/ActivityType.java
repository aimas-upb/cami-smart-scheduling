package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ActivityType")
public class ActivityType extends AbstractPersistable {

	private String code;
	private int index;
	private int duration; // expressed in seconds(minutes?)
	private Difficulty difficulty; // EASY, HARD, MEDIUM(?)
	private int calories; // expressed in kcal
	private int timesPerDay, timesPerWeek;
	private Timeslot imposedTimeslot, permittedTimeslot;
	private LeadingActivity previousActivity;
	private FollowingActivity followingActivity;
	private ActivityCategory activityCategory;
	private String description;

	public ActivityCategory getActivityCategory() {
		return activityCategory;
	}

	public void setActivityCategory(ActivityCategory activityCategory) {
		this.activityCategory = activityCategory;
	}

	public LeadingActivity getPreviousActivity() {
		return previousActivity;
	}

	public void setPreviousActivity(LeadingActivity previousActivity) {
		this.previousActivity = previousActivity;
	}

	public FollowingActivity getFollowingActivity() {
		return followingActivity;
	}

	public void setFollowingActivity(FollowingActivity followingActivity) {
		this.followingActivity = followingActivity;
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

	public int getTimesPerDay() {
		return timesPerDay;
	}

	public void setTimesPerDay(int timesPerDay) {
		this.timesPerDay = timesPerDay;
	}

	public int getTimesPerWeek() {
		return timesPerWeek;
	}

	public void setTimesPerWeek(int timesPerWeek) {
		this.timesPerWeek = timesPerWeek;
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
