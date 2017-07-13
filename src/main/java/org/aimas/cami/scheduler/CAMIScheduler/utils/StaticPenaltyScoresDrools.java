package org.aimas.cami.scheduler.CAMIScheduler.utils;

public class StaticPenaltyScoresDrools {

	public static final int imposedTimePenalty = -100;
	public static final int excludedTimeslotsSameDayPenalty = -10;
	public static final int excludedTimeslotsBetweenDaysPenalty = -25;
	public static final int relativeActivityPenaltyDifferentDays = -35;
	public static final int instancesPerWeekPenalty = 15;
	// for now
	public static final int instancesPerWeekPatternPenalty = -10;
	public static final int instancesPerDayPenalty = -5;
}
