package org.aimas.cami.scheduler.CAMIScheduler.swingui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ScoreParametrization;

public class ScoreParametrizationDialog extends JDialog {

	protected final CAMITaskSchedulerPanel camiTaskSchedulerPanel;

	private JSpinner instancesPerDayPenaltyField;
	private JSpinner instancesPerWeekPenaltyField;
	private JSpinner periodConflictPenaltyField;
	private JSpinner earlyHourField;
	private JSpinner distanceBetweenExerciseAndMealField;
	private JSpinner distanceBetweenExercisesField;
	private JSpinner hardExerciseLateHourField;

	protected final AbstractAction saveAction;
	protected final AbstractAction cancelAction;

	public ScoreParametrizationDialog(Frame frame, CAMITaskSchedulerPanel camiTaskSchedulerPanel) {
		super(frame, "Edit score parametrization", true);
		this.camiTaskSchedulerPanel = camiTaskSchedulerPanel;
		JPanel contentPanel = new JPanel(new BorderLayout(5, 5));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		contentPanel.add(createCustomDialogPanel(), BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel(new FlowLayout());

		saveAction = new AbstractAction("Save") {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAction();
			}
		};
		buttonPanel.add(new JButton(saveAction));
		cancelAction = new AbstractAction("Cancel") {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		};

		buttonPanel.add(new JButton(cancelAction));
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);

		setContentPane(contentPanel);
		pack();
	}

	public void setScoreParametrization(ScoreParametrization scoreParametrization) {
		instancesPerDayPenaltyField.setValue(scoreParametrization.getInstancesPerDayPenalty());
		instancesPerWeekPenaltyField.setValue(scoreParametrization.getInstancesPerWeekPenalty());
		periodConflictPenaltyField.setValue(scoreParametrization.getPeriodConflictPenalty());
		earlyHourField.setValue(scoreParametrization.getEarlyHour());
		distanceBetweenExerciseAndMealField.setValue(scoreParametrization.getDistanceBetweenExerciseAndMeal());
		distanceBetweenExercisesField.setValue(scoreParametrization.getDistanceBetweenExercises());
		hardExerciseLateHourField.setValue(scoreParametrization.getHardExerciseLateHour());

		setLocationRelativeTo(camiTaskSchedulerPanel.getTopLevelAncestor());
	}

	private JPanel createCustomDialogPanel() {
		JPanel dialogPanel = new JPanel(new GridLayout(0, 2, 5, 5));

		dialogPanel.add(new JLabel("instances per day penalty"));
		instancesPerDayPenaltyField = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
		dialogPanel.add(instancesPerDayPenaltyField);

		dialogPanel.add(new JLabel("instances per week penalty"));
		instancesPerWeekPenaltyField = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
		dialogPanel.add(instancesPerWeekPenaltyField);

		dialogPanel.add(new JLabel("period conflict between activities penalty"));
		periodConflictPenaltyField = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
		dialogPanel.add(periodConflictPenaltyField);

		dialogPanel.add(new JLabel("activities must be planned after this hour"));
		earlyHourField = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
		dialogPanel.add(earlyHourField);

		dialogPanel.add(new JLabel("distance between exercise and meal(minutes)"));
		distanceBetweenExerciseAndMealField = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
		dialogPanel.add(distanceBetweenExerciseAndMealField);

		dialogPanel.add(new JLabel("distance between exercises(minutes)"));
		distanceBetweenExercisesField = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
		dialogPanel.add(distanceBetweenExercisesField);

		dialogPanel.add(new JLabel("hard exercises must not be planned after this hour"));
		hardExerciseLateHourField = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
		dialogPanel.add(hardExerciseLateHourField);

		return dialogPanel;
	}

	private void saveAction() {
		final int instancesPerDayPenalty = (Integer) instancesPerDayPenaltyField.getValue();
		final int instancesPerWeekPenalty = (Integer) instancesPerWeekPenaltyField.getValue();
		final int periodConflictPenalty = (Integer) periodConflictPenaltyField.getValue();
		final int earlyHour = (Integer) earlyHourField.getValue();
		final int distanceBetweenExerciseAndMeal = (Integer) distanceBetweenExerciseAndMealField.getValue();
		final int distanceBetweenExercises = (Integer) distanceBetweenExercisesField.getValue();
		final int hardExerciseLateHour = (Integer) hardExerciseLateHourField.getValue();
		setVisible(false);

		camiTaskSchedulerPanel.doProblemFactChange(scoreDirector -> {
			ActivitySchedule activitySchedule = scoreDirector.getWorkingSolution();
			ScoreParametrization scoreParametrization = activitySchedule.getScoreParametrization();

			scoreDirector.beforeProblemPropertyChanged(scoreParametrization);

			scoreParametrization.setInstancesPerDayPenalty(instancesPerDayPenalty);
			scoreParametrization.setInstancesPerWeekPenalty(instancesPerWeekPenalty);
			scoreParametrization.setPeriodConflictPenalty(periodConflictPenalty);
			scoreParametrization.setEarlyHour(earlyHour);
			scoreParametrization.setDistanceBetweenExerciseAndMeal(distanceBetweenExerciseAndMeal);
			scoreParametrization.setDistanceBetweenExercises(distanceBetweenExercises);
			scoreParametrization.setHardExerciseLateHour(hardExerciseLateHour);

			scoreDirector.afterProblemPropertyChanged(scoreParametrization);
		}, true);
	}
}
