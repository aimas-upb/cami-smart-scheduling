package org.aimas.cami.scheduler.CAMIScheduler.swingui;

import static org.aimas.cami.scheduler.CAMIScheduler.swingui.TimeTablePanel.HeaderColumnKey.HEADER_COLUMN_GROUP1;
import static org.aimas.cami.scheduler.CAMIScheduler.swingui.TimeTablePanel.HeaderRowKey.HEADER_ROW;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
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
import javax.swing.SwingConstants;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Time;
import org.aimas.cami.scheduler.CAMIScheduler.domain.WeekDay;
import org.optaplanner.swing.impl.SwingUtils;
import org.optaplanner.swing.impl.TangoColorFactory;

public class CAMITaskSchedulerPanel extends SolutionPanel<ActivitySchedule> {

	private final TimeTablePanel<WeekDay, Time> schedulePanel;
	private Map<Integer, Time> timeMap;

	public CAMITaskSchedulerPanel() {
		setLayout(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		schedulePanel = new TimeTablePanel<>();
		tabbedPane.add("Week Schedule", new JScrollPane(schedulePanel));
		add(tabbedPane, BorderLayout.CENTER);
		setPreferredSize(PREFERRED_SCROLLABLE_VIEWPORT_SIZE);

		timeMap = new HashMap<>();
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

	private void defineGrid(ActivitySchedule activitySchedule) {
		JButton footprint = SwingUtils.makeSmallButton(new JButton("LinLetGre1-0"));
		int footprintWidth = footprint.getPreferredSize().width;

		// Time slots header
		schedulePanel.defineColumnHeaderByKey(HEADER_COLUMN_GROUP1);

		// define day headers
		for (WeekDay weekDay : activitySchedule.getWeekdayList()) {
			schedulePanel.defineColumnHeader(weekDay, footprintWidth);
		}

		// unassigned
		schedulePanel.defineColumnHeader(null, footprintWidth);

		schedulePanel.defineRowHeaderByKey(HEADER_ROW);
		for (Time time : activitySchedule.getTimeList()) {
			schedulePanel.defineRowHeader(time);
			timeMap.put(time.getHour(), time);
		}
		schedulePanel.defineRowHeader(null, footprintWidth);

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
			schedulePanel.addRowHeader(HEADER_COLUMN_GROUP1, time, createTableHeader(new JLabel(time.getLabel())));
		}

		schedulePanel.addRowHeader(HEADER_COLUMN_GROUP1, null, createTableHeader(new JLabel("Unassigned")));
	}

	private void fillActivityCells(ActivitySchedule activitySchedule) {
		preparePlanningEntityColors(activitySchedule.getActivityList());
		for (Activity activity : activitySchedule.getActivityList()) {
			Color color = determinePlanningEntityColor(activity, activity.getActivityType());
			String toolTip = determinePlanningEntityTooltip(activity);
			System.out.println("Got Something:" + activity.getActivityType().getCode() + " "
					+ activity.getActivityPeriodTime() + " " + activity.getActivityEndPeriod());

			if (activity.getActivityPeriodTime() != null)
				schedulePanel.addCell(activity.getActivityPeriodWeekday(),
						timeMap.get(activity.getActivityPeriodTime().getHour()),
						createButton(activity, color, toolTip));
			else
				schedulePanel.addCell(activity.getActivityPeriodWeekday(), activity.getActivityPeriodTime(),
						createButton(activity, color, toolTip));
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
		if (activity.getActivityPeriod() == null)
			plannedTime = "";
		else
			plannedTime = activity.getActivityPeriod().getLabel();
		button.setToolTipText("<html>" + plannedTime + "<br/><br/>" + toolTip.substring(6));
		return button;
	}

	@Override
	public boolean isIndictmentHeatMapEnabled() {
		return true;
	}

	private class ActivityAction extends AbstractAction {

		private Activity activity;

		public ActivityAction(Activity activity) {
			super(activity.getLabel());
			this.activity = activity;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel listFieldsPanel = new JPanel(new GridLayout(2, 2));
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

}
