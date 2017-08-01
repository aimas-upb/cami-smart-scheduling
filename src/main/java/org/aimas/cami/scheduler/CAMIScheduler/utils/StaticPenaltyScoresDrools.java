package org.aimas.cami.scheduler.CAMIScheduler.utils;

public class StaticPenaltyScoresDrools {

	public static final int periodConflictPenalty = -1;
	public static final int imposedTimePenalty = -2;
	public static final int excludedTimeslotsSameDayPenalty = -3;
	public static final int excludedTimeslotsBetweenDaysPenalty = -4;
	public static final int relativeActivityBeforePenaltyDifferentDays = -5;
	public static final int relativeActivityAfterPenaltyDifferentDays = -6;
	public static final int instancesPerWeekPenalty = -7;
	public static final int instancesPerDayPenalty = -8;
}
