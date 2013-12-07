package tk.gbl.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import tk.gbl.core.HttpServer;


/**
 * 主窗体 设置端口 和 webapp所在路径
 * @author Don'T PH 7.0
 * 2013-8-30
 */
public class MainFrame extends JFrame implements ActionListener{
	
	JTextField portText;
	JTextField pathText;
	
	HttpServer server;
	
	public MainFrame(){
		this.setBounds(200,100,800,470);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		
		init();
		this.setVisible(true);
	}
	
	public void init(){
		JLabel portLabel = new JLabel();
		portLabel.setText("开放端口：");
		portLabel.setBounds(100, 50, 100, 50);
		this.add(portLabel);
		
		portText = new JTextField();
		portText.setText("80");
		portText.setBounds(200, 50, 100, 50);
		this.add(portText);
		
		JLabel pathLabel = new JLabel();
		pathLabel.setText("所在路径：");
		pathLabel.setBounds(100, 150, 100, 50);
		this.add(pathLabel);
		
		pathText = new JTextField();
		pathText.setText("C:/Users/Administrator/Desktop/mytest/");
		pathText.setBounds(200, 150, 300, 50);
		this.add(pathText);
		
		JButton startButton = new JButton();
		startButton.setText("开始");
		startButton.setBounds(100,250,100,100);
		startButton.setActionCommand("start");
		startButton.addActionListener(this);
		this.add(startButton);
		
		JButton endButton = new JButton();
		endButton.setText("停止");
		endButton.setBounds(400,250,100,100);
		endButton.setActionCommand("end");
		endButton.addActionListener(this);
		this.add(endButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("start")){
			server = new HttpServer(portText.getText(),pathText.getText());
			server.start();
		}
		if(e.getActionCommand().equals("end")){
			server.setStart(false);
		}
	}

}
