/*
 * Copyright 2010 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.aimas.cami.scheduler.CAMIScheduler.swingui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.filechooser.FileFilter;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NormalActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ScoreParametrization;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Time;
import org.aimas.cami.scheduler.CAMIScheduler.utils.SolutionBusiness;
import org.aimas.cami.scheduler.CAMIScheduler.utils.Utility;
import org.apache.commons.io.FilenameUtils;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.FeasibilityScore;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.swing.impl.SwingUtils;
import org.optaplanner.swing.impl.TangoColorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @param <Solution_>
 *            the solution type, the class with the {@link PlanningSolution}
 *            annotation
 */
public class SolverAndPersistenceFrame<Solution_> extends JFrame {

	protected final transient Logger logger = LoggerFactory.getLogger(getClass());

	private final SolutionBusiness<Solution_> solutionBusiness;
	private final ImageIcon indictmentHeatMapTrueIcon;
	private final ImageIcon indictmentHeatMapFalseIcon;
	private final ImageIcon refreshScreenDuringSolvingTrueIcon;
	private final ImageIcon refreshScreenDuringSolvingFalseIcon;

	private SolutionPanel<Solution_> solutionPanel;
	private ConstraintMatchesDialog constraintMatchesDialog;

	private JPanel quickOpenUnsolvedPanel;
	private List<Action> quickOpenUnsolvedActionList;
	private JPanel quickOpenSolvedPanel;
	private List<Action> quickOpenSolvedActionList;
	private Action openAction;
	private Action saveAction;
	private JToggleButton refreshScreenDuringSolvingToggleButton;
	private JToggleButton indictmentHeatMapToggleButton;
	private Action solveAction;
	private JButton solveButton;
	private Action terminateSolvingEarlyAction;
	private JButton terminateSolvingEarlyButton;
	private JPanel middlePanel;
	private JProgressBar progressBar;
	private JTextField scoreField;
	private ShowConstraintMatchesDialogAction showConstraintMatchesDialogAction;

	private Timer notificationTimer;
	private Timer activityTimer;
	protected boolean solutionWasOpened;

	public SolverAndPersistenceFrame(SolutionBusiness<Solution_> solutionBusiness,
			SolutionPanel<Solution_> solutionPanel) {
		super(solutionBusiness.getAppName());
		this.solutionBusiness = solutionBusiness;
		this.solutionPanel = solutionPanel;
		solutionPanel.setSolutionBusiness(solutionBusiness);
		solutionPanel.setSolverAndPersistenceFrame(this);
		indictmentHeatMapTrueIcon = new ImageIcon(getClass().getResource(""));
		indictmentHeatMapFalseIcon = new ImageIcon(getClass().getResource(""));
		refreshScreenDuringSolvingTrueIcon = new ImageIcon(getClass().getResource(""));
		refreshScreenDuringSolvingFalseIcon = new ImageIcon(getClass().getResource(""));
		registerListeners();
		constraintMatchesDialog = new ConstraintMatchesDialog(this, solutionBusiness);
		solutionWasOpened = false;
	}

	private void createTimer() {
		notificationTimer = new Timer(300000, new TimerActionListener());
		activityTimer = new Timer(300000, new ActivityTimerActionListener());

		notificationTimer.start();
		activityTimer.start();
	}

