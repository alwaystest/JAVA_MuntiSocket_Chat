package multisocket.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import multisocket.event.MultiSocketEvent;
import multisocket.event.MultiSocketListener;
import multisocket.net.ConfigFiles;
import multisocket.net.MultiDatagramPacket;
import multisocket.net.MultiSocket;
/**
 * GUI Client.
 *
 */
public class Client extends JFrame implements ActionListener,
		MultiSocketListener {

	private JPanel centerPanel;

	private JList userList;

	private JPanel leftPanel;
	private JPanel leftDownPanel;

	private JTextField sendMessage;

	private JTextArea display;

	private JLabel user;
	private JCheckBox whisper;

	private JButton colorSelectButton;
	private JButton renameButton;
	private JButton sendButton;

	private JToggleButton allUser;

	private MultiSocket socket;

	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public Client(String arg0) throws HeadlessException {
		super(arg0);

		try {
			socket = new MultiSocket(ConfigFiles.SERVER_CONFIG);
			socket.addMultiSocketListener(this);
		} catch (IOException e) {
			e.printStackTrace();
		}

		setSize(600, 600);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}
		});
		setResizable(false);

		// layout
		Container ct = getContentPane();
		ct.setLayout(null);
		initCenterPanel();
		ct.add(centerPanel);

		JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		initUserList();

		initLeftPanel();
		jsp.setLeftComponent(leftPanel);
		jsp.setRightComponent(new JScrollPane(userList));
		centerPanel.add(jsp, BorderLayout.CENTER);
		jsp.setDividerLocation(440);
		jsp.setContinuousLayout(true);
		jsp.setOneTouchExpandable(true);

		setVisible(true);
		sendMessage.requestFocus();

		try {
			socket.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initCenterPanel() {
		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBounds(15, 20, 565, 530);
		// centerPanel.setBorder(BorderFactory.createLineBorder(Color.red));
	}

	private void initUserList() {
		DefaultListModel model = new DefaultListModel();
		userList = new JList(model);
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					String selectionValue = userList.getSelectedValue()
							.toString();
					user.setText("发给：" + selectionValue);
				}

			}
		});

	}

	private void initLeftPanel() {
		leftPanel = new JPanel(new BorderLayout(5, 5));
		initDisplay();
		leftPanel.add(new JScrollPane(display), BorderLayout.CENTER);
		initLeftDownPanel();
		leftPanel.add(leftDownPanel, BorderLayout.SOUTH);
	}

	private void initDisplay() {
		display = new JTextArea();
		display.setLineWrap(true);
		display.setWrapStyleWord(true);
		display.setToolTipText("按 ALT+C 清空：）");
		display.setEditable(false);
	}

	private void initLeftDownPanel() {
		leftDownPanel = new JPanel(new GridLayout(3, 1));
		initSendMessage();

		JPanel jp1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		initUser();
		initWhisper();
		jp1.add(user);

		initAllUser();
		jp1.add(allUser);

		jp1.add(whisper);
		leftDownPanel.add(jp1);
		leftDownPanel.add(sendMessage);

		initColorSelectButton();
		initRenameButton();
		initSendButton();
		JPanel jp2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		jp2.add(colorSelectButton);
		jp2.add(renameButton);
		jp2.add(sendButton);

		leftDownPanel.add(jp2);
	}

	private void initSendMessage() {
		sendMessage = new JTextField();
		sendMessage.addActionListener(this);
		sendMessage.registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				display.setText("");

			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK, false),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	private void initUser() {
		user = new JLabel("发送：所有人");
		user.setPreferredSize(new Dimension(100, 30));
	}

	private void initWhisper() {
		whisper = new JCheckBox("悄悄");
	}

	private void initColorSelectButton() {
		colorSelectButton = new JButton("选色S");
		colorSelectButton.setMnemonic('S');
		colorSelectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(Client.this,
						"请选择窗体背景色：", Color.red);
				Client.this.getContentPane().setBackground(color);

			}
		});
	}

	private void initRenameButton() {
		renameButton = new JButton("改名R");
		renameButton.setMnemonic('R');
		renameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String newName = JOptionPane.showInputDialog(Client.this,
						"请输入新名字：", "", JOptionPane.INFORMATION_MESSAGE);
				if (newName != null && newName.trim().length() > 0) {
					MultiDatagramPacket packet = MultiDatagramPacket
							.createUserRenamePacket(newName,
									socket.getUsername());
					socket.sendPacket(packet);
				}

			}
		});

	}

	private void initSendButton() {
		sendButton = new JButton("发送");
		sendButton.addActionListener(this);
	}

	private void initAllUser() {
		allUser = new JToggleButton("所有人");
		allUser.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					user.setText("发给：所有人");
					whisper.setEnabled(false);
				} else {
					Object obj = userList.getSelectedValue();
					if (obj == null) {
						whisper.setEnabled(true);
					} else {
						user.setText("发给：" + obj.toString());
						whisper.setEnabled(true);
					}
				}

			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String message = sendMessage.getText().trim();
		if(message.length() > 0){
			String send = socket.getUsername();
			String receive = (allUser.isSelected() || userList.getSelectedValue()==null)? null:userList.getSelectedValue().toString();
			boolean isWhisper = whisper.isSelected();
			MultiDatagramPacket p1 = MultiDatagramPacket.createSendWhisperMessagePacket(send, receive, isWhisper, message);
			socket.sendPacket(p1);
		}


	}

	@Override
	public void accept(MultiSocketEvent e) {
	}

	@Override
	public void connect(MultiSocketEvent e) {
		System.out.println("client connect ok....");

	}

	@Override
	public void read(MultiSocketEvent e) {
		MultiDatagramPacket packet = e.getPacket();
		DefaultListModel model = (DefaultListModel) userList.getModel();
		if (packet.getPacketType() == MultiDatagramPacket.ALLOC_USERNAME) {
			setTitle("我的聊天室：" + packet.getEnterExitSendAllocNewName());
			socket.setUsername(packet.getEnterExitSendAllocNewName());
		} else if (packet.getPacketType() == MultiDatagramPacket.ONLINE_USERLIST) {
			for (String ele : packet.getUserlist()) {
				model.addElement(ele);
			}
		} else if (packet.getPacketType() == MultiDatagramPacket.USER_ENTER) {
			model.addElement(packet.getEnterExitSendAllocNewName());
			display.append("新用户进入：" + packet.getEnterExitSendAllocNewName());
			display.append("\n");
		} else if (packet.getPacketType() == MultiDatagramPacket.USER_EXIT) {
			model.removeElement(packet.getEnterExitSendAllocNewName());
			display.append("用户离开：" + packet.getEnterExitSendAllocNewName());
			display.append("\n");
		} else if (packet.getPacketType() == MultiDatagramPacket.USER_RENAME) {
			model.setElementAt(packet.getEnterExitSendAllocNewName(),
					model.indexOf(packet.getReceiveOldName()));
			if (packet.getReceiveOldName().equals(socket.getUsername())) {
				setTitle("我的聊天室：" + packet.getEnterExitSendAllocNewName());
				e.getSocket()
						.setUsername(packet.getEnterExitSendAllocNewName());
			}

			display.append("用户 ");
			display.append(packet.getReceiveOldName());
			display.append(" 改名为：");
			display.append(packet.getEnterExitSendAllocNewName());
			display.append("\n");
		} else if (packet.getPacketType() == MultiDatagramPacket.SEND_MESSAGE) {
			processSendMessage(packet);
		}

	}

	private void processSendMessage(MultiDatagramPacket packet) {

		String sendName = packet.getEnterExitSendAllocNewName();
		String receiveName = packet.getReceiveOldName();
		/*
		 * 系统公告
		 */
		if (sendName == null && receiveName == null) {
			display.append("系统公告：" + packet.getMessage());
			display.append("\n");
		} else {
			if (sendName == null) {
				display.append("系统 --- ");
			} else {
				display.append(sendName + " --- ");
			}
			if (packet.isWhisper()) {
				display.append("（悄悄）--- ");
			}
			if (receiveName == null) {
				display.append("系统： ");
			} else {
				display.append(receiveName + "：");
			}
			display.append(packet.getMessage());
			display.append("\n");
		}
	}

	@Override
	public void write(MultiSocketEvent e) {

	}

	@Override
	public void close(MultiSocketEvent e) {
		System.out.println("服务断开连接。");
		System.exit(0);
	}

}
