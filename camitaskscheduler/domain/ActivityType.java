package org.optaplanner.examples.camitaskscheduler.domain;

import java.util.List;
import org.optaplanner.examples.common.domain.AbstractPersistable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ActivityType")
public class ActivityType extends AbstractPersistable {

	private String code;
	private int index;
	private int duration; // expressed in seconds(minutes?)
	private Difficulty difficulty; // EASY, HARD, MEDIUM(?)
	private int calories; // expressed in kcal
	private int timesPerDay, timesPerWeek;
	private List<Timeslot> excludedTimeslots;
	private Timeslot fixedTimeslot, variableTimeslot;
	private String description;

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
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public int getCalories() {
		return calories;
	}

	public void setCalories(int calories) {
		this.calories = calories;
	}

	public List<Timeslot> getExcludedTimeslots() {
		return excludedTimeslots;
	}

	public void setExcludedTimeslots(List<Timeslot> excludedTimeslots) {
		this.excludedTimeslots = excludedTimeslots;
	}

	public Timeslot getFixedTimeslot() {
		return fixedTimeslot;
	}

	public void setFixedTimeslot(Timeslot fixedTimeslot) {
		this.fixedTimeslot = fixedTimeslot;
	}

	public Timeslot getVariableTimeslot() {
		return variableTimeslot;
	}

	public void setVariableTimeslot(Timeslot variableTimeslot) {
		this.variableTimeslot = variableTimeslot;
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