	/**
	 * Event notification using a time listener.
	 */
	class TimerActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			// if the app isn't solving
			if (solveButton.isEnabled()) {

				ActivitySchedule solution = (ActivitySchedule) solutionBusiness.getSolution();

				// notify the user if there is a nearby activity by 15 minutes
				for (Activity activity : solution.getActivityList()) {
					if (activity.getActivityPeriod() != null) {
						if ((LocalDateTime.now().getDayOfWeek().getValue() - 1) == activity.getActivityPeriodWeekday()
								.getDayIndex()) {
							if (Utility.getNumberOfMinutesInInterval(
									new Time(LocalDateTime.now().getHour(), LocalDateTime.now().getMinute()),
									activity.getActivityPeriodTime()) == 15) {
								JOptionPane.showMessageDialog(null,
										activity.getActivityTypeCode() + " is in 15 minutes!", "Activity notification",
										JOptionPane.INFORMATION_MESSAGE);
							}
						}
					}
				}
			}

		}

	}

	class ActivityTimerActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			ActivitySchedule solution = (ActivitySchedule) solutionBusiness.getSolution();

			if (solution != null && solution.getScore().isSolutionInitialized() && solveButton.isEnabled()) {

				for (Activity activity : solution.getActivityList()) {
					if (activity.getActivityPeriod() != null && !activity.isPastActivity()
							&& Utility.isBeforeTheCurrentPeriod(activity.getActivityPeriod())) {

						solutionPanel.doProblemFactChange(scoreDirector -> {
							scoreDirector.beforeProblemPropertyChanged(activity);
							activity.setPastActivity(true);
							scoreDirector.afterProblemPropertyChanged(activity);

							scoreDirector.triggerVariableListeners();
						});

					}
				}

				resetScreen();

			}

		}

	}

	private void registerListeners() {
		solutionBusiness.registerForBestSolutionChanges(this);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// This async, so it doesn't stop the solving immediately
				solutionBusiness.terminateSolvingEarly();

				if (notificationTimer.isRunning())
					notificationTimer.stop();

				if (activityTimer.isRunning())
					activityTimer.stop();
			}
		});
	}

	public void bestSolutionChanged() {
		Solution_ solution = solutionBusiness.getSolution();
		Score score = solutionBusiness.getScore();
		if (refreshScreenDuringSolvingToggleButton.isSelected()) {
			solutionPanel.updatePanel(solution);
			validate(); // TODO remove me?
		}
		refreshScoreField(score);
	}

	public void init(Component centerForComponent) {
		setContentPane(createContentPane());
		pack();
		setLocationRelativeTo(centerForComponent);
		createTimer();
	}

	private JComponent createContentPane() {
		JComponent quickOpenPanel = createQuickOpenPanel();
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(createToolBar(), BorderLayout.NORTH);
		mainPanel.add(createMiddlePanel(), BorderLayout.CENTER);
		mainPanel.add(createScorePanel(), BorderLayout.SOUTH);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, quickOpenPanel, mainPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(0.2);
		return splitPane;
	}

	private JComponent createQuickOpenPanel() {
		JPanel quickOpenPanel = new JPanel(new BorderLayout());
		JLabel quickOpenLabel = new JLabel("Quick open");
		quickOpenLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		quickOpenPanel.add(quickOpenLabel, BorderLayout.NORTH);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createQuickOpenUnsolvedPanel(),
				createQuickOpenSolvedPanel());
		splitPane.setResizeWeight(0.8);
		splitPane.setBorder(null);
		quickOpenPanel.add(splitPane, BorderLayout.CENTER);
		return quickOpenPanel;
	}

	private JComponent createQuickOpenUnsolvedPanel() {
		quickOpenUnsolvedPanel = new JPanel();
		quickOpenUnsolvedActionList = new ArrayList<>();
		List<File> unsolvedFileList = solutionBusiness.getUnsolvedFileList();
		return createQuickOpenPanel(quickOpenUnsolvedPanel, "Unsolved dataset", quickOpenUnsolvedActionList,
				unsolvedFileList);
	}

	private JComponent createQuickOpenSolvedPanel() {
		quickOpenSolvedPanel = new JPanel();
		quickOpenSolvedActionList = new ArrayList<>();
		List<File> solvedFileList = solutionBusiness.getSolvedFileList();
		return createQuickOpenPanel(quickOpenSolvedPanel, "Solved dataset", quickOpenSolvedActionList, solvedFileList);
	}

	private JComponent createQuickOpenPanel(JPanel panel, String title, List<Action> quickOpenActionList,
			List<File> fileList) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		refreshQuickOpenPanel(panel, quickOpenActionList, fileList);
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(25);
		scrollPane.setMinimumSize(new Dimension(100, 80));
		// Size fits into screen resolution 1024*768
		scrollPane.setPreferredSize(new Dimension(180, 200));
		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.add(scrollPane, BorderLayout.CENTER);
		titlePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2),
				BorderFactory.createTitledBorder(title)));
		return titlePanel;
	}

	private void refreshQuickOpenPanel(JPanel panel, List<Action> quickOpenActionList, List<File> fileList) {
		panel.removeAll();
		quickOpenActionList.clear();
		if (fileList.isEmpty()) {
			JLabel noneLabel = new JLabel("None");
			noneLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			panel.add(noneLabel);
		} else {
			for (File file : fileList) {
				Action quickOpenAction = new QuickOpenAction(file);
				quickOpenActionList.add(quickOpenAction);
				JButton quickOpenButton = SwingUtils.makeSmallButton(new JButton(quickOpenAction));
				quickOpenButton.setHorizontalAlignment(SwingConstants.LEFT);
				panel.add(quickOpenButton);
			}
		}
	}

	/**
	 * Get the {@link ScoreParametrization} from file and update it in solution.
	 */
	private void setScoreParametrization() {
		solutionPanel.doProblemFactChange(scoreDirector -> {

			ActivitySchedule activitySchedule = (ActivitySchedule) solutionBusiness.getSolution();
			ScoreParametrization solutionScoreParametrization = activitySchedule.getScoreParametrization();

			ScoreParametrization scoreParametrization = Utility.getScoreParametrization(
					(ActivitySchedule) solutionBusiness.getSolution(),
					new File(new File(solutionBusiness.getUnsolvedDataDir().getParentFile(), ""),
							"Score parametrization" + ".xml"));

			scoreParametrization.setId(0L);

			if (solutionScoreParametrization != null) {
				scoreDirector.beforeProblemFactRemoved(solutionScoreParametrization);
				activitySchedule.setScoreParametrization(null);
				scoreDirector.afterProblemFactRemoved(solutionScoreParametrization);
			}

			scoreDirector.beforeProblemFactAdded(scoreParametrization);
			activitySchedule.setScoreParametrization(scoreParametrization);
			scoreDirector.afterProblemFactAdded(scoreParametrization);

			scoreDirector.triggerVariableListeners();

		});
	}

	private class QuickOpenAction extends AbstractAction {

		private File file;

		public QuickOpenAction(File file) {
			super(file.getName().replaceAll("\\.xml$", ""));
			this.file = file;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			try {
				solutionWasOpened = true;
				solutionBusiness.openSolution(file);
				setSolutionLoaded();

				resetValueRange();
				resetIsPastActivity();
				setScoreParametrization();
			} finally {
				setCursor(Cursor.getDefaultCursor());
			}
		}

	}

	private JComponent createToolBar() {
		// JToolBar looks ugly in Nimbus LookAndFeel
		JPanel toolBar = new JPanel();
		GroupLayout toolBarLayout = new GroupLayout(toolBar);
		toolBar.setLayout(toolBarLayout);

		openAction = new OpenAction();
		openAction.setEnabled(true);
		JButton openButton = new JButton(openAction);
		saveAction = new SaveAction();
		saveAction.setEnabled(false);
		JButton saveButton = new JButton(saveAction);

		progressBar = new JProgressBar(0, 100);

		JPanel solvePanel = new JPanel(new CardLayout());
		solveAction = new SolveAction();
		solveAction.setEnabled(false);
		solveButton = new JButton(solveAction);
		terminateSolvingEarlyAction = new TerminateSolvingEarlyAction();
		terminateSolvingEarlyAction.setEnabled(false);
		terminateSolvingEarlyButton = new JButton(terminateSolvingEarlyAction);
		terminateSolvingEarlyButton.setVisible(false);
		solvePanel.add(solveButton, "solveAction");
		solvePanel.add(terminateSolvingEarlyButton, "terminateSolvingEarlyAction");
		solveButton.setMinimumSize(terminateSolvingEarlyButton.getMinimumSize());
		solveButton.setPreferredSize(terminateSolvingEarlyButton.getPreferredSize());

		toolBarLayout.setHorizontalGroup(
				toolBarLayout.createSequentialGroup().addComponent(openButton).addComponent(saveButton).addGap(10)
						.addComponent(solvePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(progressBar, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
		toolBarLayout.setVerticalGroup(toolBarLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(openButton).addComponent(saveButton).addComponent(solvePanel).addComponent(progressBar));
		return toolBar;
	}

	private class SolveAction extends AbstractAction {

		public SolveAction() {
			super("Solve", new ImageIcon(SolverAndPersistenceFrame.class.getResource("")));
		}

		// *********************SOLUTION SOLVER********************
		@Override
		public void actionPerformed(ActionEvent e) {
			setSolvingState(true);
			Solution_ planningProblem = solutionBusiness.getSolution();
			new SolveWorker(planningProblem).execute();
		}

	}

	/**
	 * Start a new solving(when reschedule)
	 */
	public void startSolveAction() {
		if (solveButton.isEnabled()) {
			setSolvingState(true);
			Solution_ planningProblem = solutionBusiness.getSolution();
			new SolveWorker(planningProblem).execute();
		}
	}

	protected class SolveWorker extends SwingWorker<Solution_, Void> {

		protected final Solution_ planningProblem;

		public SolveWorker(Solution_ planningProblem) {
			this.planningProblem = planningProblem;
		}

		@Override
		protected Solution_ doInBackground() throws Exception {
			return solutionBusiness.solve(planningProblem);
		}

		@Override
		protected void done() {
			try {
				Solution_ bestSolution = get();
				solutionBusiness.setSolution(bestSolution);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new IllegalStateException("Solving was interrupted.", e);
			} catch (ExecutionException e) {
				throw new IllegalStateException("Solving failed.", e.getCause());
			} finally {
				setSolvingState(false);
				resetScreen();
			}
		}

	}

	private class TerminateSolvingEarlyAction extends AbstractAction {

		public TerminateSolvingEarlyAction() {
			super("Terminate solving early", new ImageIcon(SolverAndPersistenceFrame.class.getResource("")));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			terminateSolvingEarlyAction.setEnabled(false);
			progressBar.setString("Terminating...");
			// This async, so it doesn't stop the solving immediately
			solutionBusiness.terminateSolvingEarly();
		}

	}

	private class OpenAction extends AbstractAction {

		private static final String NAME = "Open...";
		private JFileChooser fileChooser;

		public OpenAction() {
			super(NAME, new ImageIcon(SolverAndPersistenceFrame.class.getResource("")));

			solutionWasOpened = true;
			fileChooser = new JFileChooser(solutionBusiness.getSolvedDataDir());
			fileChooser.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.isDirectory() || file.getName().endsWith(".xml");
				}

				@Override
				public String getDescription() {
					return "Solution XStream XML files";
				}
			});
			fileChooser.setDialogTitle(NAME);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int approved = fileChooser.showOpenDialog(SolverAndPersistenceFrame.this);
			if (approved == JFileChooser.APPROVE_OPTION) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				try {
					solutionBusiness.openSolution(fileChooser.getSelectedFile());
					setSolutionLoaded();

					resetValueRange();
					resetIsPastActivity();
					setScoreParametrization();
				} finally {
					setCursor(Cursor.getDefaultCursor());
				}
			}
		}

	}

	/**
	 * When a new schedule is opened from file, reset all the activity domain value
	 * ranges. In real time rescheduling, activities after the current time will
	 * have a more restricted value range, and activities before the current time
	 * will be immovable.
	 */
	protected void resetValueRange() {

		ActivitySchedule activitySchedule = (ActivitySchedule) solutionBusiness.getSolution();

		solutionPanel.doProblemFactChange(scoreDirector -> {
			for (Activity activity : activitySchedule.getActivityList()) {
				if (activity instanceof NormalActivity) {
					scoreDirector.beforeProblemPropertyChanged(activity);
					((NormalActivity) activity).setPeriodDomainRangeList(activitySchedule.getActivityPeriodList());
					scoreDirector.afterProblemPropertyChanged(activity);
				}
			}
		});
	}

	protected void resetIsPastActivity() {

		ActivitySchedule activitySchedule = (ActivitySchedule) solutionBusiness.getSolution();

		solutionPanel.doProblemFactChange(scoreDirector -> {
			for (Activity activity : activitySchedule.getActivityList()) {

				scoreDirector.beforeProblemPropertyChanged(activity);
				activity.setPastActivity(false);
				scoreDirector.afterProblemPropertyChanged(activity);

			}
		});
	}

	private class SaveAction extends AbstractAction {

		private static final String NAME = "Save as...";
		private JFileChooser fileChooser;

		public SaveAction() {
			super(NAME, new ImageIcon(SolverAndPersistenceFrame.class.getResource("")));
			fileChooser = new JFileChooser(solutionBusiness.getSolvedDataDir());
			fileChooser.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.isDirectory() || file.getName().endsWith(".xml");
				}

				@Override
				public String getDescription() {
					return "Solution XStream XML files";
				}
			});
			fileChooser.setDialogTitle(NAME);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			fileChooser.setSelectedFile(new File(solutionBusiness.getSolvedDataDir(),
					FilenameUtils.getBaseName(solutionBusiness.getSolutionFileName()) + ".xml"));
			int approved = fileChooser.showSaveDialog(SolverAndPersistenceFrame.this);
			if (approved == JFileChooser.APPROVE_OPTION) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				try {
					setScoreParametrization();
					solutionBusiness.saveSolution(fileChooser.getSelectedFile());
				} finally {
					setCursor(Cursor.getDefaultCursor());
				}
				refreshQuickOpenPanel(quickOpenUnsolvedPanel, quickOpenUnsolvedActionList,
						solutionBusiness.getUnsolvedFileList());
				refreshQuickOpenPanel(quickOpenSolvedPanel, quickOpenSolvedActionList,
						solutionBusiness.getSolvedFileList());
				SolverAndPersistenceFrame.this.validate();
			}
		}

	}

	private JPanel createMiddlePanel() {
		middlePanel = new JPanel(new CardLayout());
		JPanel usageExplanationPanel = new JPanel(new BorderLayout(5, 5));
		// ImageIcon usageExplanationIcon = new
		// ImageIcon(getClass().getResource(solutionPanel.getUsageExplanationPath()));
		// JLabel usageExplanationLabel = new JLabel(usageExplanationIcon);
		// Allow splitPane divider to be moved to the right
		// usageExplanationLabel.setMinimumSize(new Dimension(100, 100));
		// usageExplanationPanel.add(usageExplanationLabel,
		// BorderLayout.CENTER);
		JPanel descriptionPanel = new JPanel(new BorderLayout(2, 2));
		descriptionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		descriptionPanel.add(new JLabel("Example description"), BorderLayout.NORTH);
		JTextArea descriptionTextArea = new JTextArea(8, 70);
		descriptionTextArea.setEditable(false);
		descriptionTextArea.setText(solutionBusiness.getAppDescription());
		descriptionPanel.add(new JScrollPane(descriptionTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		usageExplanationPanel.add(descriptionPanel, BorderLayout.SOUTH);
		middlePanel.add(usageExplanationPanel, "usageExplanationPanel");
		JComponent wrappedSolutionPanel;
		if (solutionPanel.isWrapInScrollPane()) {
			wrappedSolutionPanel = new JScrollPane(solutionPanel);
		} else {
			wrappedSolutionPanel = solutionPanel;
		}
		middlePanel.add(wrappedSolutionPanel, "solutionPanel");
		return middlePanel;
	}

	private JPanel createScorePanel() {
		JPanel scorePanel = new JPanel(new BorderLayout(5, 0));
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		showConstraintMatchesDialogAction = new ShowConstraintMatchesDialogAction();
		showConstraintMatchesDialogAction.setEnabled(false);
		buttonPanel.add(new JButton(showConstraintMatchesDialogAction));
		indictmentHeatMapToggleButton = new JToggleButton(
				solutionPanel.isUseIndictmentColor() ? indictmentHeatMapTrueIcon : indictmentHeatMapFalseIcon,
				solutionPanel.isUseIndictmentColor());
		indictmentHeatMapToggleButton.setEnabled(false);
		indictmentHeatMapToggleButton.setToolTipText("Show indictment heat map");
		indictmentHeatMapToggleButton.addActionListener(e -> {
			boolean selected = indictmentHeatMapToggleButton.isSelected();
			indictmentHeatMapToggleButton.setIcon(selected ? indictmentHeatMapTrueIcon : indictmentHeatMapFalseIcon);
			solutionPanel.setUseIndictmentColor(selected);
			resetScreen();
		});
		buttonPanel.add(indictmentHeatMapToggleButton);
		scorePanel.add(buttonPanel, BorderLayout.WEST);
		scoreField = new JTextField("Score:");
		scoreField.setEditable(false);
		scoreField.setForeground(Color.BLACK);
		scoreField.setBorder(BorderFactory.createLoweredBevelBorder());
		scorePanel.add(scoreField, BorderLayout.CENTER);
		refreshScreenDuringSolvingToggleButton = new JToggleButton(refreshScreenDuringSolvingTrueIcon, true);
		refreshScreenDuringSolvingToggleButton.setToolTipText("Refresh screen during solving");
		refreshScreenDuringSolvingToggleButton.addActionListener(e -> {
			refreshScreenDuringSolvingToggleButton
					.setIcon(refreshScreenDuringSolvingToggleButton.isSelected() ? refreshScreenDuringSolvingTrueIcon
							: refreshScreenDuringSolvingFalseIcon);
		});
		scorePanel.add(refreshScreenDuringSolvingToggleButton, BorderLayout.EAST);
		return scorePanel;
	}

	private class ShowConstraintMatchesDialogAction extends AbstractAction {

		public ShowConstraintMatchesDialogAction() {
			super("Constraint matches", new ImageIcon(SolverAndPersistenceFrame.class.getResource("")));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			constraintMatchesDialog.resetContentPanel();
			constraintMatchesDialog.setVisible(true);
		}

	}

	private void setSolutionLoaded() {
		setTitle(solutionBusiness.getAppName() + " - " + solutionBusiness.getSolutionFileName());
		((CardLayout) middlePanel.getLayout()).show(middlePanel, "solutionPanel");
		setSolvingState(false);
		resetScreen();
	}

	private void setSolvingState(boolean solving) {
		for (Action action : quickOpenUnsolvedActionList) {
			action.setEnabled(!solving);
		}
		for (Action action : quickOpenSolvedActionList) {
			action.setEnabled(!solving);
		}
		openAction.setEnabled(!solving);
		saveAction.setEnabled(!solving);
		solveAction.setEnabled(!solving);
		solveButton.setVisible(!solving);
		terminateSolvingEarlyAction.setEnabled(solving);
		terminateSolvingEarlyButton.setVisible(solving);

		if (solving) {
			terminateSolvingEarlyButton.requestFocus();
		} else {
			solveButton.requestFocus();

			setScoreParametrization();

			boolean postponeFound = false;

			ActivitySchedule solution = (ActivitySchedule) solutionBusiness.getSolution();

			for (Activity activity : solution.getActivityList()) {
				if (activity.getPostpone() != null) {
					postponeFound = true;
					break;
				}
			}

			// notify that the best solution was found
			if (postponeFound || !solutionWasOpened)
				JOptionPane.showMessageDialog(null, "This is the best solution found.", "Solver notification",
						JOptionPane.INFORMATION_MESSAGE);

			solutionWasOpened = false;
		}

		solutionPanel.setEnabled(!solving);
		progressBar.setIndeterminate(solving);
		progressBar.setStringPainted(solving);
		progressBar.setString(solving ? "Solving..." : null);
		indictmentHeatMapToggleButton.setEnabled(solutionPanel.isIndictmentHeatMapEnabled() && !solving);
		showConstraintMatchesDialogAction.setEnabled(!solving);
	}

	public void resetScreen() {
		Solution_ solution = solutionBusiness.getSolution();
		Score score = solutionBusiness.getScore();
		solutionPanel.resetPanel(solution);
		setScoreParametrization();
		validate();
		refreshScoreField(score);
	}

	public void refreshScoreField(Score score) {
		scoreField.setForeground(determineScoreFieldForeground(score));
		scoreField.setText("Latest best score: " + score);
	}

	private Color determineScoreFieldForeground(Score<?> score) {
		if (!score.isSolutionInitialized()) {
			return TangoColorFactory.SCARLET_3;
		} else if (!(score instanceof FeasibilityScore)) {
			return Color.BLACK;
		} else {
			FeasibilityScore<?> feasibilityScore = (FeasibilityScore<?>) score;
			return feasibilityScore.isFeasible() ? TangoColorFactory.CHAMELEON_3 : TangoColorFactory.ORANGE_3;
		}
	}

}
