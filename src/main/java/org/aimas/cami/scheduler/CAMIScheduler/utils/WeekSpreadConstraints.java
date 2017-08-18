package org.aimas.cami.scheduler.CAMIScheduler.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap that holds pairs (instancesPerWeek, gapBetweenInstances)
 * 
 * @author Bogdan
 *
 */
public class WeekSpreadConstraints {

	private static final Map<Integer, Integer> spreadGap;
	static {
		spreadGap = new HashMap<>();
		spreadGap.put(3, 2);
		spreadGap.put(2, 3);
	}

	public static Map<Integer, Integer> getSpreadGap() {
		return spreadGap;
	}

}
