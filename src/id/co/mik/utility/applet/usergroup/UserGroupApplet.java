package id.co.mik.utility.applet.usergroup;

import id.co.mik.utility.applet.utils.ExceptionViewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultTreeCellRenderer;

public class UserGroupApplet extends JApplet {

	private static final long serialVersionUID = 1L;

	private JLabel lblCollapseAll;
	private JLabel lblExpandAll;
	private JTree tree;

	public void collapseAll() {
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.collapseRow(i);
		}
	}

	public void createCheckBoxTree() {
		try {
			// String privilegeIds = getParameter("privilegeIds");
			String privilegeIds = "012,021";
			String[] initialPrivilegeIds = privilegeIds.split(",");

			TreeMap<String, Privilege> treeMap = PrivilegeUtils.getAllMenus();
			Object[] objects = TreeNodeUtils.generateCheckBoxTreeNode(treeMap);

			ArrayList<Privilege> selectedList = PrivilegeUtils
					.getPrivileges(initialPrivilegeIds);
			this.tree = new CheckBoxTree<String, Privilege>(objects,
					selectedList);
		} catch (Exception e) {
			ExceptionViewer.showException(null, e);
		}
	}

	public void createConfirmTree() {
		String privilegeIds = getParameter("privilegeIds");
		String[] initialPrivilegeIds = privilegeIds.split(",");

		TreeMap<String, Privilege> treeMap = PrivilegeUtils
				.generateReadOnlyTree(initialPrivilegeIds);
		Object[] objects = TreeNodeUtils.generateCommonTreeNode(treeMap);

		this.tree = new JTree(objects);

		((DefaultTreeCellRenderer) this.tree.getCellRenderer())
				.setOpenIcon(null);
		((DefaultTreeCellRenderer) this.tree.getCellRenderer())
				.setClosedIcon(null);
	}

	public void expandAll() {
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
	}

	public String getSelected() {
		if (tree instanceof CheckBoxTree) {
			String string = "";
			ArrayList<?> list = ((CheckBoxTree<?, ?>) tree).getAllSelected();
			for (Object object : list) {
				Privilege privilege = (Privilege) object;
				string += "," + privilege.getId();
			}
			return string.replaceFirst(",", "");
		} else {
			ExceptionViewer.showException(null, new Exception(
					"Method getSelected only can be used for CheckBoxTree"));
			return null;
		}
	}
	
	@Override
	public void start() {
		createCheckBoxTree();
		initComponent();
		expandAll();
	}

	@Override
	public void init() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			ExceptionViewer.showException(null, e);
		} catch (InstantiationException e) {
			ExceptionViewer.showException(null, e);
		} catch (IllegalAccessException e) {
			ExceptionViewer.showException(null, e);
		} catch (UnsupportedLookAndFeelException e) {
			ExceptionViewer.showException(null, e);
		}
	}

	public void initComponent() {
		lblExpandAll = new JLabel("Expand All");
		lblExpandAll.setForeground(Color.BLUE);
		lblExpandAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserGroupApplet.this.expandAll();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblExpandAll.setCursor(Cursor
						.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblExpandAll.setCursor(Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

		lblCollapseAll = new JLabel("Collapse All");
		lblCollapseAll.setForeground(Color.BLUE);
		lblCollapseAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserGroupApplet.this.collapseAll();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblCollapseAll.setCursor(Cursor
						.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblCollapseAll.setCursor(Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

		JPanel topPanel = new JPanel();
		((FlowLayout) topPanel.getLayout()).setAlignment(FlowLayout.LEFT);
		topPanel.setBackground(Color.WHITE);
		topPanel.add(lblExpandAll);
		topPanel.add(new JLabel("   |   "));
		topPanel.add(lblCollapseAll);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setPreferredSize(new Dimension(300, 1000));
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(tree, BorderLayout.CENTER);
		tree.setBackground(Color.WHITE);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(mainPanel);

		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(BorderFactory.createLoweredBevelBorder());
		contentPane.add(scrollPane);
	}
}