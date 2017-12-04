package org.aimas.cami.scheduler.CAMIScheduler.swingui;

import static org.aimas.cami.scheduler.CAMIScheduler.swingui.TimeTablePanel.HeaderColumnKey.HEADER_COLUMN_GROUP1;
import static org.aimas.cami.scheduler.CAMIScheduler.swingui.TimeTablePanel.HeaderRowKey.HEADER_ROW;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityCategory;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivityType;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Difficulty;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ExcludedTimePeriodsPenalty;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NewActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NormalActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NormalRelativeActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.PeriodInterval;
import org.aimas.cami.scheduler.CAMIScheduler.domain.RelativeActivityPenalty;
import org.aimas.cami.scheduler.CAMIScheduler.domain.RelativeType;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ScoreParametrization;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Time;
import org.aimas.cami.scheduler.CAMIScheduler.domain.TimeInterval;
import org.aimas.cami.scheduler.CAMIScheduler.domain.WeekDay;
import org.aimas.cami.scheduler.CAMIScheduler.postpone.Postpone;
import org.aimas.cami.scheduler.CAMIScheduler.postpone.PostponeType;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AdjustActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.utils.Utility;
import org.optaplanner.swing.impl.SwingUtils;
import org.optaplanner.swing.impl.TangoColorFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

/**
 * Class that implements your personalized GUI panel.
 * 
 * @author Bogdan
 *
 */
public class CAMITaskSchedulerPanel extends SolutionPanel<ActivitySchedule> {

	// map table that puts an object to a (x, y) coordinate
	private final TimeTablePanel<WeekDay, Time> schedulePanel;

	// "add a new activity" panel
	private JPanel addActivityListPanel;
	private final JPanel addActivityPanel;
	private final JPanel addActivityFromXMLPanel;
	private final JPanel dropDownPanel;

	// save the "original" time objects in these maps to avoid hash exceptions from
	// TimeTablePanel
	private Map<Integer, Time> timeMap;
	private Map<Integer, WeekDay> weekDayMap;

	private JButton addActivityButton;
	private JButton addActivityFromXmlButton;

	private JComboBox<String> activityListDropDown;
	private JPopupMenu popupMenu;
	private AbstractButton arrowButton;
	private boolean dropdownCreated;

	// serialization id
	private long postponeId;

	private ScoreParametrizationDialog scoreParametrizationDialog;
	private AbstractAction scoreParametrizationAction;

	public CAMITaskSchedulerPanel() {

		setLayout(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		schedulePanel = new TimeTablePanel<>();

		addActivityListPanel = new JPanel();
		GroupLayout toolBarLayout = new GroupLayout(addActivityListPanel);
		addActivityListPanel.setLayout(toolBarLayout);

		addActivityPanel = new JPanel();
		addActivityFromXMLPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

		tabbedPane.add("Week Schedule", new JScrollPane(schedulePanel));

		// add the panels to the frame
		add(tabbedPane, BorderLayout.CENTER);
		add(createScoreParametrizationPanel(), BorderLayout.SOUTH);
		add(addActivityListPanel, BorderLayout.NORTH);

		setPreferredSize(PREFERRED_SCROLLABLE_VIEWPORT_SIZE);

		createAddActivityButton();
		createAddActivityFromXMLButton(new File("data\\activityschedule\\", "New Activity" + ".xml"));

		dropDownPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

		toolBarLayout.setHorizontalGroup(toolBarLayout.createSequentialGroup()
				.addComponent(addActivityPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
						GroupLayout.PREFERRED_SIZE)
				.addComponent(addActivityFromXMLPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
						GroupLayout.PREFERRED_SIZE)
				.addComponent(dropDownPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
						GroupLayout.PREFERRED_SIZE));
		toolBarLayout.setVerticalGroup(toolBarLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(addActivityPanel).addComponent(addActivityFromXMLPanel).addComponent(dropDownPanel));

		// initialize maps and id
		timeMap = new HashMap<>();
		weekDayMap = new HashMap<>();
		postponeId = 0L;

		dropdownCreated = false;
	}

	/**
	 * Create a {@link ScoreParametrization} panel using the
	 * {@link ScoreParametrizationDialog} created
	 * 
	 */
	private JPanel createScoreParametrizationPanel() {
		JPanel scoreParametrizationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		scoreParametrizationAction = new AbstractAction("Edit scoring parameters and preferences") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// not safe to change score calculation penalties/preferences during solving
				if (solutionBusiness.isSolving()) {
					JOptionPane.showMessageDialog(CAMITaskSchedulerPanel.this.getTopLevelAncestor(),
							"The GUI does not support this action during solving.\n"
									+ "\nTerminate solving first and try again.",
							"Unsupported in GUI", JOptionPane.ERROR_MESSAGE);
					return;
				}

				scoreParametrizationDialog.setScoreParametrization(getSolution().getScoreParametrization());
				scoreParametrizationDialog.setVisible(true);

			}
		};

		scoreParametrizationAction.setEnabled(false);
		scoreParametrizationPanel.add(new JButton(scoreParametrizationAction));

