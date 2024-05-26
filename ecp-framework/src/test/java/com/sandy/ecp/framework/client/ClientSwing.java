package com.sandy.ecp.framework.client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.synth.SynthLookAndFeel;

public class ClientSwing extends JFrame {

	private static final long serialVersionUID = 1L;
	public ClientSwing() throws UnsupportedLookAndFeelException, ParseException {
		SwingUtilities.invokeLater ( new Runnable () {
			public void run() {
				SynthLookAndFeel lookAndFeel = new SynthLookAndFeel();
				//class.getResourceAsStream("synth/style.xml")
				InputStream in = getClass().getResourceAsStream("/synth/style.xml");
				try {
					lookAndFeel.load(in, ClientSwing.class);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				//UIManager.setLookAndFeel(this);
				SwingUtilities.updateComponentTreeUI(ClientSwing.this);
			};
		});
		setTitle("我的测试");
		JButton title = new JButton("aaa");
		this.getContentPane().add(title);
		this.initEvent();
	}
	
	public void initEvent() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
				super.windowClosing(e);
			}
		});
	}
	
	public static void main(String[] args) throws UnsupportedLookAndFeelException, ParseException {
		ClientSwing client = new ClientSwing();
		client.setSize(700, 470);
		client.setLocation(200, 200);
		client.setVisible(true);
	}
}
