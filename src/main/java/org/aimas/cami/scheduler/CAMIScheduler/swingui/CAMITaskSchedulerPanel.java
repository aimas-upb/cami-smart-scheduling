package org.aimas.cami.scheduler.CAMIScheduler.swingui;

import static org.aimas.cami.scheduler.CAMIScheduler.swingui.TimeTablePanel.HeaderColumnKey.HEADER_COLUMN_GROUP1;
import static org.aimas.cami.scheduler.CAMIScheduler.swingui.TimeTablePanel.HeaderRowKey.HEADER_ROW;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityCategory;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityType;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Difficulty;
import org.aimas.cami.scheduler.CAMIScheduler.domain.RelativeActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Time;
import org.aimas.cami.scheduler.CAMIScheduler.domain.TimeInterval;
import org.aimas.cami.scheduler.CAMIScheduler.domain.WeekDay;
import org.aimas.cami.scheduler.CAMIScheduler.postpone.Postpone;
import org.aimas.cami.scheduler.CAMIScheduler.postpone.PostponeType;
import org.optaplanner.swing.impl.SwingUtils;
import org.optaplanner.swing.impl.TangoColorFactory;

public class CAMITaskSchedulerPanel extends SolutionPanel<ActivitySchedule> {

	private final TimeTablePanel<WeekDay, Time> schedulePanel;
	private final JPanel addActivityPanel;
	private Map<Integer, Time> timeMap;
	private Map<Integer, WeekDay> weekDayMap;
	JButton addActivityButton;
	private long postponeId;

