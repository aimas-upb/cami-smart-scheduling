package org.aimas.cami.scheduler.CAMIScheduler.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.drools.core.spi.KnowledgeHelper;

public class Utility {

	public static List<Integer> accumulateList;

	public static void help(final KnowledgeHelper drools, final String message) {
		System.out.println(message);
		System.out.println("rule triggered: " + drools.getRule().getName() + "\n");
	}

	public static void helper(final KnowledgeHelper drools) {
		System.out.println("\nrule triggered: " + drools.getRule().getName());
	}

	public static void initializeList() {
		accumulateList = new ArrayList<>();
	}

	public static void clearList() {
		accumulateList.clear();
	}

	public static void sortList() {
		Collections.sort(accumulateList);
	}

}