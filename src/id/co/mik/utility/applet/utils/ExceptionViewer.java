package id.co.mik.utility.applet.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ExceptionViewer extends JDialog {

	private static final long serialVersionUID = -3491290769308831690L;

	private JButton buttonClose;
	private JLabel labelMessage;
	private JScrollPane scrollPaneStackTrace;
	private JTextArea textAreaStackTrace;
	
	public static void showException(Frame parent, Exception e) {
		new ExceptionViewer(parent, e).setVisible(true);
	}

	private ExceptionViewer(Frame parent, Exception e) {
		super(parent, true);

		initComponents();

		labelMessage
				.setText(e.getMessage() == null ? "<null>" : e.getMessage());
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		textAreaStackTrace.setText(sw.toString());

		pack();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void initComponents() {
		setTitle("The system found unexpected problem");

		JLabel label = new JLabel("Message: ");
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		labelMessage = new JLabel();

		JPanel topPanel = new JPanel();
		((FlowLayout) topPanel.getLayout()).setAlignment(FlowLayout.LEFT);
		topPanel.add(label);
		topPanel.add(labelMessage);

		textAreaStackTrace = new JTextArea();
		textAreaStackTrace.setFont(labelMessage.getFont());
		textAreaStackTrace.setEditable(false);

		scrollPaneStackTrace = new JScrollPane();
		scrollPaneStackTrace.setPreferredSize(new Dimension(500, 150));
		scrollPaneStackTrace.setViewportView(textAreaStackTrace);

		JPanel centerPanel = new JPanel();
		centerPanel.add(scrollPaneStackTrace);

		buttonClose = new JButton("Close");
		buttonClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ExceptionViewer.this.dispose();
			}
		});

		JPanel bottomPanel = new JPanel();
		bottomPanel.add(buttonClose);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

		getContentPane().add(mainPanel);

	}
}
