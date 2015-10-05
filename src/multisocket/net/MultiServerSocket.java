package multisocket.net;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import multisocket.event.MultiSocketEvent;
import multisocket.event.MultiSocketListener;
import multisocket.event.MultiSocketSupport;
/**
 * serverSocket for server
 * accept client socket, open a new thread to handle packets.
 *
 */
public class MultiServerSocket extends Thread {
	private Properties configProperties;
	private ServerSocket server;
	private MultiSocketSupport support;
	private ConcurrentHashMap<String, MultiSocket> onlineUserlist;

	public MultiServerSocket(String configFilePath) throws IOException {
		this.support = new MultiSocketSupport(this);
		this.onlineUserlist = new ConcurrentHashMap<String, MultiSocket>();
		configProperties = new Properties();
		try {
			configProperties.loadFromXML(new FileInputStream(configFilePath));
		} catch (IOException e) {
			throw new IOException("加载服务器配置文件失败。", e);
		}
		setDaemon(true);
	}

	public void run() {
		try {
			server = new ServerSocket(Integer.parseInt(configProperties
					.getProperty("serverPort")), 0,
					InetAddress.getByName(configProperties
							.getProperty("serverIp")));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		int index = 1;
		while (true) {
			Socket sk = null;
			try {
				sk = server.accept();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			MultiSocket socket = new MultiSocket(sk, support);
			String username = "Guest " + index;
			index++;
			socket.setUsername(username);
			onlineUserlist.put(username, socket);
			MultiDatagramPacket enterPacket = MultiDatagramPacket
					.createEnterPacket(username);
			support.fireMultiSocketEvent(socket,
					MultiSocketEvent.EventType.ACCEPT, enterPacket);
		}

	}

	public String[] getOnlineUserList() {
		Object[] obj = onlineUserlist.keySet().toArray();
		String[] value = new String[obj.length];
		for (int i = 0; i < obj.length; i++) {
			value[i] = obj[i].toString();
		}
		return value;

		// String temp = Arrays.toString(obj);
		// temp = temp.substring(1, temp.length()-1);
		// String[] value = temp.split(", ");
		// return value;
	}

	/**
	 * 向参数用户发送packet
	 * 
	 * @param username
	 * @param packet
	 */
	public void sendMessage(String username, MultiDatagramPacket packet) {
		if (username == null) {
			Iterator<String> iter = onlineUserlist.keySet().iterator();
			while (iter.hasNext()) {
				String un = iter.next();
				onlineUserlist.get(un).sendPacket(packet);
			}
		} else {
			onlineUserlist.get(username).sendPacket(packet);
		}
	}

	/**
	 * 向除参数用户以外的其他在线用户 发送packet
	 * 
	 * @param username
	 * @param packet
	 */
	public void sendRemoveParamMessage(String username,
			MultiDatagramPacket packet) {
		Iterator<String> iter = onlineUserlist.keySet().iterator();
		while (iter.hasNext()) {
			String un = iter.next();
			if (!un.equals(username)) {
				onlineUserlist.get(un).sendPacket(packet);

			}
		}
	}

	public void removeUser(String username) {
		MultiSocket socket = onlineUserlist.remove(username);
		if (socket != null) {
			socket.close();
		}
	}
	public void renameUser(String newName,String oldName){
		onlineUserlist.put(newName,onlineUserlist.remove(oldName));
	}
	public boolean isOnline(String username){
		return onlineUserlist.containsKey(username);
	}

	public void addMultiSocketListener(MultiSocketListener l) {
		support.addMultiSocketListener(l);
	}

	public void removeMultiSocketListener(MultiSocketListener l) {
		support.removeMultiSocketListener(l);
	}

	public void addMultiSocketListener(MultiSocketListener l,
			MultiSocketEvent.EventType type) {
		support.addMultiSocketListener(l, type);
	}

	public void removeMultiSocketListener(MultiSocketListener l,
			MultiSocketEvent.EventType type) {
		support.removeMultiSocketListener(l, type);
	}
}
