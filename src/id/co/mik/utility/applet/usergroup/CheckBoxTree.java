package id.co.mik.utility.applet.usergroup;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

/*
 Definitive Guide to Swing for Java 2, Second Edition
 By John Zukowski     
 ISBN: 1-893115-78-X
 Publisher: APress
 */

/*
 * Modified by Robert Julius
 * 
 * Oct 21, 2012
 */
public class CheckBoxTree<K, V> extends JTree {

	private static final long serialVersionUID = 1L;
	private ArrayList<V> selectedList;

	public CheckBoxTree(Object[] nodes, ArrayList<V> selectedList) {
		super(nodes);

		CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
		setCellRenderer(renderer);

		setCellEditor(new CheckBoxNodeEditor(this));
		setEditable(true);

		this.selectedList = selectedList;
	}

	public ArrayList<V> getAllSelected() {
		return selectedList;
	}

	@Override
	public void expandRow(int row) {
		super.expandRow(row);

		TreePath treePath = getPathForRow(row);
		Object[] nodes = treePath.getPath();

		for (Object object : nodes) {
			Object node = ((DefaultMutableTreeNode) object).getUserObject();
			if (node instanceof CheckBoxNode) {
				@SuppressWarnings("unchecked")
				CheckBoxNode<V> checkBoxNode = (CheckBoxNode<V>) node;
				if (selectedList.contains(checkBoxNode.getValue())) {
					checkBoxNode.setSelected(true);
				}
			}
		}
	}

	private class CheckBoxNodeRenderer implements TreeCellRenderer {

		private JCheckBox leafRenderer = new JCheckBox();
		private DefaultTreeCellRenderer nonLeafRenderer = new DefaultTreeCellRenderer();
		// private Color selectionBorderColor;
		private Color selectionForeground;
		private Color selectionBackground;
		private Color textForeground;
		private Color textBackground;
		private CheckBoxNode<V> leafCheckBoxNode = new CheckBoxNode<V>(null,
				false);

		public CheckBoxNode<V> getLeafCheckBoxNode() {
			return leafCheckBoxNode;
		}

		protected JCheckBox getLeafRenderer() {
			return leafRenderer;
		}

		public CheckBoxNodeRenderer() {
			Font fontValue;
			fontValue = UIManager.getFont("Tree.font");
			if (fontValue != null) {
				leafRenderer.setFont(fontValue);
			}
			Boolean booleanValue = (Boolean) UIManager
					.get("Tree.drawsFocusBorderAroundIcon");
			leafRenderer.setFocusPainted((booleanValue != null)
					&& (booleanValue.booleanValue()));

			nonLeafRenderer.setFont(fontValue.deriveFont(Font.BOLD));
			nonLeafRenderer.setOpenIcon(null);
			nonLeafRenderer.setClosedIcon(null);

			// selectionBorderColor =
			// UIManager.getColor("Tree.selectionBorderColor");
			selectionForeground = UIManager
					.getColor("Tree.selectionForeground");
			selectionBackground = UIManager
					.getColor("Tree.selectionBackground");
			textForeground = UIManager.getColor("Tree.textForeground");
			textBackground = UIManager.getColor("Tree.textBackground");
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {

			Component returnValue;
			if (leaf) {

				String stringValue = tree.convertValueToText(value, selected,
						expanded, leaf, row, false);
				leafRenderer.setText(stringValue);
				leafRenderer.setSelected(false);

				leafRenderer.setEnabled(tree.isEnabled());

				if (selected) {
					leafRenderer.setForeground(selectionForeground);
					leafRenderer.setBackground(selectionBackground);
				} else {
					leafRenderer.setForeground(textForeground);
					leafRenderer.setBackground(textBackground);
				}

				if ((value != null)
						&& (value instanceof DefaultMutableTreeNode)) {
					Object userObject = ((DefaultMutableTreeNode) value)
							.getUserObject();
					if (userObject instanceof CheckBoxNode) {

						@SuppressWarnings("unchecked")
						CheckBoxNode<V> node = (CheckBoxNode<V>) userObject;

						leafRenderer.setText(node.toString());
						leafRenderer.setSelected(node.isSelected());

						leafCheckBoxNode.setValue(node.getValue());
					}
				}
				returnValue = leafRenderer;
			} else {
				returnValue = nonLeafRenderer.getTreeCellRendererComponent(
						tree, value, selected, expanded, leaf, row, hasFocus);
				if (selected) {
					nonLeafRenderer.setForeground(selectionForeground);
					nonLeafRenderer.setBackground(selectionBackground);
				} else {
					nonLeafRenderer.setForeground(Color.BLUE);
					nonLeafRenderer.setBackground(textBackground);
				}
			}

			return returnValue;
		}
	}

	private class CheckBoxNodeEditor extends AbstractCellEditor implements
			TreeCellEditor {

		private static final long serialVersionUID = 1L;
		CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
		JTree tree;

		public CheckBoxNodeEditor(JTree tree) {
			this.tree = tree;
		}

		@Override
		public Object getCellEditorValue() {
			JCheckBox checkbox = renderer.getLeafRenderer();
			CheckBoxNode<V> leafBoxNode = renderer.getLeafCheckBoxNode();

			return new CheckBoxNode<V>(leafBoxNode.getValue(),
					checkbox.isSelected());
		}

		@Override
		public boolean isCellEditable(EventObject event) {
			boolean returnValue = false;
			if (event instanceof MouseEvent) {
				MouseEvent mouseEvent = (MouseEvent) event;
				TreePath path = tree.getPathForLocation(mouseEvent.getX(),
						mouseEvent.getY());
				if (path != null) {
					Object node = path.getLastPathComponent();
					if ((node != null)
							&& (node instanceof DefaultMutableTreeNode)) {
						DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
						Object userObject = treeNode.getUserObject();
						returnValue = ((treeNode.isLeaf()) && (userObject instanceof CheckBoxNode));

						if (userObject instanceof CheckBoxNode) {

							@SuppressWarnings("unchecked")
							CheckBoxNode<V> checkBoxNode = (CheckBoxNode<V>) userObject;

							/*
							 * If checkBoxNode.isSelected mean that old value
							 * TRUE and new value will be FALSE, vise versa
							 */

							if (!checkBoxNode.isSelected()
									&& !selectedList.contains(checkBoxNode
											.getValue())) {
								selectedList.add(checkBoxNode.getValue());
							} else if (checkBoxNode.isSelected()) {
								selectedList.remove(checkBoxNode.getValue());
							}
						}
					}
				}
			}
			return returnValue;
		}

		@Override
		public Component getTreeCellEditorComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row) {

			Component editor = renderer.getTreeCellRendererComponent(tree,
					value, true, expanded, leaf, row, true);

			// editor always selected / focused
			ItemListener itemListener = new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent itemEvent) {
					if (stopCellEditing()) {
						fireEditingStopped();
					}
				}
			};
			if (editor instanceof JCheckBox) {
				((JCheckBox) editor).addItemListener(itemListener);
			}

			return editor;
		}
	}
}
