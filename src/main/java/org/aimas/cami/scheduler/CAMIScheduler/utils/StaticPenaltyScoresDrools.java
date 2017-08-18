package org.aimas.cami.scheduler.CAMIScheduler.utils;

public class StaticPenaltyScoresDrools {

	public static final int periodConflictPenalty = -5;
	public static final int excludedTimeslotsSameDayPenalty = -4;
	public static final int excludedTimeslotsBetweenDaysPenalty = -3;
	//public static final int relativeActivityBeforePenaltyDifferentDays = -4;
	//public static final int relativeActivityAfterPenaltyDifferentDays = -5;
	public static final int instancesPerWeekPenalty = -2;
	public static final int instancesPerDayPenalty = -1;
}
