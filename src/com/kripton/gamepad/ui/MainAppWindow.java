package com.kripton.gamepad.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.JComboBox;

public class MainAppWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainAppWindow window = new MainAppWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainAppWindow() {
		con = new Controller();
		initialize();
		//con.start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("MyApp");
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				con.closePort();
			}
			
		});
		
		JLabel lblComPort = new JLabel("Arduino COM Port");
		lblComPort.setBounds(12, 12, 137, 15);
		frame.getContentPane().add(lblComPort);
		
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(12, 60, 137, 25);
		frame.getContentPane().add(btnConnect);
		
		final JButton btnConnectFinal = btnConnect;
		
		
		btnConnect.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) 
			{
				if(!connected)
				{
					String portName = (String)comboBox.getSelectedItem();
					System.out.println("Selected item: "+portName);
					con.openCOMPort(portName, 57600);
					btnConnectFinal.setText("Disconnect");
					connected = true;
				}
				else
				{
					con.closePort();
					btnConnectFinal.setText("Connect");
					connected = false;
				}
				
				
			}
		});
		
		
		JLabel lblComLog = new JLabel("COM Log");
		lblComLog.setBounds(22, 101, 70, 15);
		frame.getContentPane().add(lblComLog);
		
		/* Create Scroll pane for text area with COM INFO */
		JScrollPane COMInfoScroll = new JScrollPane();
		COMInfoScroll.setBounds(12, 128, 321, 391);
		COMInfoScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		COMInfoScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		COMInfoScroll.setAutoscrolls(true);
		frame.getContentPane().add(COMInfoScroll);
		txtrComInfo = new JTextArea();
		COMInfoScroll.setViewportView(txtrComInfo);
		txtrComInfo.setBorder(new LineBorder(new Color(153, 153, 153), 1, true));
		txtrComInfo.setEditable(false);
		txtrComInfo.setBackground(new Color(238, 238, 238));
		txtrComInfo.setText("COM INFO");
		txtrComInfo.setAutoscrolls(true);
		DefaultCaret caret = (DefaultCaret) txtrComInfo.getCaret();
		con.setCOMInfoArea(txtrComInfo);
		
		/* Text area with COM INFO */
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		
		JButton clearCOMLogBtn = new JButton("Clear");
		clearCOMLogBtn.setBounds(12, 531, 137, 25);
		frame.getContentPane().add(clearCOMLogBtn);
		
		clearCOMLogBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtrComInfo.setText("");
			}
		});
		
		
	
		
		
		/* For updating values */
		
		JLabel lblJoystickInfo = new JLabel("Joystick info");
		lblJoystickInfo.setBounds(372, 22, 105, 15);
		frame.getContentPane().add(lblJoystickInfo);
		
		JLabel lblSlider = new JLabel("slider:");
		lblSlider.setName("slider");
		lblSlider.setBounds(372, 37, 130, 15);
		frame.getContentPane().add(lblSlider);
		
		comboBox = new JComboBox<String>();
		comboBox.setBounds(12, 30, 137, 24);
		frame.getContentPane().add(comboBox);
		

		con.showCOMPorts(comboBox);
		
		JLabel lblQuadroInfo = new JLabel("Quadro info");
		lblQuadroInfo.setBounds(372, 277, 105, 15);
		frame.getContentPane().add(lblQuadroInfo);
		
		JLabel lblGyro = new JLabel("Gyro:");
		lblGyro.setName("gyro");
		lblGyro.setBounds(372, 315, 81, 15);
		frame.getContentPane().add(lblGyro);
		
		JLabel lblAccel = new JLabel("Accel:");
		lblAccel.setName("accel");
		lblAccel.setBounds(372, 330, 100, 15);
		frame.getContentPane().add(lblAccel);
		
		JLabel lblTrigger = new JLabel("trigger:");
		lblTrigger.setName("Trigger");
		lblTrigger.setBounds(372, 67, 100, 15);
		frame.getContentPane().add(lblTrigger);
		
		JLabel lblThumb = new JLabel("thumb:");
		lblThumb.setName("Thumb");
		lblThumb.setBounds(372, 82, 100, 15);
		frame.getContentPane().add(lblThumb);
		
		JLabel lblThumb_1 = new JLabel("thumb2:");
		lblThumb_1.setName("Thumb 2");
		lblThumb_1.setBounds(372, 112, 100, 15);
		frame.getContentPane().add(lblThumb_1);
		
		JLabel lblTop = new JLabel("top:");
		lblTop.setName("Top");
		lblTop.setBounds(372, 52, 100, 15);
		frame.getContentPane().add(lblTop);
		
		JLabel lblTop_1 = new JLabel("top2:");
		lblTop_1.setName("Top 2");
		lblTop_1.setBounds(372, 97, 100, 15);
		frame.getContentPane().add(lblTop_1);
		
		JLabel lblPinkie = new JLabel("pinkie:");
		lblPinkie.setName("Pinkie");
		lblPinkie.setBounds(372, 127, 100, 15);
		frame.getContentPane().add(lblPinkie);
		
		JLabel lblBase = new JLabel("base:");
		lblBase.setName("Base");
		lblBase.setBounds(372, 157, 100, 15);
		frame.getContentPane().add(lblBase);
		
		JLabel lblBase_2 = new JLabel("base2:");
		lblBase_2.setName("Base 2");
		lblBase_2.setBounds(372, 172, 100, 15);
		frame.getContentPane().add(lblBase_2);
		
		JLabel lblBase_1 = new JLabel("base3:");
		lblBase_1.setName("Base 3");
		lblBase_1.setBounds(372, 142, 100, 15);
		frame.getContentPane().add(lblBase_1);
		
		JLabel lblBase_3 = new JLabel("base4:");
		lblBase_3.setName("Base 4");
		lblBase_3.setBounds(372, 187, 100, 15);
		frame.getContentPane().add(lblBase_3);
		
		JLabel lblX = new JLabel("x:");
		lblX.setName("x");
		lblX.setBounds(372, 202, 100, 15);
		frame.getContentPane().add(lblX);
		
		JLabel lblY = new JLabel("y:");
		lblY.setName("y");
		lblY.setBounds(372, 217, 100, 15);
		frame.getContentPane().add(lblY);
		
		JLabel lblZ = new JLabel("z:");
		lblZ.setName("z");
		lblZ.setBounds(372, 232, 100, 15);
		frame.getContentPane().add(lblZ);
		
		JLabel lblRz = new JLabel("rz:");
		lblRz.setName("rz");
		lblRz.setBounds(372, 247, 100, 15);
		frame.getContentPane().add(lblRz);
		
		JLabel lblPov = new JLabel("pov:");
		lblPov.setName("pov");
		lblPov.setBounds(372, 262, 100, 15);
		frame.getContentPane().add(lblPov);
		
		
		/* For output gamepad values */
		Map<String, JLabel> writeLbls = new HashMap<String, JLabel>();
		Map<String, JLabel> readLbls = new HashMap<String, JLabel>();
		
		writeLbls.put("0", lblTrigger);
		writeLbls.put("1", lblThumb);
		writeLbls.put("2", lblThumb_1);
		writeLbls.put("3", lblTop);
		writeLbls.put("4", lblTop_1);
		writeLbls.put("5", lblPinkie);
		writeLbls.put("6", lblBase);
		writeLbls.put("7", lblBase_1);
		writeLbls.put("8", lblBase_2);
		writeLbls.put("9", lblBase_3);
		writeLbls.put("10", lblX);
		writeLbls.put("11", lblY);
		writeLbls.put("12", lblZ);
		writeLbls.put("13", lblRz);
		writeLbls.put("14", lblSlider);
		writeLbls.put("15", lblPov);
		
		readLbls.put("1", lblGyro);
		readLbls.put("2", lblAccel);
		
		
		JButton openGraphButton = new JButton("Open graph");
		openGraphButton.setBounds(184, 531, 149, 25);
		frame.getContentPane().add(openGraphButton);
		
		con.setInfoLabels(writeLbls, readLbls);
		
		openGraphButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				ChartFrame fr = new ChartFrame("Real time plotting");
				con.openChartFrame(fr);
				fr.setVisible(true);
				fr.opened = true;
			}
			
		});
		
	}
	
	
	static Controller con;
	JComboBox<String> comboBox;
	JTextArea txtrComInfo;
	
	private boolean connected = false;
}
