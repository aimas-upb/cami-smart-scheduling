package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.swingui.Labeled;

/**
 * 
 * @author Bogdan
 *
 */
public enum RelativeType implements Labeled {
	BEFORE, AFTER;

	@Override
	public String getLabel() {
		return this.toString();
	}
}
