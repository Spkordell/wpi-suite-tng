/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.subrequirementsTree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

@SuppressWarnings("serial")
public class SubReqTreeTransferHandler extends TransferHandler implements
		ISaveNotifier {
	DataFlavor nodesFlavor;
	DataFlavor[] flavors = new DataFlavor[1];
	DefaultMutableTreeNode[] nodesToRemove;
	private MainTabController tabController;
	private Requirement draggedRequirement;

	public SubReqTreeTransferHandler(MainTabController tabController) {
		this.tabController = tabController;
		try {
			String mimeType = DataFlavor.javaJVMLocalObjectMimeType
					+ ";class=\""
					+ javax.swing.tree.DefaultMutableTreeNode[].class.getName()
					+ "\"";
			nodesFlavor = new DataFlavor(mimeType);
			flavors[0] = nodesFlavor;
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFound: " + e.getMessage());
		}
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport support) {
		/*
		 * if (!support.isDrop()) { return false; }
		 * support.setShowDropLocation(true); if
		 * (!support.isDataFlavorSupported(nodesFlavor)) { return false; }
		 */
		// Do not allow a drop on the drag source selections.
		JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
		JTree tree = (JTree) support.getComponent();

		int[] selRows = tree.getSelectionRows();

		// Do not allow a non-leaf node to be copied to a level
		// which is less than its source level.
		TreePath dest = dl.getPath();
		DefaultMutableTreeNode target = (DefaultMutableTreeNode) dest
				.getLastPathComponent();
		TreePath path = tree.getPathForRow(selRows[0]);
		DefaultMutableTreeNode firstNode = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		if (firstNode.getLevel() == 0)
			return false;
		if (firstNode == target || target == firstNode.getParent())
			return false;

		return true;
	}

	private boolean haveCompleteNode(JTree tree) {
		int[] selRows = tree.getSelectionRows();
		TreePath path = tree.getPathForRow(selRows[0]);
		DefaultMutableTreeNode first = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		int childCount = first.getChildCount();
		// first has children and no children are selected.
		if (childCount > 0 && selRows.length == 1)
			return false;
		// first may have children.
		for (int i = 1; i < selRows.length; i++) {
			path = tree.getPathForRow(selRows[i]);
			DefaultMutableTreeNode next = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			if (first.isNodeChild(next)) {
				// Found a child of first.
				if (childCount > selRows.length - 1) {
					// Not all children of first are selected.
					return false;
				}
			}
		}
		return true;
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		JTree tree = (JTree) c;
		TreePath[] paths = tree.getSelectionPaths();
		if (paths != null) {
			// Make up a node array of copies for transfer and
			// another for/of the nodes that will be removed in
			// exportDone after a successful drop.
			List<DefaultMutableTreeNode> copies = new ArrayList<DefaultMutableTreeNode>();
			List<DefaultMutableTreeNode> toRemove = new ArrayList<DefaultMutableTreeNode>();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[0]
					.getLastPathComponent();
			DefaultMutableTreeNode copy = copy(node);
			copies.add(copy);
			toRemove.add(node);
			for (int i = 1; i < paths.length; i++) {
				DefaultMutableTreeNode next = (DefaultMutableTreeNode) paths[i]
						.getLastPathComponent();
				// Do not allow higher level nodes to be added to list.
				if (next.getLevel() < node.getLevel()) {
					break;
				} else if (next.getLevel() > node.getLevel()) { // child node
					copy.add(copy(next));
					// node already contains child
				} else { // sibling
					copies.add(copy(next));
					toRemove.add(next);
				}
			}
			DefaultMutableTreeNode[] nodes = copies
					.toArray(new DefaultMutableTreeNode[copies.size()]);
			nodesToRemove = toRemove
					.toArray(new DefaultMutableTreeNode[toRemove.size()]);
			return new NodesTransferable(nodes);
		}
		return null;
	}

	/** Defensive copy used in createTransferable. */
	private DefaultMutableTreeNode copy(TreeNode node) {
		return new DefaultMutableTreeNode(node);
	}

	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {
		if ((action & MOVE) == MOVE) {
			JTree tree = (JTree) source;
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			// Remove nodes saved in nodesToRemove in createTransferable.
			for (int i = 0; i < nodesToRemove.length; i++) {
				model.removeNodeFromParent(nodesToRemove[i]);
			}
		}
	}

	@Override
	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}

	@Override
	public boolean importData(TransferHandler.TransferSupport support) {
		if (!canImport(support)) {
			return false;
		}
		// Extract transfer data.
		DefaultMutableTreeNode[] nodes = null;
		try {
			Transferable t = support.getTransferable();
			nodes = (DefaultMutableTreeNode[]) t.getTransferData(nodesFlavor);
		} catch (UnsupportedFlavorException ufe) {
			System.out.println("UnsupportedFlavor: " + ufe.getMessage());
		} catch (java.io.IOException ioe) {
			System.out.println("I/O error: " + ioe.getMessage());
		}
		// Get drop location info.
		JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
		int childIndex = dl.getChildIndex();
		TreePath dest = dl.getPath();
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) dest
				.getLastPathComponent();
		JTree tree = (JTree) support.getComponent();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		// Configure for drop mode.
		int index = childIndex; // DropMode.INSERT
		if (childIndex == -1) { // DropMode.ON
			index = parent.getChildCount();
		}
		// Add data to model
		// for (int i = 0; i < nodes.length; i++) {
		// model.insertNodeInto(nodes[i], parent, index++);
		int[] selRows = tree.getSelectionRows();
		dest = dl.getPath();
		TreePath path = tree.getPathForRow(selRows[0]);

		DefaultMutableTreeNode target = (DefaultMutableTreeNode) dest
				.getLastPathComponent();
		DefaultMutableTreeNode firstNode = (DefaultMutableTreeNode) path
				.getLastPathComponent();

		RequirementsController RequirementsController = new RequirementsController();
		UpdateRequirementRequestObserver reqObserver = new UpdateRequirementRequestObserver(
				this);

		if (target.getLevel() != 0) {

			Requirement requirement = (Requirement) firstNode.getUserObject();
			Requirement anRequirement = (Requirement) target.getUserObject();
			Requirement parentRequirement = null;

			Integer anReqID = new Integer(anRequirement.getrUID());
			Integer reqID = new Integer(requirement.getrUID());

			if (requirement.getpUID().size() > 0) {
				try {
					parentRequirement = RequirementDatabase.getInstance().get(
							requirement.getpUID().get(0));
					parentRequirement.removeSubRequirement(reqID);
					requirement.removePUID(parentRequirement.getrUID());
					RequirementsController.save(parentRequirement, reqObserver);
				} catch (RequirementNotFoundException e) {
					e.printStackTrace();
				}
			}

			anRequirement.addSubRequirement(reqID);
			requirement.addPUID(anReqID);
			RequirementsController.save(anRequirement, reqObserver);
			RequirementsController.save(requirement, reqObserver);
			this.draggedRequirement = requirement;
		} else {

			Requirement requirement = (Requirement) firstNode.getUserObject();
			Requirement parentRequirement = null;
			Integer reqID = new Integer(requirement.getrUID());

			if (requirement.getpUID().size() > 0) {
				try {
					parentRequirement = RequirementDatabase.getInstance().get(
							requirement.getpUID().get(0));
					parentRequirement.removeSubRequirement(reqID);
					requirement.removePUID(parentRequirement.getrUID());
					RequirementsController.save(parentRequirement, reqObserver);
					RequirementsController.save(requirement, reqObserver);
				} catch (RequirementNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}

	@Override
	public String toString() {
		return getClass().getName();
	}

	private class NodesTransferable implements Transferable {
		DefaultMutableTreeNode[] nodes;

		private NodesTransferable(DefaultMutableTreeNode[] nodes) {
			this.nodes = nodes;
		}

		@Override
		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor))
				throw new UnsupportedFlavorException(flavor);
			return nodes;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return flavors;
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return nodesFlavor.equals(flavor);
		}
	}

	@Override
	public void responseSuccess() {
		if (getTabController() != null) {

			Requirement requirement = getDraggedRequirement();

			for (int i = 0; i < getTabController().getTabView().getTabCount(); i++) {
				if (getTabController().getTabView().getComponentAt(i) instanceof DetailPanel) {
					if (((((DetailPanel) getTabController().getTabView()
							.getComponentAt(i))).getModel().getrUID()) == (requirement
							.getrUID())) {
						try {
							(((DetailPanel) getTabController().getTabView()
									.getComponentAt(i)))
									.getComboBoxIteration()
									.setSelectedItem(
											IterationDatabase
													.getInstance()
													.get(
															requirement
																	.getIteration())
													.getName());
						} catch (IterationNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}

		}
	}

	@Override
	public void responseError(int statusCode, String statusMessage) {
		// TODO Auto-generated method stub
	}

	@Override
	public void fail(Exception exception) {
		// TODO Auto-generated method stub
	}

	/**
	 * @return the tabController
	 */
	public MainTabController getTabController() {
		return tabController;
	}

	public Requirement getDraggedRequirement() {
		return draggedRequirement;
	}

}