	public CAMITaskSchedulerPanel() {
		setLayout(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		schedulePanel = new TimeTablePanel<>();
		addActivityPanel = new JPanel();
		tabbedPane.add("Week Schedule", new JScrollPane(schedulePanel));
		tabbedPane.add("Add a new activity", addActivityPanel);
		add(tabbedPane, BorderLayout.CENTER);
		setPreferredSize(PREFERRED_SCROLLABLE_VIEWPORT_SIZE);
		createAddActivityButton();

		timeMap = new HashMap<>();
		weekDayMap = new HashMap<>();
		postponeId = 0L;
	}

	@Override
	public boolean isWrapInScrollPane() {
		return false;
	}

	@Override
	public void resetPanel(ActivitySchedule activitySchedule) {
		schedulePanel.reset();
		defineGrid(activitySchedule);
		fillCells(activitySchedule);
		repaint();
	}

	private void createAddActivityButton() {
		addActivityButton = SwingUtils.makeSmallButton(new JButton(new AddActivityAction()));
		addActivityButton.setToolTipText("Add activity");
		addActivityButton.setPreferredSize(new Dimension(150, 75));
		addActivityButton.setFont(new Font("TIMES NEW ROMAN", Font.BOLD, 24));
		addActivityPanel.add(addActivityButton);
	}

	private void defineGrid(ActivitySchedule activitySchedule) {
		JButton footprint = SwingUtils.makeSmallButton(new JButton("LinLetGre1-0"));
		int footprintWidth = footprint.getPreferredSize().width;

		// Times header
		schedulePanel.defineColumnHeaderByKey(HEADER_COLUMN_GROUP1);

		// define day headers
		for (WeekDay weekDay : activitySchedule.getWeekdayList()) {
			schedulePanel.defineColumnHeader(weekDay, footprintWidth);
			weekDayMap.put(weekDay.getDayIndex(), weekDay);
		}

		// unassigned
		schedulePanel.defineColumnHeader(null, footprintWidth);

		schedulePanel.defineRowHeaderByKey(HEADER_ROW);
		for (Time time : activitySchedule.getTimeList()) {
			if (time.getMinutes() == 0) {
				schedulePanel.defineRowHeader(time);
				timeMap.put(time.getHour(), time);
			}
		}
		schedulePanel.defineRowHeader(null);

	}

	private void fillCells(ActivitySchedule activitySchedule) {
		schedulePanel.addCornerHeader(HEADER_COLUMN_GROUP1, HEADER_ROW, createTableHeader(new JLabel("Time")));
		fillDayCells(activitySchedule);
		fillTimeCells(activitySchedule);
		fillActivityCells(activitySchedule);
	}

	private void fillDayCells(ActivitySchedule activitySchedule) {
		for (WeekDay weekDay : activitySchedule.getWeekdayList()) {
			schedulePanel.addColumnHeader(weekDay, HEADER_ROW,
					createTableHeader(new JLabel(weekDay.getLabel(), SwingConstants.CENTER)));
		}

		schedulePanel.addColumnHeader(null, HEADER_ROW,
				createTableHeader(new JLabel("Unassigned", SwingConstants.CENTER)));

	}

	private void fillTimeCells(ActivitySchedule activitySchedule) {
		for (Time time : activitySchedule.getTimeList()) {
			if (time.getMinutes() == 0)
				schedulePanel.addRowHeader(HEADER_COLUMN_GROUP1, time, createTableHeader(new JLabel(time.getLabel())));
		}

		schedulePanel.addRowHeader(HEADER_COLUMN_GROUP1, null, createTableHeader(new JLabel("Unassigned")));
	}

	private void fillActivityCells(ActivitySchedule activitySchedule) {
		preparePlanningEntityColors(activitySchedule.getActivityList());
		for (Activity activity : activitySchedule.getActivityList()) {
			Color color = determinePlanningEntityColor(activity, activity.getActivityType());
			String toolTip = determinePlanningEntityTooltip(activity);

			if (activity instanceof RelativeActivity)
				if (((RelativeActivity) activity).getActivityPeriod() != null)
					schedulePanel.addCell(
							weekDayMap
									.get(((RelativeActivity) activity).getActivityPeriod().getWeekDay().getDayIndex()),
							timeMap.get(((RelativeActivity) activity).getActivityPeriod().getPeriodHour()),
							createButton(activity, color, toolTip));
				else
					schedulePanel.addCell(((RelativeActivity) activity).getRelativeActivityWeekDay(),
							((RelativeActivity) activity).getRelativeActivityPeriodTime(),
							createButton(activity, color, toolTip));
			else {
				if (activity.getActivityPeriodTime() != null) {
					schedulePanel.addCell(weekDayMap.get(activity.getActivityPeriodWeekday().getDayIndex()),
							timeMap.get(activity.getActivityPeriodTime().getHour()),
							createButton(activity, color, toolTip));
				} else
					schedulePanel.addCell(activity.getActivityPeriodWeekday(), activity.getActivityPeriodTime(),
							createButton(activity, color, toolTip));
			}
		}
	}

	private JPanel createTableHeader(JLabel label) {
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.add(label, BorderLayout.NORTH);
		headerPanel.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(TangoColorFactory.ALUMINIUM_5),
						BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		return headerPanel;
	}

	private JButton createButton(Activity activity, Color color, String toolTip) {
		String plannedTime;
		JButton button = SwingUtils.makeSmallButton(new JButton(new ActivityAction(activity)));
		button.setBackground(color);

		if (activity.isImmovable()) {
			button.setIcon(new ImageIcon("locked.png"));
		}

		if (activity instanceof RelativeActivity)
			if (((RelativeActivity) activity).getActivityPeriod() == null)
				plannedTime = "";
			else
				plannedTime = ((RelativeActivity) activity).getActivityPeriod().getLabel() + " - "
						+ ((RelativeActivity) activity).getActivityEndPeriod().getTime().getLabel();
		else {
			if (activity.getActivityPeriod() == null)
				plannedTime = "";
			else
				plannedTime = activity.getActivityPeriod().getLabel() + " - "
						+ activity.getActivityEndPeriod().getTime().getLabel();
		}

		button.setToolTipText(
				"<html>" + activity.getLabel() + "<br/><br/>" + plannedTime + "<br/><br/>" + toolTip.substring(6));
		return button;
	}

	@Override
	public boolean isIndictmentHeatMapEnabled() {
		return true;
	}

	private class ActivityAction extends AbstractAction {

		private Activity activity;

		public ActivityAction(Activity activity) {
			super(activity.getLabel() + ":" + activity.getId());
			this.activity = activity;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel listFieldsPanel = new JPanel(new GridLayout(3, 2));

			listFieldsPanel.add(new JLabel("Period:"));
			ActivitySchedule activitySchedule = getSolution();
			List<ActivityPeriod> periodList = activitySchedule.getActivityPeriodList();
			JComboBox periodListField = new JComboBox<>(periodList.toArray(new Object[periodList.size() + 1]));
			LabeledComboBoxRenderer.applyToComboBox(periodListField);
			periodListField.setSelectedItem(activity.getActivityPeriod());
			listFieldsPanel.add(periodListField);

			listFieldsPanel.add(new JLabel("Immovable:"));
			JCheckBox lockedField = new JCheckBox("activity immovable during planning");
			lockedField.setSelected(activity.isImmovable());
			listFieldsPanel.add(lockedField);

			listFieldsPanel.add(new JLabel("Postpone"));
			JButton addPostponeButton = SwingUtils.makeSmallButton(new JButton(new AddPostponeAction(activity)));
			listFieldsPanel.add(addPostponeButton);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Select period for \"" + activity.getLabel() + "\"", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				ActivityPeriod toActivityPeriod = (ActivityPeriod) periodListField.getSelectedItem();

				if (activity.getActivityPeriod() != toActivityPeriod) {
					solutionBusiness.doChangeMove(activity, "activityPeriod", toActivityPeriod);
				}

				boolean toImmovable = lockedField.isSelected();

				if (activity.isImmovable() != toImmovable) {
					if (solutionBusiness.isSolving()) {
						logger.error("Not doing user change because the solver is solving.");
						return;
					}
					activity.setImmovable(toImmovable);
				}
				solverAndPersistenceFrame.resetScreen();
			}

		}

	}

