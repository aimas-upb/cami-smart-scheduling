package org.aimas.cami.scheduler.CAMIScheduler.utils;

import java.util.HashMap;
import java.util.Map;

public class DaysPatterns {

	private static final Map<Integer, String[]> patterns;
	static {
		patterns = new HashMap<>();
		String[] s1 = { "024", "135", "246" };
		patterns.put(3, s1);

		String[] s2 = { "03", "14", "25", "36" };
		patterns.put(2, s2);
	}

	public static Map<Integer, String[]> getPatterns() {
		return patterns;
	}

}
