package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

/**
 * 
 * @author Bogdan
 *
 */
public class ScoreParametrization extends AbstractPersistable {

	private int instancesPerDayPenalty;
	private int instancesPerWeekPenalty;
	private int periodConflictPenalty;
	private int earlyHour;
	private int distanceBetweenExerciseAndMeal;
	private int distanceBetweenExercises;
	private int hardExerciseLateHour;

	public ScoreParametrization() {
	}

	public int getInstancesPerDayPenalty() {
		return instancesPerDayPenalty;
	}

	public void setInstancesPerDayPenalty(int instancesPerDayPenalty) {
		this.instancesPerDayPenalty = instancesPerDayPenalty;
	}

	public int getInstancesPerWeekPenalty() {
		return instancesPerWeekPenalty;
	}

	public void setInstancesPerWeekPenalty(int instancesPerWeekPenalty) {
		this.instancesPerWeekPenalty = instancesPerWeekPenalty;
	}

	public int getPeriodConflictPenalty() {
		return periodConflictPenalty;
	}

	public void setPeriodConflictPenalty(int periodConflictPenalty) {
		this.periodConflictPenalty = periodConflictPenalty;
	}

	public int getEarlyHour() {
		return earlyHour;
	}

	public void setEarlyHour(int earlyHour) {
		this.earlyHour = earlyHour;
	}

	public int getDistanceBetweenExerciseAndMeal() {
		return distanceBetweenExerciseAndMeal;
	}

	public void setDistanceBetweenExerciseAndMeal(int distanceBetweenExerciseAndMeal) {
		this.distanceBetweenExerciseAndMeal = distanceBetweenExerciseAndMeal;
	}

	public int getDistanceBetweenExercises() {
		return distanceBetweenExercises;
	}

	public void setDistanceBetweenExercises(int distanceBetweenExercises) {
		this.distanceBetweenExercises = distanceBetweenExercises;
	}

	public int getHardExerciseLateHour() {
		return hardExerciseLateHour;
	}

	public void setHardExerciseLateHour(int hardExerciseLateHour) {
		this.hardExerciseLateHour = hardExerciseLateHour;
	}

}
