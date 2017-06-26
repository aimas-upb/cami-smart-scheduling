package org.aimas.cami.scheduler.CAMIScheduler.domain;

import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
//import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
//import org.optaplanner.persistence.xstream.api.score.buildin.simple.SimpleScoreXStreamConverter;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;
import org.optaplanner.persistence.xstream.api.score.buildin.hardsoft.HardSoftScoreXStreamConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@PlanningSolution
@XStreamAlias("ActivitySchedule")
public class ActivitySchedule extends AbstractPersistable {

	private List<Activity> activityList;
	private List<ActivityDomain> activityDomainList;
	private List<ActivityPeriod> activityPeriodList;
	private List<ActivityCategory> activityCategoryList;
	private List<ActivityType> activityTypeList;
	private List<Timeslot> timeslotList;
	private List<WeekDay> weekdayList;

	@XStreamConverter(HardSoftScoreXStreamConverter.class)
	private HardSoftScore score;

	/*
	 * @XStreamConverter(SimpleScoreXStreamConverter.class) private SimpleScore
	 * score;
	 */

	@PlanningEntityCollectionProperty
	public List<Activity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<Activity> activityList) {
		this.activityList = activityList;
	}

	public List<ActivityDomain> getActivityCategoryList() {
		return activityDomainList;
	}

	public void setActivityCategoryList(List<ActivityDomain> activityCategoryList) {
		this.activityDomainList = activityCategoryList;
	}

	@ValueRangeProvider(id = "periodRange")
	@ProblemFactCollectionProperty
	public List<ActivityPeriod> getActivityPeriodList() {
		return activityPeriodList;
	}

	public void setActivityPeriodList(List<ActivityPeriod> activityPeriodList) {
		this.activityPeriodList = activityPeriodList;
	}

	@ProblemFactCollectionProperty
	public List<ActivityCategory> getActivitySubcategoryList() {
		return activityCategoryList;
	}

	public void setActivitySubcategoryList(List<ActivityCategory> activitySubcategoryList) {
		this.activityCategoryList = activitySubcategoryList;
	}

	@ProblemFactCollectionProperty
	public List<ActivityType> getActivityTypeList() {
		return activityTypeList;
	}

	public void setActivityTypeList(List<ActivityType> activityTypeList) {
		this.activityTypeList = activityTypeList;
	}

	@ProblemFactCollectionProperty
	public List<Timeslot> getTimeslotList() {
		return timeslotList;
	}

	public void setTimeslotList(List<Timeslot> timeslotList) {
		this.timeslotList = timeslotList;
	}

	@ProblemFactCollectionProperty
	public List<WeekDay> getWeekdayList() {
		return weekdayList;
	}

	public void setWeekdayList(List<WeekDay> weekdayList) {
		this.weekdayList = weekdayList;
	}

	/*
	 * @PlanningScore public SimpleScore getScore() { return score; }
	 * 
	 * public void setScore(SimpleScore score) { this.score = score; }
	 */

	@PlanningScore
	public HardSoftScore getScore() {
		return score;
	}

	public void setScore(HardSoftScore score) {
		this.score = score;
	}

	// ************************************************************************
	// Complex methods
	// ************************************************************************

}
