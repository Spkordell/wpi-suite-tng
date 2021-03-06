/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Alex Woodyard
 *    @author Conor Geary
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.permissions;

import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.PermissionsDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.Tab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.PermissionSelectionChangedListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.SavePermissionsAction;

/**
 * This panel is for managing the permissions for all users on the project. Only
 * admins can access this panel
 */
@SuppressWarnings ("serial")
public class PermissionsPanel extends Tab {
	
	/** List of locally stored permissions */
	private List<PermissionModel> localPermissions;
	
	/** A table to display users and their permission level */
	private PermissionsTable userTable;
	
	/** A button to select administrative permission level */
	private final JRadioButton adminButton;
	
	/** A button to select update permission level */
	private final JRadioButton updateButton;
	
	/** A button to select the minimal permission level */
	private final JRadioButton noPermissionButton;
	
	/** A button to save changes in permissions */
	private final JButton saveButton;
	
	/**
	 * Creates a new PermissionsPanel to manage the permissions for users on the
	 * project
	 */
	@SuppressWarnings ("unchecked")
	public PermissionsPanel() {
		final PermissionModel model = PermissionModel.getInstance();
		final SpringLayout layout = new SpringLayout();
		final SpringLayout radioLayout = new SpringLayout();
		saveButton = new JButton("Save Permission");
		
		final JPanel radioPanel = new JPanel();
		
		radioPanel.setLayout(radioLayout);
		setLayout(layout);
		
		/** Initialize the list of local permissions */
		localPermissions = PermissionsDatabase.getInstance().getAll();
		Collections.sort(localPermissions);
		
		// construct the table of users and their permissions
		final String[] columnNames = new String[3];
		columnNames[0] = "User";
		columnNames[1] = "Full Name";
		columnNames[2] = "Permission Level";
		
		final String[][] rowData = new String[localPermissions.size()][3];
		for (int i = 0; i < localPermissions.size(); i++) {
			rowData[i][0] = localPermissions.get(i).getUser().getUsername();
			rowData[i][1] = localPermissions.get(i).getUser().getName();
			// Handle correct casing of options
			final String perm = localPermissions.get(i).getPermLevel()
					.toString();
			rowData[i][2] = perm.substring(0, 1).concat(
					perm.substring(1).toLowerCase());
		}
		
		userTable = new PermissionsTable(rowData, columnNames, localPermissions);
		userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userTable
				.addMouseListener(new PermissionSelectionChangedListener(this));
		
		for (int i = 0; i < userTable.getRowCount(); i++) {
			if (userTable.isRowSelected(i)) {
				setSelectedButtons((UserPermissionLevel) userTable.getValueAt(
						i, 2));
				if (localPermissions.get(i).getUser().getUsername()
						.equals("admin")
						|| localPermissions.get(i).getUser().getUsername()
								.equals(model.getUser().getUsername())) {
					saveButton.setEnabled(false);
				}
			}
		}
		
		/** Construct the admin button */
		adminButton = new JRadioButton("Admin", false);
		
		/** Construct the update button */
		updateButton = new JRadioButton("Update", false);
		
		/** Construct the none button */
		noPermissionButton = new JRadioButton("None", false);
		
		final JScrollPane userScroll = new JScrollPane();
		userScroll.setBorder(null);
		userScroll.getViewport().add(userTable);
		
		// Create a group for all the buttons
		final ButtonGroup group = new ButtonGroup();
		group.add(adminButton);
		group.add(updateButton);
		group.add(noPermissionButton);
		
		// set constraints for the overall panel
		layout.putConstraint(SpringLayout.WEST, userScroll, 0,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, userScroll, 0,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, userScroll, 10,
				SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, userScroll, 0,
				SpringLayout.SOUTH, this);
		
		// add the buttons to the panel
		radioPanel.add(adminButton);
		radioPanel.add(updateButton);
		radioPanel.add(noPermissionButton);
		radioPanel.add(saveButton);
		radioPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Permission Level"));
		
		this.add(userScroll);
	}
	
	/**
	 * 
	 * @return The local copy of the database
	 */
	public List<PermissionModel> getLocalDatabase() {
		return localPermissions;
	}
	
	/**
	 * 
	 * @return The permission level selected on the radio buttons
	 */
	public UserPermissionLevel getPermission() {
		if (adminButton.isSelected()) {
			return UserPermissionLevel.ADMIN;
		} else if (updateButton.isSelected()) {
			return UserPermissionLevel.UPDATE;
		} else if (noPermissionButton.isSelected()) {
			return UserPermissionLevel.OBSERVE;
		}
		return null;
	}
	
	/**
	 * 
	 * @return The table of users and their permissions.
	 */
	public JTable getUserList() {
		return userTable;
	}
	
	@Override
	public void refresh() {
		localPermissions = PermissionsDatabase.getInstance().getAll();
		final String[][] rowData = new String[localPermissions.size()][3];
		final String[] columnNames = new String[3];
		columnNames[0] = "User";
		columnNames[1] = "Full Name";
		columnNames[2] = "Permission Level";
		for (int i = 0; i < localPermissions.size(); i++) {
			rowData[i][0] = localPermissions.get(i).getUser().getUsername();
			rowData[i][1] = localPermissions.get(i).getUser().getName();
			rowData[i][2] = localPermissions.get(i).getPermLevel().toString();
		}
		userTable = new PermissionsTable(rowData, columnNames, localPermissions);
	}
	
	/**
	 * 
	 * @param level
	 *            The level to set the radio buttons to reflect
	 */
	public void setSelectedButtons(final UserPermissionLevel level) {
		switch (level) {
			case ADMIN:
				adminButton.setSelected(true);
				updateButton.setSelected(false);
				noPermissionButton.setSelected(false);
				saveButton.setAction(new SavePermissionsAction(this,
						localPermissions.get(userTable.getSelectedRow())));
				break;
			case UPDATE:
				adminButton.setSelected(false);
				updateButton.setSelected(true);
				noPermissionButton.setSelected(false);
				saveButton.setAction(new SavePermissionsAction(this,
						localPermissions.get(userTable.getSelectedRow())));
				break;
			case OBSERVE:
				adminButton.setSelected(false);
				updateButton.setSelected(false);
				noPermissionButton.setSelected(true);
				saveButton.setAction(new SavePermissionsAction(this,
						localPermissions.get(userTable.getSelectedRow())));
				break;
			default:
				adminButton.setSelected(false);
				updateButton.setSelected(false);
				noPermissionButton.setSelected(false);
				saveButton.setEnabled(false);
				break;
		}
	}
}