		return scoreParametrizationPanel;
	}

	@Override
	public void setSolverAndPersistenceFrame(SolverAndPersistenceFrame solverAndPersistenceFrame) {
		super.setSolverAndPersistenceFrame(solverAndPersistenceFrame);
		scoreParametrizationDialog = new ScoreParametrizationDialog(solverAndPersistenceFrame, this);
	}

	@Override
	public boolean isWrapInScrollPane() {
		return false;
	}

	/**
	 * Reset panel components and redraw them every time a change in best solution
	 * has occurred
	 */
	@Override
	public void resetPanel(ActivitySchedule activitySchedule) {
		schedulePanel.reset();
		defineGrid(activitySchedule);
		fillCells(activitySchedule);
		createDropDown();
		scoreParametrizationAction.setEnabled(true);
		repaint();
	}

	private void createAddActivityButton() {
		addActivityButton = SwingUtils.makeSmallButton(new JButton(new AddActivityOptionAction()));
		addActivityButton.setToolTipText("Add a new activity to the schedule");
		addActivityButton.setPreferredSize(new Dimension(110, 25));
		addActivityButton.setFont(new Font("TIMES NEW ROMAN", Font.BOLD, 16));
		addActivityPanel.add(addActivityButton);
	}

	private void createAddActivityFromXMLButton(File inputFile) {
		addActivityFromXmlButton = SwingUtils.makeSmallButton(new JButton("Add activity from XML"));
		addActivityFromXmlButton.setToolTipText("Add a new activity from XML to the schedule");
		addActivityFromXmlButton.setPreferredSize(new Dimension(180, 25));
		addActivityFromXmlButton.setFont(new Font("TIMES NEW ROMAN", Font.BOLD, 16));
		addActivityFromXmlButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// deserialize the new activity added
				XStream xStream = new XStream();
				xStream.alias("NewActivity", NewActivity.class);
				xStream.setMode(XStream.ID_REFERENCES);
				xStream.autodetectAnnotations(true);

				try (Reader reader = new InputStreamReader(new FileInputStream(inputFile), "UTF-8")) {

					// get its properties
					NewActivity na = (NewActivity) xStream.fromXML(reader);
					Activity newActivity = na.getActivity();
					ExcludedTimePeriodsPenalty excludedTimePeriodsPenalty = na.getExcludedTimePeriodsPenalty();
					RelativeActivityPenalty relativeActivityPenalty = na.getRelativeActivityPenalty();

					ActivitySchedule activitySchedule = getSolution();

					if (newActivity instanceof NormalActivity) {

						doProblemFactChange(scoreDirector -> {

							List<Activity> activityList = new ArrayList<>(activitySchedule.getActivityList());
							activitySchedule.setActivityList(activityList);

							List<ActivityType> activityTypeList = new ArrayList<>(
									activitySchedule.getActivityTypeList());
							activitySchedule.setActivityTypeList(activityTypeList);

							// get activity instances
							int instances = 1;
							if (newActivity.getActivityType().getInstancesPerDay() != 0)
								instances = newActivity.getActivityType().getInstancesPerDay() * 7;
							else if (newActivity.getActivityType().getInstancesPerWeek() != 0)
								instances = newActivity.getActivityType().getInstancesPerWeek();

							// set activity type id
							newActivity.getActivityType()
									.setId(activityTypeList.get(activityTypeList.size() - 1).getId() + 1);

							// create instances of this type of activity
							for (int i = 0; i < instances; i++) {
								NormalActivity activity = new NormalActivity();

								activity.setActivityType(newActivity.getActivityType());
								activity.setImmovable(newActivity.isImmovable());
								activity.setOnDropdown(true);
								activity.setPeriodDomainRangeList(activitySchedule.getActivityPeriodList());
								activity.setId(activityList.get(activityList.size() - 1).getId() + 1);

								scoreDirector.beforeEntityAdded(activity);
								activityList.add(activity);
								scoreDirector.afterEntityAdded(activity);

								// if its period is imposed
								if (activity.getActivityType().getImposedPeriod() != null) {
									// scoreDirector.beforeVariableChanged(activity, "activityPeriod");
									// activity.setActivityPeriod(activity.getActivityType().getImposedPeriod());
									// scoreDirector.afterVariableChanged(activity, "activityPeriod");

									activity.setImmovable(true);
								}
							}

							scoreDirector.beforeProblemFactAdded(newActivity.getActivityType());
							activityTypeList.add(newActivity.getActivityType());
							scoreDirector.afterProblemFactAdded(newActivity.getActivityType());

							scoreDirector.triggerVariableListeners();

						});
					} else if (newActivity instanceof NormalRelativeActivity) {

						if (relativeActivityPenalty != null) {
							doProblemFactChange(scoreDirector -> {
								List<Activity> activityList = new ArrayList<>(activitySchedule.getActivityList());
								activitySchedule.setActivityList(activityList);

								List<ActivityType> activityTypeList = new ArrayList<>(
										activitySchedule.getActivityTypeList());
								activitySchedule.setActivityTypeList(activityTypeList);

								List<RelativeActivityPenalty> relativeActivityPenaltyList = new ArrayList<>(
										activitySchedule.getRelativeActivityPenaltyList());
								activitySchedule.setRelativeActivityPenaltyList(relativeActivityPenaltyList);

								newActivity.getActivityType()
										.setId(activityTypeList.get(activityTypeList.size() - 1).getId() + 1);

								// if the activity is relative to a specific activity
								if (relativeActivityPenalty.getNormalActivityType() != null) {

									int instances = 1;

									// get activity instances(activity to which newActivity is relative)
									for (ActivityType activityType : activityTypeList) {
										if (activityType.getCode()
												.equals(relativeActivityPenalty.getNormalActivityType())) {

											if (activityType.getInstancesPerDay() != 0) {
												instances = activityType.getInstancesPerDay() * 7;
												newActivity.getActivityType().setInstancesPerDay(instances / 7);
											} else if (activityType.getInstancesPerWeek() != 0) {
												instances = activityType.getInstancesPerWeek();
												newActivity.getActivityType().setInstancesPerWeek(instances);
											}

											break;
										}
									}

									// create instances of this type of activity
									for (int i = 0; i < instances; i++) {
										NormalRelativeActivity relativeActivity = new NormalRelativeActivity();
										relativeActivity.setActivityType(newActivity.getActivityType());
										relativeActivity.setOffset(((NormalRelativeActivity) newActivity).getOffset()
												* getRelativeTypeSign(relativeActivityPenalty.getRelativeType()));
										relativeActivity.setImmovable(newActivity.isImmovable());
										relativeActivity.setOnDropdown(true);
										relativeActivity.setId(activityList.get(activityList.size() - 1).getId() + 1);

										scoreDirector.beforeEntityAdded(relativeActivity);
										activityList.add(relativeActivity);
										scoreDirector.afterEntityAdded(relativeActivity);
									}

								} else if (relativeActivityPenalty.getCategory() != null) { // else if this activity is
																							// relative to a category of
																							// activities

									int instances = 1;

									// get the imposed instances
									if (newActivity.getActivityType().getInstancesPerDay() != 0) {
										instances = newActivity.getActivityType().getInstancesPerDay() * 7;
									} else if (newActivity.getActivityType().getInstancesPerWeek() != 0) {
										instances = newActivity.getActivityType().getInstancesPerWeek();
									}

									// create instances of this type of activity
									for (int i = 0; i < instances; i++) {
										NormalRelativeActivity relativeActivity = new NormalRelativeActivity();
										relativeActivity.setActivityType(newActivity.getActivityType());
										relativeActivity.setOffset(((NormalRelativeActivity) newActivity).getOffset()
												* getRelativeTypeSign(relativeActivityPenalty.getRelativeType()));
										relativeActivity.setImmovable(newActivity.isImmovable());
										relativeActivity.setOnDropdown(true);
										relativeActivity.setId(activityList.get(activityList.size() - 1).getId() + 1);

										scoreDirector.beforeEntityAdded(relativeActivity);
										activityList.add(relativeActivity);
										scoreDirector.afterEntityAdded(relativeActivity);
									}

								}

								// add relativeActivityPenalty fact to the solution
								relativeActivityPenalty.setId(
										relativeActivityPenaltyList.get(relativeActivityPenaltyList.size() - 1).getId()
												+ 1);

								scoreDirector.beforeProblemFactAdded(relativeActivityPenalty);
								relativeActivityPenaltyList.add(relativeActivityPenalty);
								scoreDirector.afterProblemFactAdded(relativeActivityPenalty);

								scoreDirector.beforeProblemFactAdded(newActivity.getActivityType());
								activityTypeList.add(newActivity.getActivityType());
								scoreDirector.afterProblemFactAdded(newActivity.getActivityType());

								// *****trigger the listener*****
								// so the relative activity new created has its period set
								if (relativeActivityPenalty.getNormalActivityType() != null) {

									triggerListener(activityList, relativeActivityPenalty.getNormalActivityType(),
											null);

								} else if ((relativeActivityPenalty.getCategory() != null)) {

									triggerListener(activityList, null, relativeActivityPenalty.getCategory());

								}

								scoreDirector.triggerVariableListeners();
							});
						}

					}

					// add excludedTimePeriodsPenalty to the solution
					if (excludedTimePeriodsPenalty != null) {

						List<ExcludedTimePeriodsPenalty> excludedTimePeriodsPenaltyList = new ArrayList<>(
								activitySchedule.getExcludedTimePeriodsList());
						activitySchedule.setExcludedTimePeriodsList(excludedTimePeriodsPenaltyList);

						doProblemFactChange(scoreDirector -> {
							excludedTimePeriodsPenalty.setId(excludedTimePeriodsPenaltyList
									.get(excludedTimePeriodsPenaltyList.size() - 1).getId() + 1);

							scoreDirector.beforeProblemFactAdded(excludedTimePeriodsPenalty);
							excludedTimePeriodsPenaltyList.add(excludedTimePeriodsPenalty);
							scoreDirector.afterProblemFactAdded(excludedTimePeriodsPenalty);

							scoreDirector.triggerVariableListeners();
						});

					}

					solverAndPersistenceFrame.startSolveAction();

				} catch (XStreamException | IOException exception) {
					throw new IllegalArgumentException("Failed reading inputSolutionFile (" + inputFile + ").",
							exception);
				}

				solverAndPersistenceFrame.resetScreen();
			}
		});
		addActivityFromXMLPanel.add(addActivityFromXmlButton);
	}

	private Set<String> filterDropDownActivities(List<Activity> activityList) {

		Set<String> activitySet = new HashSet<>();

		// System.out.println("========================================================");

		for (Activity activity : activityList) {

			if (activity.isOnDropdown() || activity.isWantedToBePlanned())
				activitySet.add(activity.getActivityTypeCode());

		}

		return activitySet;

	}

	private String getActivityCategory(String activityName, List<Activity> activityList) {

		for (Activity activity : activityList) {
			if (activity.getActivityTypeCode().equals(activityName))
				return Utility.getActivityCategory(activity);
		}

		return null;
	}

	private boolean isActivityPlanned(String activityName, List<Activity> activityList) {

		boolean allInstancesPlanned = true;
		for (Activity activity : activityList) {
			if (activity.getActivityTypeCode().equals(activityName))
				if (!activity.isWantedToBePlanned()) {
					allInstancesPlanned = false;
					break;
				} else if (activity.isWantedToBePlanned() && activity.getActivityPeriod() == null) {
					allInstancesPlanned = false;
					break;
				}
		}
		return allInstancesPlanned;
	}

	private RelativeActivityPenalty getRelativeDependence(String activityName,
			List<RelativeActivityPenalty> relativeActivityPenaltyList) {

		for (RelativeActivityPenalty rap : relativeActivityPenaltyList) {
			if (rap.getRelativeActivityType().equals(activityName))
				return rap;

		}

		return null;
	}

	private JMenuItem createMenuItem(final String activityCode) {
		JMenuItem item = new JMenuItem(activityCode);

		ActivitySchedule activitySchedule = getSolution();

		if (isActivityPlanned(activityCode, activitySchedule.getActivityList())) {
			item.setEnabled(false);
		}

		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// System.out.println("Ai selectat -> " + activityCode);

				// set "wantedToBePlanned" to true for each activityCode instance
				doProblemFactChange(scoreDirector -> {
					for (Activity activity : activitySchedule.getActivityList()) {
						if (activity.getActivityTypeCode().equals(activityCode)) {

							Activity workingActivity = scoreDirector.lookUpWorkingObject(activity);

							scoreDirector.beforeProblemPropertyChanged(workingActivity);
							workingActivity.setWantedToBePlanned(true);
							scoreDirector.afterProblemPropertyChanged(workingActivity);

							if (workingActivity.getImposedPeriod() != null) {
								scoreDirector.beforeVariableChanged(((NormalActivity) workingActivity),
										"activityPeriod");
								((NormalActivity) workingActivity)
										.setActivityPeriod(((NormalActivity) workingActivity).getImposedPeriod());
								scoreDirector.afterVariableChanged(((NormalActivity) workingActivity),
										"activityPeriod");
							}

							if (workingActivity instanceof NormalRelativeActivity) {
								RelativeActivityPenalty rap = getRelativeDependence(
										workingActivity.getActivityTypeCode(),
										activitySchedule.getRelativeActivityPenaltyList());

								// *****trigger the listener*****
								if (rap.getNormalActivityType() != null) {

									triggerListener(activitySchedule.getActivityList(), rap.getNormalActivityType(),
											null);

								} else if (rap.getCategory() != null) {

									triggerListener(activitySchedule.getActivityList(), null, rap.getCategory());
								}
							}

						}
					}
					scoreDirector.triggerVariableListeners();
					solverAndPersistenceFrame.startSolveAction();
				});

				solverAndPersistenceFrame.resetScreen();
			}
		});
		return item;
	}

	private void setPopupVisible(JPopupMenu popupMenu, boolean visible) {
		if (visible) {
			popupMenu.show(activityListDropDown, 0, activityListDropDown.getSize().height);
		} else {
			popupMenu.setVisible(false);
		}
	}

	class MyComboBoxRenderer extends JLabel implements ListCellRenderer {
		private String _title;

		public MyComboBoxRenderer(String title) {
			_title = title;
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean hasFocus) {
			if (index == -1 && value == null)
				setText(_title);
			else
				setText(value.toString());
			return this;
		}
	}

	protected void createDropDown() {
		ActivitySchedule activitySchedule = getSolution();

		Map<String, ArrayList<String>> menuData = new HashMap<>();

		if (activitySchedule != null) {

			popupMenu = new JPopupMenu();
			popupMenu.setBorder(new MatteBorder(1, 1, 1, 1, Color.CYAN));

			for (ActivityCategory activityCategory : activitySchedule.getActivityCategoryList()) {
				menuData.put(Utility.getCategory(activityCategory.getCode()), new ArrayList<>());
			}

			Set<String> activitySet = filterDropDownActivities(activitySchedule.getActivityList());

			for (String activity : activitySet) {
				menuData.get(getActivityCategory(activity, activitySchedule.getActivityList())).add(activity);
			}

			List<String> categories = new ArrayList<String>(menuData.keySet());
			Collections.sort(categories);

			for (String category : categories) {
				JMenu menu = new JMenu(category);
				for (String itemName : menuData.get(category)) {
					menu.add(createMenuItem(itemName));
				}
				popupMenu.add(menu);
			}

			activityListDropDown = new JComboBox<String>();
			activityListDropDown.setPrototypeDisplayValue("123456789012345678901234567890");

			for (Component comp : activityListDropDown.getComponents()) {
				if (comp instanceof AbstractButton) {
					arrowButton = (AbstractButton) comp;
				}
			}

			arrowButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					setPopupVisible(popupMenu, !popupMenu.isVisible());
				}
			});

			activityListDropDown.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					setPopupVisible(popupMenu, !popupMenu.isVisible());
				}
			});

			activityListDropDown.setRenderer(new MyComboBoxRenderer("Select an activity to plan"));
			activityListDropDown.setSelectedIndex(-1);
		}

		dropDownPanel.removeAll();
		dropDownPanel.add(activityListDropDown);
	}

	/**
	 * Find all activities with their name equals to normalActivityType and reset
	 * their period
	 * 
	 */
	private void triggerListener(List<Activity> activityList, String normalActivityType, String category) {

		doProblemFactChange(scoreDirector -> {
			if (normalActivityType != null) {

				for (Activity activity : activityList) {
					if (activity instanceof NormalActivity) {
						if (((NormalActivity) activity).getActivityTypeCode().equals(normalActivityType)) {

							scoreDirector.beforeVariableChanged(activity, "activityPeriod");
							((NormalActivity) activity).setActivityPeriod(activity.getActivityPeriod());
							scoreDirector.afterVariableChanged(activity, "activityPeriod");
						}
					}
				}

			} else if ((category != null)) {

				for (Activity activity : activityList) {
					if (activity instanceof NormalActivity) {
						if (((NormalActivity) activity).getActivityCategory() != null
								&& ((NormalActivity) activity).getActivityCategory().getCode().equals(category)) {

							scoreDirector.beforeVariableChanged(activity, "activityPeriod");
							((NormalActivity) activity).setActivityPeriod(activity.getActivityPeriod());
							scoreDirector.afterVariableChanged(activity, "activityPeriod");
						}
					}
				}

			}

			scoreDirector.triggerVariableListeners();
		});
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

	/**
	 * Fill all cells and assign them to a header column/row on the table.
	 */
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

		// schedulePanel.addColumnHeader(null, HEADER_ROW,
		// createTableHeader(new JLabel("Unassigned", SwingConstants.CENTER)));

	}

	private void fillTimeCells(ActivitySchedule activitySchedule) {
		for (Time time : activitySchedule.getTimeList()) {
			if (time.getMinutes() == 0)
				schedulePanel.addRowHeader(HEADER_COLUMN_GROUP1, time, createTableHeader(new JLabel(time.getLabel())));
		}

		// schedulePanel.addRowHeader(HEADER_COLUMN_GROUP1, null, createTableHeader(new
		// JLabel("Unassigned")));
	}

	private void fillActivityCells(ActivitySchedule activitySchedule) {
		preparePlanningEntityColors(activitySchedule.getActivityList());
		for (Activity activity : activitySchedule.getActivityList()) {
			Color color = determinePlanningEntityColor(activity, activity.getActivityType());
			String toolTip = determinePlanningEntityTooltip(activity);

			if (activity.getActivityPeriod() != null) {
				schedulePanel.addCell(weekDayMap.get(activity.getActivityPeriodWeekday().getDayIndex()),
						timeMap.get(activity.getActivityPeriodTime().getHour()),
						createButton(activity, color, toolTip));
			} /*
				 * else // unassigned schedulePanel.addCell(null, null, createButton(activity,
				 * color, toolTip));
				 */
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
		JButton button = SwingUtils.makeSmallButton(new JButton(new ActivityOptionAction(activity)));
		button.setBackground(color);

		if (activity.isImmovable()) {
			button.setIcon(new ImageIcon("immovable.png"));
		}

		if (activity.getActivityPeriod() == null)
			plannedTime = "";
		else
			plannedTime = activity.getActivityPeriod().getLabel() + " - "
					+ activity.getActivityEndPeriod().getTime().getLabel();

		// text displayed when you hover the buton
		button.setToolTipText("<html>" + activity.getActivityTypeCode() + ": " + activity.getId() + "<br/><br/>"
				+ plannedTime + "<br/><br/>" + toolTip.substring(6));
		return button;
	}

	@Override
	public boolean isIndictmentHeatMapEnabled() {
		return true;
	}

	private void triggerInstancesListeners(JTextField instancesPerDayField, JTextField instancesPerWeekField) {

		instancesPerDayField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				actionPerformed();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				actionPerformed();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				actionPerformed();
			}

			public void actionPerformed() {
				if (!instancesPerDayField.getText().equals("")) {
					instancesPerWeekField.setEnabled(false);
					instancesPerWeekField.setText("");
				} else
					instancesPerWeekField.setEnabled(true);

			}
		});

		instancesPerWeekField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				actionPerformed();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				actionPerformed();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				actionPerformed();
			}

			public void actionPerformed() {
				if (!instancesPerWeekField.getText().equals("")) {
					instancesPerDayField.setEnabled(false);
					instancesPerDayField.setText("");
				} else
					instancesPerDayField.setEnabled(true);

			}
		});

	}

	/**
	 * Choose to change activity period or postpone it.
	 */
	private class ActivityOptionAction extends AbstractAction {

		private Activity activity;

		public ActivityOptionAction(Activity activity) {
			super(activity.getActivityTypeCode());
			this.activity = activity;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel listFieldsPanel = new JPanel(new GridLayout(1, 2));

			JButton setPeriodButton = SwingUtils.makeSmallButton(new JButton(new ActivityAction(activity)));
			listFieldsPanel.add(setPeriodButton);

			JButton addPostponeButton = SwingUtils.makeSmallButton(new JButton(new AddPostponeAction(activity)));
			listFieldsPanel.add(addPostponeButton);

			if (activity.getActivityPeriod() == null)
				addPostponeButton.setEnabled(false);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Select an option for \"" + activity.getActivityTypeCode() + "\"", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				solverAndPersistenceFrame.resetScreen();
			}

		}

	}

	/**
	 * Add a new activity to schedule using the GUI.
	 */
	private class AddActivityOptionAction extends AbstractAction {

		public AddActivityOptionAction() {
			super("Add activity");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel listFieldsPanel = new JPanel(new GridLayout(1, 2));

			JButton setPeriodButton = SwingUtils.makeSmallButton(new JButton(new AddActivityAction()));
			listFieldsPanel.add(setPeriodButton);

			JButton addPostponeButton = SwingUtils.makeSmallButton(new JButton(new AddRelativeActivityAction()));
			listFieldsPanel.add(addPostponeButton);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Select an option", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				solverAndPersistenceFrame.resetScreen();
			}

		}

	}

	private class ActivityAction extends AbstractAction {

		private Activity activity;

		public ActivityAction(Activity activity) {
			super("Set period");
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

			periodListField.setSelectedItem((activity instanceof NormalRelativeActivity)
					? ((NormalRelativeActivity) activity).getActivityPeriod()
					: activity.getActivityPeriod());
			listFieldsPanel.add(periodListField);

			listFieldsPanel.add(new JLabel("Immovable:"));
			JCheckBox lockedField = new JCheckBox("activity immovable during planning");
			lockedField.setSelected(activity.isImmovable());
			listFieldsPanel.add(lockedField);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Select period for \"" + activity.getActivityTypeCode() + "\"", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				ActivityPeriod toActivityPeriod = (ActivityPeriod) periodListField.getSelectedItem();

				if (!(activity instanceof NormalRelativeActivity)) {
					if (activity.getActivityPeriod() != toActivityPeriod) {

						if (solutionBusiness.isSolving()) {
							logger.error("Not doing user change because the solver is solving.");
							return;
						}

						solutionBusiness.doChangeMove(activity, "activityPeriod", toActivityPeriod);
					}
				} else {
					if (((NormalRelativeActivity) activity).getActivityPeriod() != toActivityPeriod) {

						if (solutionBusiness.isSolving()) {
							logger.error("Not doing user change because the solver is solving.");
							return;
						}

						doProblemFactChange(scoreDirector -> {
							scoreDirector.beforeVariableChanged(((NormalRelativeActivity) activity), "activityPeriod");
							((NormalRelativeActivity) activity).setActivityPeriod(toActivityPeriod);
							scoreDirector.afterVariableChanged(((NormalRelativeActivity) activity), "activityPeriod");

							scoreDirector.triggerVariableListeners();
						});
					}
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

	/**
	 * Add a new activity using GUI components similarly to adding a new activity
	 * using XML.
	 */
	private class AddActivityAction extends AbstractAction {

		public AddActivityAction() {
			super("Add a new activity");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel listFieldsPanel = new JPanel(new GridLayout(3, 2));

			ActivitySchedule activitySchedule = getSolution();

			ActivityType activityType = new ActivityType();
			JButton activityTypeButton = SwingUtils
					.makeSmallButton(new JButton(new AddActivityTypeAction(activityType)));
			activityTypeButton.setToolTipText("Define activity type properties");
			listFieldsPanel.add(new JLabel("Define the activity type"));
			listFieldsPanel.add(activityTypeButton);

			ExcludedTimePeriodsPenalty etpp = new ExcludedTimePeriodsPenalty();
			etpp.setExcludedActivityPeriods(new ArrayList<>());
			JButton excludedTimePeriodsButton = SwingUtils
					.makeSmallButton(new JButton(new AddExcludedTimePeriodsAction(etpp)));
			excludedTimePeriodsButton.setToolTipText("Set excluded time periods");
			listFieldsPanel.add(new JLabel("Excluded time periods"));
			listFieldsPanel.add(excludedTimePeriodsButton);

			listFieldsPanel.add(new JLabel("Immovable:"));
			JCheckBox lockedField = new JCheckBox("activity immovable during planning");
			lockedField.setSelected(false);
			listFieldsPanel.add(lockedField);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Add a new activity", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				doProblemFactChange(scoreDirector -> {

					List<Activity> activityList = new ArrayList<>(activitySchedule.getActivityList());
					activitySchedule.setActivityList(activityList);

					List<ActivityType> activityTypeList = new ArrayList<>(activitySchedule.getActivityTypeList());
					activitySchedule.setActivityTypeList(activityTypeList);

					activityType.setId(activityTypeList.get(activityTypeList.size() - 1).getId() + 1);

					int instances = 1;
					if (activityType.getInstancesPerDay() != 0)
						instances = activityType.getInstancesPerDay() * 7;
					else if (activityType.getInstancesPerWeek() != 0)
						instances = activityType.getInstancesPerWeek();

					for (int i = 0; i < instances; i++) {
						NormalActivity activity = new NormalActivity();
						activity.setActivityType(activityType);
						activity.setImmovable(lockedField.isSelected());
						activity.setOnDropdown(true);
						activity.setPeriodDomainRangeList(activitySchedule.getActivityPeriodList());
						activity.setId(activityList.get(activityList.size() - 1).getId() + 1);

						scoreDirector.beforeEntityAdded(activity);
						activityList.add(activity);
						scoreDirector.afterEntityAdded(activity);

						if (activityType.getImposedPeriod() != null) {
							// scoreDirector.beforeVariableChanged(activity, "activityPeriod");
							// activity.setActivityPeriod(activityType.getImposedPeriod());
							// scoreDirector.afterVariableChanged(activity, "activityPeriod");

							activity.setImmovable(true);
						}
					}

					etpp.setActivityType(activityType);

					scoreDirector.beforeProblemFactAdded(activityType);
					activityTypeList.add(activityType);
					scoreDirector.afterProblemFactAdded(activityType);

					scoreDirector.triggerVariableListeners();

					solverAndPersistenceFrame.startSolveAction();

				});

				solverAndPersistenceFrame.resetScreen();
			}

		}

	}

	/**
	 * Add a new relative activity using GUI components similarly to adding a new
	 * activity using XML.
	 */
	private class AddRelativeActivityAction extends AbstractAction {

		public AddRelativeActivityAction() {
			super("Add a new activity which is relative to another activity");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel listFieldsPanel = new JPanel(new GridLayout(8, 2));

			ActivitySchedule activitySchedule = getSolution();

			ActivityType activityType = new ActivityType();
			JButton activityTypeButton = SwingUtils
					.makeSmallButton(new JButton(new AddRelativeActivityTypeAction(activityType)));
			activityTypeButton.setToolTipText("Define activity type properties");
			listFieldsPanel.add(new JLabel("Define the activity type"));
			listFieldsPanel.add(activityTypeButton);

			List<ActivityType> activityTypeList = activitySchedule.getActivityTypeList();
			Set<String> activityTypeSet = new HashSet<>();
			List<ActivityType> activityTypeResultList = new ArrayList<>();

			for (ActivityType activityTypeElement : activityTypeList) {
				if (activityTypeSet.add(activityTypeElement.getCode())) {
					activityTypeResultList.add(activityTypeElement);
				}
			}

			JComboBox activityTypeListListField = new JComboBox<>(
					activityTypeResultList.toArray(new Object[activityTypeResultList.size() + 1]));
			activityTypeListListField.setSelectedItem(null);
			LabeledComboBoxRenderer.applyToComboBox(activityTypeListListField);
			listFieldsPanel.add(new JLabel("Relative to the activity"));
			listFieldsPanel.add(activityTypeListListField);

			List<ActivityCategory> activityCategoryList = activitySchedule.getActivityCategoryList();
			JComboBox activityCategoryListField = new JComboBox<>(
					activityCategoryList.toArray(new Object[activityCategoryList.size() + 1]));
			activityCategoryListField.setSelectedItem(null);
			LabeledComboBoxRenderer.applyToComboBox(activityCategoryListField);
			listFieldsPanel.add(new JLabel("Relative to category"));
			listFieldsPanel.add(activityCategoryListField);

			JTextField instancesPerDayField = new JTextField();
			listFieldsPanel.add(new JLabel("Instances per day"));
			listFieldsPanel.add(instancesPerDayField);

			JTextField instancesPerWeekField = new JTextField();
			listFieldsPanel.add(new JLabel("Instances per week"));
			listFieldsPanel.add(instancesPerWeekField);

			triggerInstancesListeners(instancesPerDayField, instancesPerWeekField);

			ActionListener activityTypeActionListener = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (activityTypeListListField.getSelectedItem() != null) {
						activityCategoryListField.setEnabled(false);
						activityCategoryListField.setSelectedItem(null);

						instancesPerDayField.setEnabled(false);
						instancesPerDayField.setText("");

						instancesPerWeekField.setEnabled(false);
						instancesPerWeekField.setText("");
					} else {
						activityCategoryListField.setEnabled(true);
						instancesPerDayField.setEnabled(true);
						instancesPerWeekField.setEnabled(true);
					}
				}
			};

			activityTypeListListField.addActionListener(activityTypeActionListener);

			ActionListener activityCategoryActionListener = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (activityCategoryListField.getSelectedItem() != null) {
						activityTypeListListField.setEnabled(false);
						activityTypeListListField.setSelectedItem(null);
					} else
						activityTypeListListField.setEnabled(true);
				}
			};

			activityCategoryListField.addActionListener(activityCategoryActionListener);

			ExcludedTimePeriodsPenalty etpp = new ExcludedTimePeriodsPenalty();
			etpp.setExcludedActivityPeriods(new ArrayList<>());
			JButton excludedTimePeriodsButton = SwingUtils
					.makeSmallButton(new JButton(new AddExcludedTimePeriodsAction(etpp)));
			excludedTimePeriodsButton.setToolTipText("Set excluded time periods");
			listFieldsPanel.add(new JLabel("Excluded time periods"));
			listFieldsPanel.add(excludedTimePeriodsButton);

			JTextField offsetField = new JTextField();
			listFieldsPanel.add(new JLabel("Offset"));
			listFieldsPanel.add(offsetField);

			listFieldsPanel.add(new JLabel("Relative type:"));
			JComboBox relativeTypeListField = new JComboBox<>(RelativeType.values());
			LabeledComboBoxRenderer.applyToComboBox(relativeTypeListField);
			relativeTypeListField.setSelectedItem(null);
			listFieldsPanel.add(relativeTypeListField);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Add a new activity which is relative to another activity", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				doProblemFactChange(scoreDirector -> {

					List<Activity> activityList = new ArrayList<>(activitySchedule.getActivityList());
					activitySchedule.setActivityList(activityList);

					List<ActivityType> activityTypeListSolution = new ArrayList<>(
							activitySchedule.getActivityTypeList());
					activitySchedule.setActivityTypeList(activityTypeListSolution);

					List<RelativeActivityPenalty> relativeActivityPenaltyList = new ArrayList<>(
							activitySchedule.getRelativeActivityPenaltyList());
					activitySchedule.setRelativeActivityPenaltyList(relativeActivityPenaltyList);

					activityType.setId(activityTypeListSolution.get(activityTypeListSolution.size() - 1).getId() + 1);

					if (relativeTypeListField.getSelectedItem() != null) {

						RelativeActivityPenalty relativeActivityPenalty = new RelativeActivityPenalty();
						relativeActivityPenalty.setRelativeType((RelativeType) relativeTypeListField.getSelectedItem());
						relativeActivityPenalty.setRelativeActivityType(activityType.getCode());

						if (activityTypeListListField.getSelectedItem() != null) {

							int instances = 1;
							ActivityType normalActivityType = (ActivityType) activityTypeListListField
									.getSelectedItem();

							if (normalActivityType.getInstancesPerDay() != 0) {
								instances = normalActivityType.getInstancesPerDay() * 7;
								activityType.setInstancesPerDay(instances / 7);
							} else if (normalActivityType.getInstancesPerWeek() != 0) {
								instances = normalActivityType.getInstancesPerWeek();
								activityType.setInstancesPerWeek(instances);
							}

							for (int i = 0; i < instances; i++) {
								NormalRelativeActivity relativeActivity = new NormalRelativeActivity();
								relativeActivity.setActivityType(activityType);
								relativeActivity.setOffset(
										Integer.parseInt(offsetField.getText().equals("") ? "1" : offsetField.getText())
												* getRelativeTypeSign(relativeActivityPenalty.getRelativeType()));
								relativeActivity.setImmovable(false);
								relativeActivity.setOnDropdown(true);
								relativeActivity.setId(activityList.get(activityList.size() - 1).getId() + 1);

								scoreDirector.beforeEntityAdded(relativeActivity);
								activityList.add(relativeActivity);
								scoreDirector.afterEntityAdded(relativeActivity);
							}

							relativeActivityPenalty.setNormalActivityType(normalActivityType.getCode());

						} else if (activityCategoryListField.getSelectedItem() != null) {

							int instances = 1;
							String activityCategory = ((ActivityCategory) activityCategoryListField.getSelectedItem())
									.getCode();

							if (!instancesPerDayField.getText().equals("")) {
								instances = Integer.parseInt(instancesPerDayField.getText()) * 7;
								activityType.setInstancesPerDay(instances / 7);
							} else if (!instancesPerWeekField.getText().equals("")) {
								instances = Integer.parseInt(instancesPerWeekField.getText());
								activityType.setInstancesPerWeek(instances);
							}

							for (int i = 0; i < instances; i++) {
								NormalRelativeActivity relativeActivity = new NormalRelativeActivity();
								relativeActivity.setActivityType(activityType);
								relativeActivity.setOffset(
										Integer.parseInt(offsetField.getText().equals("") ? "1" : offsetField.getText())
												* getRelativeTypeSign(relativeActivityPenalty.getRelativeType()));
								relativeActivity.setImmovable(false);
								relativeActivity.setOnDropdown(true);
								relativeActivity.setId(activityList.get(activityList.size() - 1).getId() + 1);

								scoreDirector.beforeEntityAdded(relativeActivity);
								activityList.add(relativeActivity);
								scoreDirector.afterEntityAdded(relativeActivity);
							}

							relativeActivityPenalty.setCategory(activityCategory);

						}

						etpp.setActivityType(activityType);

						relativeActivityPenalty.setId(
								relativeActivityPenaltyList.get(relativeActivityPenaltyList.size() - 1).getId() + 1);

						scoreDirector.beforeProblemFactAdded(relativeActivityPenalty);
						relativeActivityPenaltyList.add(relativeActivityPenalty);
						scoreDirector.afterProblemFactAdded(relativeActivityPenalty);

						scoreDirector.beforeProblemFactAdded(activityType);
						activityTypeListSolution.add(activityType);
						scoreDirector.afterProblemFactAdded(activityType);

						// *****trigger the listener*****
						if (activityTypeListListField.getSelectedItem() != null) {

							triggerListener(activityList,
									((ActivityType) activityTypeListListField.getSelectedItem()).getCode(), null);

						} else if ((activityCategoryListField.getSelectedItem() != null)) {

							triggerListener(activityList, null,
									((ActivityCategory) activityCategoryListField.getSelectedItem()).getCode());
						}

						scoreDirector.triggerVariableListeners();

						solverAndPersistenceFrame.startSolveAction();

					}

				});

				solverAndPersistenceFrame.resetScreen();
			}

		}

	}

	/**
	 * Set activity properties.
	 */
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

			triggerInstancesListeners(instancesPerDayField, instancesPerWeekField);

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

			List<ActivityCategory> activityCategoryList = activitySchedule.getActivityCategoryList();
			JComboBox activityCategoryListField = new JComboBox<>(
					activityCategoryList.toArray(new Object[activityCategoryList.size() + 1]));
			activityCategoryListField.setSelectedItem(null);
			LabeledComboBoxRenderer.applyToComboBox(activityCategoryListField);
			listFieldsPanel.add(new JLabel("Activity Category"));
			listFieldsPanel.add(activityCategoryListField);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Define activity type properties", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				activityType.setCode(codeField.getText());

				activityType.setDuration(
						Integer.parseInt(durationField.getText().equals("") ? "0" : durationField.getText()));
				activityType.setDifficulty((Difficulty) difficultyListField.getSelectedItem());

				activityType.setCalories(
						Integer.parseInt(caloriesField.getText().equals("") ? "0" : caloriesField.getText()));

				activityType.setInstancesPerDay(Integer
						.parseInt(instancesPerDayField.getText().equals("") ? "0" : instancesPerDayField.getText()));

				activityType.setInstancesPerWeek(Integer
						.parseInt(instancesPerWeekField.getText().equals("") ? "1" : instancesPerWeekField.getText()));

				if (imposedPeriod.getTime() == null || imposedPeriod.getWeekDay() == null)
					activityType.setImposedPeriod(null);
				else
					activityType.setImposedPeriod(imposedPeriod);

				if (permittedIntervals.size() != 0)
					activityType.setPermittedIntervals(permittedIntervals);
				else
					activityType.setPermittedIntervals(null);

				activityType.setActivityCategory((ActivityCategory) activityCategoryListField.getSelectedItem());

				solverAndPersistenceFrame.resetScreen();
			}

		}

	}

	/**
	 * Set activity properties.
	 */
	private class AddRelativeActivityTypeAction extends AbstractAction {

		private ActivityType activityType;

		public AddRelativeActivityTypeAction(ActivityType activityType) {
			super("Define activity type properties");
			this.activityType = activityType;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel listFieldsPanel = new JPanel(new GridLayout(6, 2));

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

			List<TimeInterval> permittedIntervals = new ArrayList<>();
			JButton permittedIntervalsButton = SwingUtils
					.makeSmallButton(new JButton(new AddPermittedIntervalsAction(permittedIntervals)));
			listFieldsPanel.add(new JLabel("Permitted intervals"));
			listFieldsPanel.add(permittedIntervalsButton);

			List<ActivityCategory> activityCategoryList = activitySchedule.getActivityCategoryList();
			JComboBox activityCategoryListField = new JComboBox<>(
					activityCategoryList.toArray(new Object[activityCategoryList.size() + 1]));
			activityCategoryListField.setSelectedItem(null);
			LabeledComboBoxRenderer.applyToComboBox(activityCategoryListField);
			listFieldsPanel.add(new JLabel("Activity Category"));
			listFieldsPanel.add(activityCategoryListField);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Define activity type properties", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				activityType.setCode(codeField.getText());

				activityType.setDuration(
						Integer.parseInt(durationField.getText().equals("") ? "0" : durationField.getText()));
				activityType.setDifficulty((Difficulty) difficultyListField.getSelectedItem());

				activityType.setCalories(
						Integer.parseInt(caloriesField.getText().equals("") ? "0" : caloriesField.getText()));

				if (permittedIntervals.size() != 0)
					activityType.setPermittedIntervals(permittedIntervals);
				else
					activityType.setPermittedIntervals(null);

				activityType.setActivityCategory((ActivityCategory) activityCategoryListField.getSelectedItem());

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

			listFieldsPanel.add(new JLabel("Week day:"));
			JComboBox weekDayListField = new JComboBox<>(weekDayList.toArray(new Object[weekDayList.size() + 1]));
			LabeledComboBoxRenderer.applyToComboBox(weekDayListField);
			weekDayListField.setSelectedItem(null);
			listFieldsPanel.add(weekDayListField);

			JTextField hourField = new JTextField();
			listFieldsPanel.add(new JLabel("Hour"));
			listFieldsPanel.add(hourField);

			JTextField minutesField = new JTextField();
			listFieldsPanel.add(new JLabel("Minute"));
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
			JPanel listFieldsPanel = new JPanel(new GridLayout(2, 1));

			listFieldsPanel.add(new JLabel("Added " + permittedIntervals.size() + " permitted intervals."));

			JButton addTimeIntervalButton = SwingUtils
					.makeSmallButton(new JButton(new CreateTimeIntervalAction(permittedIntervals, listFieldsPanel)));
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
		private JPanel parentListFieldsPanel;

		public CreateTimeIntervalAction(List<TimeInterval> permittedIntervals, JPanel parentListFieldsPanel) {
			super("Add a time interval");
			this.permittedIntervals = permittedIntervals;
			this.parentListFieldsPanel = parentListFieldsPanel;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel listFieldsPanel = new JPanel(new GridLayout(4, 2));

			JTextField minStartHourField = new JTextField();
			listFieldsPanel.add(new JLabel("minStart - hour"));
			listFieldsPanel.add(minStartHourField);

			JTextField minStartMinutesField = new JTextField();
			listFieldsPanel.add(new JLabel("minStart - minute"));
			listFieldsPanel.add(minStartMinutesField);

			JTextField maxEndHourField = new JTextField();
			listFieldsPanel.add(new JLabel("maxEnd - hour"));
			listFieldsPanel.add(maxEndHourField);

			JTextField maxEndMinutesField = new JTextField();
			listFieldsPanel.add(new JLabel("maxEnd - minute"));
			listFieldsPanel.add(maxEndMinutesField);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Create the time interval", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				TimeInterval timeInterval = new TimeInterval();

				timeInterval.setMinStart(
						(minStartHourField.getText().equals("") || minStartMinutesField.getText().equals("")) ? null
								: new Time(Integer.parseInt(minStartHourField.getText()),
										Integer.parseInt(minStartMinutesField.getText())));
				timeInterval.setMaxEnd(
						(maxEndHourField.getText().equals("") || maxEndMinutesField.getText().equals("")) ? null
								: new Time(Integer.parseInt(maxEndHourField.getText()),
										Integer.parseInt(maxEndMinutesField.getText())));

				if (timeInterval.getMinStart() != null && timeInterval.getMaxEnd() != null) {
					permittedIntervals.add(timeInterval);
					parentListFieldsPanel.remove(0);
					parentListFieldsPanel
							.add(new JLabel("Added " + permittedIntervals.size() + " permitted intervals."), 0);
					parentListFieldsPanel.revalidate();
					parentListFieldsPanel.repaint();
				}

				solverAndPersistenceFrame.resetScreen();
			}

		}

	}

	private class AddExcludedTimePeriodsAction extends AbstractAction {

		private ExcludedTimePeriodsPenalty etpp;

		public AddExcludedTimePeriodsAction(ExcludedTimePeriodsPenalty etpp) {
			super("Set the excluded time periods");
			this.etpp = etpp;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel listFieldsPanel = new JPanel(new GridLayout(2, 1));

			ActivitySchedule activitySchedule = getSolution();

			listFieldsPanel.add(
					new JLabel("Added " + etpp.getExcludedActivityPeriods().size() + " excluded period intervals."));

			JButton addTimePeriodsButton = SwingUtils
					.makeSmallButton(new JButton(new AddPeriodIntervalAction(etpp, listFieldsPanel)));
			listFieldsPanel.add(addTimePeriodsButton);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Set the excluded time periods", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				doProblemFactChange(scoreDirector -> {

					List<ExcludedTimePeriodsPenalty> excludedTimePeriodsPenaltyList = new ArrayList<>(
							activitySchedule.getExcludedTimePeriodsList());
					activitySchedule.setExcludedTimePeriodsList(excludedTimePeriodsPenaltyList);

					etpp.setId(
							excludedTimePeriodsPenaltyList.get(excludedTimePeriodsPenaltyList.size() - 1).getId() + 1);

					scoreDirector.beforeProblemFactAdded(etpp);
					excludedTimePeriodsPenaltyList.add(etpp);
					scoreDirector.afterProblemFactAdded(etpp);
				});

				solverAndPersistenceFrame.resetScreen();
			}

		}

	}

	private class AddPeriodIntervalAction extends AbstractAction {

		private ExcludedTimePeriodsPenalty etpp;
		private JPanel parentListFieldsPanel;

		public AddPeriodIntervalAction(ExcludedTimePeriodsPenalty etpp, JPanel parentListFieldsPanel) {
			super("Add excluded time period");
			this.etpp = etpp;
			this.parentListFieldsPanel = parentListFieldsPanel;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel listFieldsPanel = new JPanel(new GridLayout(2, 2));

			listFieldsPanel.add(new JLabel("Set start period"));
			ActivityPeriod startPeriod = new ActivityPeriod();
			JButton setStartPeriodButton = SwingUtils.makeSmallButton(new JButton(new SetPeriodAction(startPeriod)));
			listFieldsPanel.add(setStartPeriodButton);

			listFieldsPanel.add(new JLabel("Set end period"));
			ActivityPeriod endPeriod = new ActivityPeriod();
			JButton setEndPeriodButton = SwingUtils.makeSmallButton(new JButton(new SetPeriodAction(endPeriod)));
			listFieldsPanel.add(setEndPeriodButton);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Add excluded time period", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				if (startPeriod.getTime() != null && endPeriod.getTime() != null) {
					etpp.getExcludedActivityPeriods().add(new PeriodInterval(startPeriod, endPeriod));

					parentListFieldsPanel.remove(0);
					parentListFieldsPanel.add(new JLabel(
							"Added " + etpp.getExcludedActivityPeriods().size() + " excluded period intervals."), 0);
					parentListFieldsPanel.revalidate();
					parentListFieldsPanel.repaint();
				}

				solverAndPersistenceFrame.resetScreen();
			}

		}

	}

	private class SetPeriodAction extends AbstractAction {

		private ActivityPeriod activityPeriod;

		public SetPeriodAction(ActivityPeriod activityPeriod) {
			super("Set period");
			this.activityPeriod = activityPeriod;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel listFieldsPanel = new JPanel(new GridLayout(3, 2));

			ActivitySchedule activitySchedule = getSolution();
			List<WeekDay> weekDayList = activitySchedule.getWeekdayList();
			JComboBox weekDayListField = new JComboBox<>(weekDayList.toArray(new Object[weekDayList.size() + 1]));
			LabeledComboBoxRenderer.applyToComboBox(weekDayListField);
			weekDayListField.setSelectedItem(null);
			listFieldsPanel.add(new JLabel("week day"));
			listFieldsPanel.add(weekDayListField);

			JTextField hourField = new JTextField();
			listFieldsPanel.add(new JLabel("hour"));
			listFieldsPanel.add(hourField);

			JTextField minutesField = new JTextField();
			listFieldsPanel.add(new JLabel("minute"));
			listFieldsPanel.add(minutesField);

			int result = JOptionPane.showConfirmDialog(CAMITaskSchedulerPanel.this.getRootPane(), listFieldsPanel,
					"Set period", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				activityPeriod.setWeekDay((WeekDay) weekDayListField.getSelectedItem());
				activityPeriod.setTime(
						new Time(Integer.parseInt(hourField.getText()), Integer.parseInt(minutesField.getText())));

				solverAndPersistenceFrame.resetScreen();
			}

		}

	}

	/**
	 * Postpone a normal activity / normal relative activity.
	 */
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

					ActivitySchedule activitySchedule = getSolution();
					Activity workingActivity = scoreDirector.lookUpWorkingObject(activity);

					// normal activities have their period and value range changed
					// for 15, 30, 60 min postpones, value range list will have just the postponed
					// period included(activity period when it was postponed + 15/30/60 min)
					// for the other postpones, the value range is changed accordingly and period is
					// set to null
					if (workingActivity instanceof NormalActivity) {

						if (workingActivity.getActivityPeriod() != null) {

							if ((PostponeType) postponeTypeListField
									.getSelectedItem() == PostponeType.POSTPONE_LATER_THIS_WEEK) {

								// *in real time rescheduling these will be updated relative to the current
								// period of the week*

								// the future activities have their value range changed
								setValueRangeForFutureActivities(activitySchedule, workingActivity.getActivityPeriod());

								// the past activities have their value range changed
								setValueRangeForPastActivities(activitySchedule, workingActivity.getActivityPeriod());

								setValueRangeForLaterThisWeekPostponedActivity(activitySchedule, workingActivity);

								scoreDirector.beforeVariableChanged(workingActivity, "activityPeriod");
								((NormalActivity) workingActivity).setActivityPeriod(null);
								scoreDirector.afterVariableChanged(workingActivity, "activityPeriod");

							} else if ((PostponeType) postponeTypeListField
									.getSelectedItem() == PostponeType.POSTPONE_LATER_THIS_DAY) {

								setValueRangeForFutureActivities(activitySchedule, workingActivity.getActivityPeriod());
								setValueRangeForPastActivities(activitySchedule, workingActivity.getActivityPeriod());

								setValueRangeForLaterThisDayPostponedActivity(activitySchedule, workingActivity);

								scoreDirector.beforeVariableChanged(workingActivity, "activityPeriod");
								((NormalActivity) workingActivity).setActivityPeriod(null);
								scoreDirector.afterVariableChanged(workingActivity, "activityPeriod");

							} else if ((PostponeType) postponeTypeListField
									.getSelectedItem() == PostponeType.POSTPONE_15MIN) {

								setValueRangeForFutureActivities(activitySchedule, workingActivity.getActivityPeriod());
								setValueRangeForPastActivities(activitySchedule, workingActivity.getActivityPeriod());

								ActivityPeriod postponedPeriod = AdjustActivityPeriod
										.getAdjustedPeriod(workingActivity.getActivityPeriod(), 15);

								scoreDirector.beforeProblemPropertyChanged(workingActivity);
								((NormalActivity) workingActivity).setPeriodDomainRangeList(
										new ArrayList<ActivityPeriod>(Arrays.asList(postponedPeriod)));
								scoreDirector.afterProblemPropertyChanged(workingActivity);

								scoreDirector.beforeVariableChanged(workingActivity, "activityPeriod");
								((NormalActivity) workingActivity).setActivityPeriod(postponedPeriod);
								scoreDirector.afterVariableChanged(workingActivity, "activityPeriod");

							} else if ((PostponeType) postponeTypeListField
									.getSelectedItem() == PostponeType.POSTPONE_30MIN) {

								setValueRangeForFutureActivities(activitySchedule, workingActivity.getActivityPeriod());
								setValueRangeForPastActivities(activitySchedule, workingActivity.getActivityPeriod());

								ActivityPeriod postponedPeriod = AdjustActivityPeriod
										.getAdjustedPeriod(workingActivity.getActivityPeriod(), 30);

								scoreDirector.beforeProblemPropertyChanged(workingActivity);
								((NormalActivity) workingActivity).setPeriodDomainRangeList(
										new ArrayList<ActivityPeriod>(Arrays.asList(postponedPeriod)));
								scoreDirector.afterProblemPropertyChanged(workingActivity);

								scoreDirector.beforeVariableChanged(workingActivity, "activityPeriod");
								((NormalActivity) workingActivity).setActivityPeriod(postponedPeriod);
								scoreDirector.afterVariableChanged(workingActivity, "activityPeriod");

							} else if ((PostponeType) postponeTypeListField
									.getSelectedItem() == PostponeType.POSTPONE_1HOUR) {

								setValueRangeForFutureActivities(activitySchedule, workingActivity.getActivityPeriod());
								setValueRangeForPastActivities(activitySchedule, workingActivity.getActivityPeriod());

								ActivityPeriod postponedPeriod = AdjustActivityPeriod
										.getAdjustedPeriod(workingActivity.getActivityPeriod(), 60);

								scoreDirector.beforeProblemPropertyChanged(workingActivity);
								((NormalActivity) workingActivity).setPeriodDomainRangeList(
										new ArrayList<ActivityPeriod>(Arrays.asList(postponedPeriod)));
								scoreDirector.afterProblemPropertyChanged(workingActivity);

								scoreDirector.beforeVariableChanged(workingActivity, "activityPeriod");
								((NormalActivity) workingActivity).setActivityPeriod(postponedPeriod);
								scoreDirector.afterVariableChanged(workingActivity, "activityPeriod");

							}

							scoreDirector.triggerVariableListeners();

							solverAndPersistenceFrame.startSolveAction();
						}

						// normal relative activities have their period changed
					} else if (workingActivity instanceof NormalRelativeActivity) {

						Postpone postpone = new Postpone();
						postpone.setPostponePeriod(((NormalRelativeActivity) workingActivity).getActivityPeriod());
						postpone.setPostponeType((PostponeType) postponeTypeListField.getSelectedItem());
						postpone.setId(postponeId++);

						if ((PostponeType) postponeTypeListField.getSelectedItem() == PostponeType.POSTPONE_15MIN) {

							setValueRangeForFutureActivities(activitySchedule,
									((NormalRelativeActivity) workingActivity).getActivityPeriod());
							setValueRangeForPastActivities(activitySchedule,
									((NormalRelativeActivity) workingActivity).getActivityPeriod());

							ActivityPeriod period = AdjustActivityPeriod.getAdjustedPeriod(
									((NormalRelativeActivity) workingActivity).getActivityPeriod(), 15);

							scoreDirector.beforeVariableChanged(((NormalRelativeActivity) workingActivity),
									"activityPeriod");
							((NormalRelativeActivity) workingActivity).setActivityPeriod(period);
							scoreDirector.afterVariableChanged(((NormalRelativeActivity) workingActivity),
									"activityPeriod");
						} else if ((PostponeType) postponeTypeListField
								.getSelectedItem() == PostponeType.POSTPONE_30MIN) {

							setValueRangeForFutureActivities(activitySchedule,
									((NormalRelativeActivity) workingActivity).getActivityPeriod());
							setValueRangeForPastActivities(activitySchedule,
									((NormalRelativeActivity) workingActivity).getActivityPeriod());

							ActivityPeriod period = AdjustActivityPeriod.getAdjustedPeriod(
									((NormalRelativeActivity) workingActivity).getActivityPeriod(), 30);

							scoreDirector.beforeVariableChanged(((NormalRelativeActivity) workingActivity),
									"activityPeriod");
							((NormalRelativeActivity) workingActivity).setActivityPeriod(period);
							scoreDirector.afterVariableChanged(((NormalRelativeActivity) workingActivity),
									"activityPeriod");
						} else if ((PostponeType) postponeTypeListField
								.getSelectedItem() == PostponeType.POSTPONE_1HOUR) {

							setValueRangeForFutureActivities(activitySchedule,
									((NormalRelativeActivity) workingActivity).getActivityPeriod());
							setValueRangeForPastActivities(activitySchedule,
									((NormalRelativeActivity) workingActivity).getActivityPeriod());

							ActivityPeriod period = AdjustActivityPeriod.getAdjustedPeriod(
									((NormalRelativeActivity) workingActivity).getActivityPeriod(), 60);

							scoreDirector.beforeVariableChanged(((NormalRelativeActivity) workingActivity),
									"activityPeriod");
							((NormalRelativeActivity) workingActivity).setActivityPeriod(period);
							scoreDirector.afterVariableChanged(((NormalRelativeActivity) workingActivity),
									"activityPeriod");
						} else if ((PostponeType) postponeTypeListField
								.getSelectedItem() == PostponeType.POSTPONE_LATER_THIS_DAY) {
							ActivityPeriod period = Utility.getRelativeActivityPeriod(activitySchedule,
									((NormalRelativeActivity) workingActivity), AdjustActivityPeriod.getAdjustedPeriod(
											((NormalRelativeActivity) workingActivity).getActivityPeriod(), 65),
									5);

							setValueRangeForFutureActivities(activitySchedule,
									((NormalRelativeActivity) workingActivity).getActivityPeriod());
							setValueRangeForPastActivities(activitySchedule,
									((NormalRelativeActivity) workingActivity).getActivityPeriod());

							for (Activity activity : activitySchedule.getActivityList()) {
								if (activity instanceof NormalRelativeActivity
										&& ((NormalRelativeActivity) activity)
												.getActivityTypeCode() == ((NormalRelativeActivity) workingActivity)
														.getActivityTypeCode()
										&& ((NormalRelativeActivity) activity).getActivityPeriod() != null
										&& ((NormalRelativeActivity) workingActivity).getActivityPeriod() != null
										&& ((NormalRelativeActivity) activity).getActivityPeriodWeekday()
												.getDayIndex() == ((NormalRelativeActivity) workingActivity)
														.getActivityPeriodWeekday().getDayIndex()) {

									scoreDirector.beforeProblemPropertyChanged(((NormalRelativeActivity) activity));
									((NormalRelativeActivity) activity).setPostpone(postpone);
									scoreDirector.afterProblemPropertyChanged(((NormalRelativeActivity) activity));

								}
							}

							scoreDirector.beforeVariableChanged(((NormalRelativeActivity) workingActivity),
									"activityPeriod");
							((NormalRelativeActivity) workingActivity).setActivityPeriod(period);
							scoreDirector.afterVariableChanged(((NormalRelativeActivity) workingActivity),
									"activityPeriod");
						} else if ((PostponeType) postponeTypeListField
								.getSelectedItem() == PostponeType.POSTPONE_LATER_THIS_WEEK) {

							List<ActivityPeriod> periodsLaterThisWeek = Utility.getFreePeriodsLaterThisWeek(
									activitySchedule, workingActivity,
									workingActivity.getActivityPeriodWeekday().getDayIndex());

							setValueRangeForFutureActivities(activitySchedule,
									((NormalRelativeActivity) workingActivity).getActivityPeriod());
							setValueRangeForPastActivities(activitySchedule,
									((NormalRelativeActivity) workingActivity).getActivityPeriod());

							for (Activity activity : activitySchedule.getActivityList()) {
								if (activity instanceof NormalRelativeActivity
										&& ((NormalRelativeActivity) activity)
												.getActivityTypeCode() == ((NormalRelativeActivity) workingActivity)
														.getActivityTypeCode()
										&& ((NormalRelativeActivity) activity).getActivityPeriod() != null
										&& ((NormalRelativeActivity) workingActivity).getActivityPeriod() != null
										&& ((NormalRelativeActivity) activity).getActivityPeriodWeekday()
												.getDayIndex() > ((NormalRelativeActivity) workingActivity)
														.getActivityPeriodWeekday().getDayIndex()) {

									scoreDirector.beforeProblemPropertyChanged(((NormalRelativeActivity) activity));
									((NormalRelativeActivity) activity).setPostpone(postpone);
									scoreDirector.afterProblemPropertyChanged(((NormalRelativeActivity) activity));

								}
							}

							scoreDirector.beforeVariableChanged(((NormalRelativeActivity) workingActivity),
									"activityPeriod");
							((NormalRelativeActivity) workingActivity).setActivityPeriod(periodsLaterThisWeek
									.get(ThreadLocalRandom.current().nextInt(0, periodsLaterThisWeek.size())));
							scoreDirector.afterVariableChanged(((NormalRelativeActivity) workingActivity),
									"activityPeriod");

						}

						scoreDirector.beforeProblemPropertyChanged(workingActivity);
						workingActivity.setPostpone(postpone);
						scoreDirector.afterProblemPropertyChanged(workingActivity);

						scoreDirector.triggerVariableListeners();

						solverAndPersistenceFrame.startSolveAction();
					}

				});

				solverAndPersistenceFrame.resetScreen();
			}

		}

	}

	private void setValueRangeForFutureActivities(ActivitySchedule activitySchedule, ActivityPeriod activityPeriod) {
		doProblemFactChange(scoreDirector -> {

			List<ActivityPeriod> restrictedPeriodDomain = getRestrictedPeriodDomainForFutureActivities(activitySchedule,
					activityPeriod);

			for (Activity activity : activitySchedule.getActivityList()) {
				if ((activity instanceof NormalActivity) && activity.getActivityPeriod() != null) {
					if (activity.getActivityPeriodWeekday().getDayIndex() == activityPeriod.getWeekDayIndex()) {
						if (Utility.after(activityPeriod.getTime(), activity.getActivityPeriod().getTime())) {
							scoreDirector.beforeProblemPropertyChanged(activity);
							((NormalActivity) activity).setPeriodDomainRangeList(restrictedPeriodDomain);
							scoreDirector.afterProblemPropertyChanged(activity);
						}
					} else if (activity.getActivityPeriodWeekday().getDayIndex() > activityPeriod.getWeekDayIndex()) {
						scoreDirector.beforeProblemPropertyChanged(activity);
						((NormalActivity) activity).setPeriodDomainRangeList(restrictedPeriodDomain);
						scoreDirector.afterProblemPropertyChanged(activity);
					}
				}
			}
		});
	}

	/**
	 * Past activities are "immovable", so their range is just their current set
	 * period value.
	 * 
	 * @param activitySchedule
	 * @param activityPeriod
	 */
	private void setValueRangeForPastActivities(ActivitySchedule activitySchedule, ActivityPeriod activityPeriod) {
		doProblemFactChange(scoreDirector -> {

			for (Activity activity : activitySchedule.getActivityList()) {
				if ((activity instanceof NormalActivity) && activity.getActivityPeriod() != null) {
					if (activity.getActivityPeriodWeekday().getDayIndex() == activityPeriod.getWeekDayIndex()) {
						if (Utility.exclusiveBefore(activity.getActivityPeriod().getTime(), activityPeriod.getTime())) {

							scoreDirector.beforeProblemPropertyChanged(activity);
							((NormalActivity) activity).setPeriodDomainRangeList(
									new ArrayList<ActivityPeriod>(Arrays.asList(activity.getActivityPeriod())));
							scoreDirector.afterProblemPropertyChanged(activity);
						}
					} else if (activity.getActivityPeriodWeekday().getDayIndex() < activityPeriod.getWeekDayIndex()) {

						scoreDirector.beforeProblemPropertyChanged(activity);
						((NormalActivity) activity).setPeriodDomainRangeList(
								new ArrayList<ActivityPeriod>(Arrays.asList(activity.getActivityPeriod())));
						scoreDirector.afterProblemPropertyChanged(activity);
					}
				}
			}
		});
	}

	private List<ActivityPeriod> getRestrictedPeriodDomainForFutureActivities(ActivitySchedule activitySchedule,
			ActivityPeriod activityPeriod) {

		List<ActivityPeriod> restrictedPeriodDomain = new ArrayList<>();

		for (ActivityPeriod period : activitySchedule.getActivityPeriodList()) {
			if (period.getWeekDayIndex() == activityPeriod.getWeekDayIndex()
					&& Utility.after(activityPeriod.getTime(), period.getTime())) {
				restrictedPeriodDomain.add(period);
			} else if (period.getWeekDayIndex() > activityPeriod.getWeekDayIndex()) {
				restrictedPeriodDomain.add(period);
			}
		}
		return restrictedPeriodDomain;
	}

	@SuppressWarnings("unused")
	private List<ActivityPeriod> getRestrictedPeriodDomainForPastActivities(ActivitySchedule activitySchedule,
			ActivityPeriod activityPeriod) {

		List<ActivityPeriod> restrictedPeriodDomain = new ArrayList<>();

		for (ActivityPeriod period : activitySchedule.getActivityPeriodList()) {
			if (period.getWeekDayIndex() == activityPeriod.getWeekDayIndex()
					&& Utility.exclusiveBefore(period.getTime(), activityPeriod.getTime())) {
				restrictedPeriodDomain.add(period);
			} else if (period.getWeekDayIndex() < activityPeriod.getWeekDayIndex()) {
				restrictedPeriodDomain.add(period);
			}
		}
		return restrictedPeriodDomain;
	}

	private List<ActivityPeriod> getRestrictedPeriodDomainForLaterThisDayPostpone(ActivitySchedule activitySchedule,
			ActivityPeriod activityPeriod) {

		List<ActivityPeriod> restrictedPeriodDomain = new ArrayList<>();

		for (ActivityPeriod period : activitySchedule.getActivityPeriodList()) {
			if (period.getWeekDayIndex() == activityPeriod.getWeekDayIndex()
					&& Utility.exclusiveAfter(activityPeriod.getTime(), period.getTime())) {
				restrictedPeriodDomain.add(period);
			}
		}
		return restrictedPeriodDomain;
	}

	private void setValueRangeForLaterThisDayPostponedActivity(ActivitySchedule activitySchedule,
			Activity workingActivity) {

		doProblemFactChange(scoreDirector -> {

			// restrict the value range for the postponed activity -> later this
			// day means
			// at least over 1 hour
			List<ActivityPeriod> restrictedPeriodDomain = getRestrictedPeriodDomainForLaterThisDayPostpone(
					activitySchedule, AdjustActivityPeriod.getAdjustedPeriod(workingActivity.getActivityPeriod(), 60));

			for (Activity activity : activitySchedule.getActivityList()) {
				if (activity instanceof NormalActivity
						&& activity.getActivityTypeCode() == workingActivity.getActivityTypeCode()
						&& activity.getActivityPeriod() != null
						&& activity.getActivityPeriodWeekday().getDayIndex() == workingActivity
								.getActivityPeriodWeekday().getDayIndex()
						&& Utility.after(workingActivity.getActivityPeriodTime(), activity.getActivityPeriodTime())) {

					scoreDirector.beforeProblemPropertyChanged(activity);
					((NormalActivity) activity)
							.setPeriodDomainRangeList(restrictedPeriodDomain.size() != 0 ? restrictedPeriodDomain
									: new ArrayList<ActivityPeriod>(Arrays.asList(activity.getActivityPeriod())));
					scoreDirector.afterProblemPropertyChanged(activity);

				}
			}

		});

	}

	private void setValueRangeForLaterThisWeekPostponedActivity(ActivitySchedule activitySchedule,
			Activity workingActivity) {

		doProblemFactChange(scoreDirector -> {

			List<ActivityPeriod> futureRestrictedPeriodDomain = getRestrictedPeriodDomainForFutureActivities(
					activitySchedule, new ActivityPeriod(new Time(0, 0),
							new WeekDay(workingActivity.getActivityPeriodWeekday().getDayIndex() + 1)));

			for (Activity activity : activitySchedule.getActivityList()) {
				if (activity instanceof NormalActivity
						&& activity.getActivityTypeCode() == workingActivity.getActivityTypeCode()
						&& activity.getActivityPeriod() != null) {

					if (activity.getActivityPeriodWeekday().getDayIndex() >= workingActivity.getActivityPeriodWeekday()
							.getDayIndex()) {

						scoreDirector.beforeProblemPropertyChanged(activity);
						((NormalActivity) activity).setPeriodDomainRangeList(
								futureRestrictedPeriodDomain.size() != 0 ? futureRestrictedPeriodDomain
										: new ArrayList<ActivityPeriod>(Arrays.asList(activity.getActivityPeriod())));
						scoreDirector.afterProblemPropertyChanged(activity);

					}

				}
			}
		});
	}

	private int getRelativeTypeSign(RelativeType relativeType) {
		return relativeType.equals(RelativeType.AFTER) ? 1 : (-1);
	}

}
