/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Frederic Silberberg
 *    @author Mitchell Caisse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import edu.wpi.cs.wpisuitetng.janeway.gui.widgets.KeyboardShortcut;
import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.PermissionModelController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.PermissionsDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrievePermissionsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar.ToolbarController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar.ToolbarView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationTreeView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter.FilterView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.subrequirementsTree.SubRequirementTreeView;

/**
 * Implementation of IJaneway that allows the other modules to interact with the
 * Janeway client
 */
public class JanewayModule implements IJanewayModule {
	
	/** List of tabs that this module will display */
	private final List<JanewayTabModel> tabs;
	
	/** The tab controller for tabView */
	private final MainTabController tabController;
	
	/**
	 * Toolbar view, displayed above the main tab view, and contains action
	 * buttons for the tabs
	 */
	private final ToolbarView toolbarView;
	
	/** the IterationTreeView that will be displayed accross the module */
	private final IterationTreeView iterationTreeView;
	
	/** The view that will display filters */
	private final FilterView filterView;
	
	private final SubRequirementTreeView subRequirementTreeView;
	
	/** The tabbed pane on the left for filters and iterations */
	private final JTabbedPane leftTabbedPane;
	
	/** The controller for the toolbarView */
	@SuppressWarnings ("unused")
	private final ToolbarController toolbarController;
	
	/** The controller for retrieving the current users permissions set */
	private final PermissionModelController permController;
	
	/**
	 * Creates a new instance of JanewayModule, initializing the tabs to be
	 * displayed
	 * 
	 */
	
	public JanewayModule() {
		
		// Start the database threads
		RequirementDatabase.getInstance().start();
		IterationDatabase.getInstance().start();
		PermissionsDatabase.getInstance().start();
		permController = new PermissionModelController();
		final RetrievePermissionsRequestObserver observer = new RetrievePermissionsRequestObserver();
		permController.get(0, observer);
		
		// initialize the list of tabs, using an array list
		tabs = new ArrayList<JanewayTabModel>();
		
		// initialize TabController
		tabController = new MainTabController();
		
		// initialize the iterationTreeView
		iterationTreeView = tabController.getIterationTreeView();
		filterView = tabController.getFilterView();
		subRequirementTreeView = tabController.getSubReqView();
		
		leftTabbedPane = new JTabbedPane();
		leftTabbedPane.addTab("Iterations", iterationTreeView);
		leftTabbedPane.addTab("Hierarchy", subRequirementTreeView);
		leftTabbedPane.addTab("Filters", filterView);
		
		// initialize the toolbarView
		toolbarView = ToolbarView.getInstance(tabController);
		
		// initialize the toolbar Controller
		toolbarController = new ToolbarController(toolbarView, tabController);
		
		tabController.addRequirementsTab();
		
		final JSplitPane splitPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, leftTabbedPane,
				tabController.getTabView());
		splitPane.setResizeWeight(0);
		
		// create a new JanewayTabModel, passing in the tab view, and a new
		// JPanel as the toolbar
		final JanewayTabModel tab1 = new JanewayTabModel(getName(),
				new ImageIcon(), toolbarView, splitPane);
		
		// add the tab to the list of tabs
		tabs.add(tab1);
		
		registerKeyboardShortcuts(tab1);
		
		// set the color of disabled combo boxes
		UIManager.put("ComboBox.disabledForeground", Color.BLACK);
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public String getName() {
		return "Requirements Manager";
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public List<JanewayTabModel> getTabs() {
		return tabs;
	}
	
	@SuppressWarnings ("serial")
	private void registerKeyboardShortcuts(final JanewayTabModel tab) {
		final String osName = System.getProperty("os.name").toLowerCase();
		
		// command + w for mac or control + w for windows: close the current tab
		if (osName.contains("mac")) {
			tab.addKeyboardShortcut(new KeyboardShortcut(KeyStroke
					.getKeyStroke("meta W"), new AbstractAction() {
				
				@Override
				public void actionPerformed(final ActionEvent e) {
					tabController.closeCurrentTab();
				}
			}));
		} else {
			tab.addKeyboardShortcut(new KeyboardShortcut(KeyStroke
					.getKeyStroke("control W"), new AbstractAction() {
				
				@Override
				public void actionPerformed(final ActionEvent e) {
					tabController.closeCurrentTab();
				}
			}));
		}
	}
	
}
