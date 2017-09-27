package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.swingui.Labeled;

/**
 * Difficulty level an activity can have.
 * 
 * @author Bogdan
 *
 */
public enum Difficulty implements Labeled {
	EASY, MEDIUM, HARD;

	@Override
	public String getLabel() {
		return this.toString();
	}
}