	private class AddActivityAction extends AbstractAction {

		public AddActivityAction() {
			super("Add activity");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel listFieldsPanel = new JPanel(new GridLayout(4, 2));

			ActivityType activityType = new ActivityType();
			JButton activityTypeButton = SwingUtils
					.makeSmallButton(new JButton(new AddActivityTypeAction(activityType)));
			activityTypeButton.setToolTipText("Define activity type properties");
			listFieldsPanel.add(new JLabel("Define the activity type"));
			listFieldsPanel.add(activityTypeButton);

			JTextField relativeActivityField = new JTextField();
			listFieldsPanel.add(new JLabel("Relative to the activity"));
			listFieldsPanel.add(relativeActivityField);

			listFieldsPanel.add(new JLabel("Relative activity:"));
			JCheckBox relative = new JCheckBox("this activity is relative to other activity");
			relative.setSelected(false);
			listFieldsPanel.add(relative);

			listFieldsPanel.add(new JLabel("Immovable:"));
			JCheckBox lockedField = new JCheckBox("activity immovable during planning");
			lockedField.setSelected(false);
			listFieldsPanel.add(lockedField);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Add a new activity", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {
				logger.info("Add a new activity too the schedule.");

				doProblemFactChange(scoreDirector -> {

					ActivitySchedule activitySchedule = scoreDirector.getWorkingSolution();
					List<Activity> activityList = new ArrayList<>(activitySchedule.getActivityList());
					activitySchedule.setActivityList(activityList);

					// trebuie ales activity/relativeActivity
					Activity activity = new Activity();
					activity.setActivityType(activityType);
					activity.setImmovable(lockedField.isSelected());
					activity.setId(activityList.get(activityList.size() - 1).getId() + 1);

					scoreDirector.beforeEntityAdded(activity);
					activityList.add(activity);
					scoreDirector.afterEntityAdded(activity);

					scoreDirector.triggerVariableListeners();

				});

				solverAndPersistenceFrame.resetScreen();
			}

		}

	}

	private class AddActivityTypeAction extends AbstractAction {

		private ActivityType activityType;

		public AddActivityTypeAction(ActivityType activityType) {
			super("Define activity type properties");
			this.activityType = activityType;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel listFieldsPanel = new JPanel(new GridLayout(9, 2));

			ActivitySchedule activitySchedule = getSolution();

			JTextField codeField = new JTextField();
			listFieldsPanel.add(new JLabel("Code"));
			listFieldsPanel.add(codeField);

			JTextField durationField = new JTextField();
			listFieldsPanel.add(new JLabel("Duration"));
			listFieldsPanel.add(durationField);

			listFieldsPanel.add(new JLabel("Difficulty:"));
			JComboBox difficultyListField = new JComboBox<>(Difficulty.values());
			LabeledComboBoxRenderer.applyToComboBox(difficultyListField);
			difficultyListField.setSelectedItem(null);
			listFieldsPanel.add(difficultyListField);

			JTextField caloriesField = new JTextField();
			listFieldsPanel.add(new JLabel("Calories"));
			listFieldsPanel.add(caloriesField);

			JTextField instancesPerDayField = new JTextField();
			listFieldsPanel.add(new JLabel("Instances per day"));
			listFieldsPanel.add(instancesPerDayField);

			JTextField instancesPerWeekField = new JTextField();
			listFieldsPanel.add(new JLabel("Instances per week"));
			listFieldsPanel.add(instancesPerWeekField);

			ActivityPeriod imposedPeriod = new ActivityPeriod();
			JButton imposedPeriodButton = SwingUtils
					.makeSmallButton(new JButton(new AddImposedPeriodAction(imposedPeriod)));
			listFieldsPanel.add(new JLabel("Imposed period"));
			listFieldsPanel.add(imposedPeriodButton);

			List<TimeInterval> permittedIntervals = new ArrayList<>();
			JButton permittedIntervalsButton = SwingUtils
					.makeSmallButton(new JButton(new AddPermittedIntervalsAction(permittedIntervals)));
			listFieldsPanel.add(new JLabel("Permitted intervals"));
			listFieldsPanel.add(permittedIntervalsButton);

			JTextField activityCategoryField = new JTextField();
			listFieldsPanel.add(new JLabel("Activity Category"));
			listFieldsPanel.add(activityCategoryField);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Define activity type properties", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				activityType.setCode(codeField.getText());
				activityType
						.setDuration(Integer.parseInt(durationField.getText() == "" ? "0" : durationField.getText()));
				activityType.setDifficulty((Difficulty) difficultyListField.getSelectedItem());
				activityType
						.setCalories(Integer.parseInt(caloriesField.getText() == "" ? "0" : caloriesField.getText()));
				activityType.setInstancesPerDay(
						Integer.parseInt(instancesPerDayField.getText() == "" ? "0" : instancesPerDayField.getText()));
				activityType.setInstancesPerWeek(Integer
						.parseInt(instancesPerWeekField.getText() == "" ? "1" : instancesPerWeekField.getText()));
				activityType.setImposedPeriod(imposedPeriod);
				activityType.setPermittedIntervals(permittedIntervals);

				for (ActivityCategory activityCategory : activitySchedule.getActivityCategoryList()) {
					if (activityCategory.getCode().equals(activityCategoryField.getText())) {
						activityType.setActivityCategory(activityCategory);
						break;
					}
				}

				solverAndPersistenceFrame.resetScreen();
			}

		}

	}

	private class AddImposedPeriodAction extends AbstractAction {

		private ActivityPeriod imposedPeriod;

		public AddImposedPeriodAction(ActivityPeriod imposedPeriod) {
			super("Set the imposed period");
			this.imposedPeriod = imposedPeriod;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel listFieldsPanel = new JPanel(new GridLayout(3, 2));

			ActivitySchedule activitySchedule = getSolution();
			List<WeekDay> weekDayList = activitySchedule.getWeekdayList();

			listFieldsPanel.add(new JLabel("WeekDay:"));
			JComboBox weekDayListField = new JComboBox<>(weekDayList.toArray(new Object[weekDayList.size() + 1]));
			LabeledComboBoxRenderer.applyToComboBox(weekDayListField);
			weekDayListField.setSelectedItem(null);
			listFieldsPanel.add(weekDayListField);

			JTextField hourField = new JTextField();
			listFieldsPanel.add(new JLabel("Hour"));
			listFieldsPanel.add(hourField);

			JTextField minutesField = new JTextField();
			listFieldsPanel.add(new JLabel("Minutes"));
			listFieldsPanel.add(minutesField);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Set the imposed period", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				// must do sanity checks!
				WeekDay weekDay = (WeekDay) weekDayListField.getSelectedItem();
				Integer hour = Integer.parseInt(hourField.getText());
				Integer minutes = Integer.parseInt(minutesField.getText());

				imposedPeriod.setWeekDay(weekDay);
				imposedPeriod.setTime(new Time(hour, minutes));

				solverAndPersistenceFrame.resetScreen();
			}

		}

	}

	private class AddPermittedIntervalsAction extends AbstractAction {

		private List<TimeInterval> permittedIntervals;

		public AddPermittedIntervalsAction(List<TimeInterval> permittedIntervals) {
			super("Set the permitted intervals");
			this.permittedIntervals = permittedIntervals;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel listFieldsPanel = new JPanel(new GridLayout(1, 1));

			JButton addTimeIntervalButton = SwingUtils
					.makeSmallButton(new JButton(new CreateTimeIntervalAction(permittedIntervals)));
			listFieldsPanel.add(addTimeIntervalButton);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Set the permitted intervals", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				solverAndPersistenceFrame.resetScreen();
			}

		}

	}

	private class CreateTimeIntervalAction extends AbstractAction {

		private List<TimeInterval> permittedIntervals;

		public CreateTimeIntervalAction(List<TimeInterval> permittedIntervals) {
			super("Add a time interval");
			this.permittedIntervals = permittedIntervals;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel listFieldsPanel = new JPanel(new GridLayout(4, 2));

			JTextField minStartHourField = new JTextField();
			listFieldsPanel.add(new JLabel("minStart - hour"));
			listFieldsPanel.add(minStartHourField);

			JTextField minStartMinutesField = new JTextField();
			listFieldsPanel.add(new JLabel("minStart - minutes"));
			listFieldsPanel.add(minStartMinutesField);

			JTextField maxEndHourField = new JTextField();
			listFieldsPanel.add(new JLabel("maxEnd - hour"));
			listFieldsPanel.add(maxEndHourField);

			JTextField maxEndMinutesField = new JTextField();
			listFieldsPanel.add(new JLabel("maxEnd - minutes"));
			listFieldsPanel.add(maxEndMinutesField);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Create the time interval", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				TimeInterval timeInterval = new TimeInterval();

				timeInterval.setMinStart(new Time(Integer.parseInt(minStartHourField.getText()),
						Integer.parseInt(minStartMinutesField.getText())));
				timeInterval.setMaxEnd(new Time(Integer.parseInt(maxEndMinutesField.getText()),
						Integer.parseInt(minStartMinutesField.getText())));

				permittedIntervals.add(timeInterval);

				solverAndPersistenceFrame.resetScreen();
			}

		}

	}

	private class AddPostponeAction extends AbstractAction {

		private Activity activity;

		public AddPostponeAction(Activity activity) {
			super("Postpone");
			this.activity = activity;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel listFieldsPanel = new JPanel(new GridLayout(1, 1));

			listFieldsPanel.add(new JLabel("Postpone type:"));
			JComboBox postponeTypeListField = new JComboBox<>(PostponeType.values());
			LabeledComboBoxRenderer.applyToComboBox(postponeTypeListField);
			postponeTypeListField.setSelectedItem(null);
			listFieldsPanel.add(postponeTypeListField);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Postpone", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				doProblemFactChange(scoreDirector -> {

					Activity workingActivity = scoreDirector.lookUpWorkingObject(activity);

					Postpone postpone = new Postpone();
					postpone.setPostponePeriod(activity.getActivityPeriod());
					postpone.setPostponeType((PostponeType) postponeTypeListField.getSelectedItem());
					postpone.setId(postponeId++);

					if ((PostponeType) postponeTypeListField.getSelectedItem() == PostponeType.POSTPONE_LATER_THIS_WEEK
							&& workingActivity.getActivityType().getInstancesPerDay() != 0)
						return;

					scoreDirector.beforeProblemPropertyChanged(workingActivity);
					workingActivity.setPostpone(postpone);
					scoreDirector.afterProblemPropertyChanged(workingActivity);

					scoreDirector.triggerVariableListeners();

				});

				solverAndPersistenceFrame.resetScreen();
			}

		}

	}

}
