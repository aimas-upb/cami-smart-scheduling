package org.aimas.cami.scheduler.CAMIScheduler.domain;

import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactProperty;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
//import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
//import org.optaplanner.persistence.xstream.api.score.buildin.simple.SimpleScoreXStreamConverter;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;
import org.optaplanner.persistence.xstream.api.score.buildin.hardsoft.HardSoftScoreXStreamConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * The solution class used to serialize(using CAMITaskSchedulerGenerator) the
 * activity model. Also, it's used to load all the generated information in
 * memory(drools).
 * 
 * @author Bogdan
 *
 */
@PlanningSolution
@XStreamAlias("ActivitySchedule")
public class ActivitySchedule extends AbstractPersistable {

	private List<Activity> activityList;
	private List<ActivityDomain> activityDomainList;
	private List<ActivityPeriod> activityPeriodList;
	private List<ActivityCategory> activityCategoryList;
	private List<ActivityType> activityTypeList;
	private List<Time> timeList;
	private List<ExcludedTimePeriodsPenalty> excludedTimePeriodsList;
	private List<RelativeActivityPenalty> relativeActivityPenaltyList;
	private List<WeekDay> weekdayList;
	private ScoreParametrization scoreParametrization;

	@XStreamConverter(HardSoftScoreXStreamConverter.class)
	private HardSoftScore score;

	@PlanningEntityCollectionProperty
	public List<Activity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<Activity> activityList) {
		this.activityList = activityList;
	}

	@ProblemFactCollectionProperty
	public List<ActivityDomain> getActivityDomainList() {
		return activityDomainList;
	}

	public void setActivityDomainList(List<ActivityDomain> activityDomainList) {
		this.activityDomainList = activityDomainList;
	}

	@ProblemFactCollectionProperty
	public List<ActivityPeriod> getActivityPeriodList() {
		return activityPeriodList;
	}

	public void setActivityPeriodList(List<ActivityPeriod> activityPeriodList) {
		this.activityPeriodList = activityPeriodList;
	}

	@ProblemFactCollectionProperty
	public List<ActivityCategory> getActivityCategoryList() {
		return activityCategoryList;
	}

	public void setActivityCategoryList(List<ActivityCategory> activityCategoryList) {
		this.activityCategoryList = activityCategoryList;
	}

	@ProblemFactCollectionProperty
	public List<ActivityType> getActivityTypeList() {
		return activityTypeList;
	}

	public void setActivityTypeList(List<ActivityType> activityTypeList) {
		this.activityTypeList = activityTypeList;
	}

	@ProblemFactCollectionProperty
	public List<Time> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<Time> timeList) {
		this.timeList = timeList;
	}

	@ProblemFactCollectionProperty
	public List<WeekDay> getWeekdayList() {
		return weekdayList;
	}

	public void setWeekdayList(List<WeekDay> weekDayList) {
		this.weekdayList = weekDayList;
	}

	@PlanningScore
	public HardSoftScore getScore() {
		return score;
	}

	public void setScore(HardSoftScore score) {
		this.score = score;
	}

	@ProblemFactCollectionProperty
	public List<ExcludedTimePeriodsPenalty> getExcludedTimePeriodsList() {
		return excludedTimePeriodsList;
	}

	public void setExcludedTimePeriodsList(List<ExcludedTimePeriodsPenalty> excludedTimePeriodsList) {
		this.excludedTimePeriodsList = excludedTimePeriodsList;
	}

	@ProblemFactCollectionProperty
	public List<RelativeActivityPenalty> getRelativeActivityPenaltyList() {
		return relativeActivityPenaltyList;
	}

	public void setRelativeActivityPenaltyList(List<RelativeActivityPenalty> relativeActivityPenaltyList) {
		this.relativeActivityPenaltyList = relativeActivityPenaltyList;
	}

	@ProblemFactProperty
	public ScoreParametrization getScoreParametrization() {
		return scoreParametrization;
	}

	public void setScoreParametrization(ScoreParametrization scoreParametrization) {
		this.scoreParametrization = scoreParametrization;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activityCategoryList == null) ? 0 : activityCategoryList.hashCode());
		result = prime * result + ((activityDomainList == null) ? 0 : activityDomainList.hashCode());
		result = prime * result + ((activityList == null) ? 0 : activityList.hashCode());
		result = prime * result + ((activityPeriodList == null) ? 0 : activityPeriodList.hashCode());
		result = prime * result + ((activityTypeList == null) ? 0 : activityTypeList.hashCode());
		result = prime * result + ((excludedTimePeriodsList == null) ? 0 : excludedTimePeriodsList.hashCode());
		result = prime * result + ((relativeActivityPenaltyList == null) ? 0 : relativeActivityPenaltyList.hashCode());
		result = prime * result + ((score == null) ? 0 : score.hashCode());
		result = prime * result + ((scoreParametrization == null) ? 0 : scoreParametrization.hashCode());
		result = prime * result + ((timeList == null) ? 0 : timeList.hashCode());
		result = prime * result + ((weekdayList == null) ? 0 : weekdayList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ActivitySchedule))
			return false;
		ActivitySchedule other = (ActivitySchedule) obj;
		if (activityCategoryList == null) {
			if (other.activityCategoryList != null)
				return false;
		} else if (!activityCategoryList.equals(other.activityCategoryList))
			return false;
		if (activityDomainList == null) {
			if (other.activityDomainList != null)
				return false;
		} else if (!activityDomainList.equals(other.activityDomainList))
			return false;
		if (activityList == null) {
			if (other.activityList != null)
				return false;
		} else if (!activityList.equals(other.activityList))
			return false;
		if (activityPeriodList == null) {
			if (other.activityPeriodList != null)
				return false;
		} else if (!activityPeriodList.equals(other.activityPeriodList))
			return false;
		if (activityTypeList == null) {
			if (other.activityTypeList != null)
				return false;
		} else if (!activityTypeList.equals(other.activityTypeList))
			return false;
		if (excludedTimePeriodsList == null) {
			if (other.excludedTimePeriodsList != null)
				return false;
		} else if (!excludedTimePeriodsList.equals(other.excludedTimePeriodsList))
			return false;
		if (relativeActivityPenaltyList == null) {
			if (other.relativeActivityPenaltyList != null)
				return false;
		} else if (!relativeActivityPenaltyList.equals(other.relativeActivityPenaltyList))
			return false;
		if (score == null) {
			if (other.score != null)
				return false;
		} else if (!score.equals(other.score))
			return false;
		if (scoreParametrization == null) {
			if (other.scoreParametrization != null)
				return false;
		} else if (!scoreParametrization.equals(other.scoreParametrization))
			return false;
		if (timeList == null) {
			if (other.timeList != null)
				return false;
		} else if (!timeList.equals(other.timeList))
			return false;
		if (weekdayList == null) {
			if (other.weekdayList != null)
				return false;
		} else if (!weekdayList.equals(other.weekdayList))
			return false;
		return true;
	}

}
