package org.aimas.cami.scheduler.CAMIScheduler.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelativeActivityMap {
	private static Map<Long, Long> assignedActivities = new HashMap<>();
	private static List<Long> assignedRelativeActivities = new ArrayList<>();

	public static Map<Long, Long> getAssignedActivities() {
		return assignedActivities;
	}

	public static void setAssignedActivities(Map<Long, Long> assignedActivities) {
		RelativeActivityMap.assignedActivities = assignedActivities;
	}

	public static List<Long> getAssignedRelativeActivities() {
		return assignedRelativeActivities;
	}

	public static void setAssignedRelativeActivities(List<Long> assignedRelativeActivities) {
		RelativeActivityMap.assignedRelativeActivities = assignedRelativeActivities;
	}

}
