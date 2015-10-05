package multisocket.test;

import java.io.IOException;

import multisocket.event.MultiSocketAdapter;
import multisocket.event.MultiSocketEvent;
import multisocket.net.ConfigFiles;
import multisocket.net.MultiDatagramPacket;
import multisocket.net.MultiServerSocket;
import multisocket.net.MultiSocket;
/**
 * test MultiServerSocket.java
 *
 */

public class TestServer2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final MultiServerSocket server = new MultiServerSocket(
					ConfigFiles.SERVER_CONFIG);
			server.addMultiSocketListener(new MultiSocketAdapter() {

				@Override
				public void accept(MultiSocketEvent e) {
					// 系统分配新用户-名字，发送packet
					String username = e.getPacket()
							.getEnterExitSendAllocNewName();
					MultiDatagramPacket p1 = MultiDatagramPacket
							.createAllocUserNamePacket(username);
					server.sendMessage(username, p1);
					MultiDatagramPacket p11 = MultiDatagramPacket
							.createSendWhisperMessagePacket(null, username,
									true, "您已进入聊天室，欢迎！ ");
					server.sendMessage(username, p11);
					System.out.println(username);

					// 系统生成在线用户列表，发送packet至新用户
					MultiDatagramPacket p2 = MultiDatagramPacket
							.createOnlineUserListPacket(server
									.getOnlineUserList());
					server.sendMessage(username, p2);

					// 向其他在线用户发送-新用户进入packet
					MultiDatagramPacket p3 = MultiDatagramPacket
							.createEnterPacket(username);
					server.sendRemoveParamMessage(username, p3);
				}

				@Override
				public void read(MultiSocketEvent e) {
					MultiDatagramPacket packet = e.getPacket();
					if (packet.getPacketType() == MultiDatagramPacket.USER_RENAME) {
						String newName = packet.getEnterExitSendAllocNewName();
						if (newName.startsWith("Guest ")) {
							MultiDatagramPacket p1 = MultiDatagramPacket
									.createSendWhisperMessagePacket(null,
											packet.getReceiveOldName(), true,
											newName + " 不能以Guest开头！ ");
							server.sendMessage(packet.getReceiveOldName(), p1);
						}else if(server.isOnline(newName)){
							MultiDatagramPacket p1 = MultiDatagramPacket
									.createSendWhisperMessagePacket(null,
											packet.getReceiveOldName(), true,
											newName + " 已存在，请重新改名！ ");
							server.sendMessage(packet.getReceiveOldName(), p1);
						}else if(newName.equals(packet.getReceiveOldName())){
							MultiDatagramPacket p1 = MultiDatagramPacket
									.createSendWhisperMessagePacket(null,
											packet.getReceiveOldName(), true,
											newName + " 新名字必须与原名有区别！ ");
							server.sendMessage(packet.getReceiveOldName(), p1);
						}else{
							server.renameUser(newName, packet.getReceiveOldName());
							server.sendMessage(null, packet);
						}
						
					}else if (packet.getPacketType() == MultiDatagramPacket.SEND_MESSAGE){
						String send = packet.getEnterExitSendAllocNewName();
						String receive = packet.getReceiveOldName();
						boolean isWhisper = packet.isWhisper();
						//发送给接收方
						if(receive == null){
							server.sendMessage(null, packet);
						}else{
							if(isWhisper){
								server.sendMessage(send, packet);
								server.sendMessage(receive, packet);
							}else{
								server.sendMessage(null, packet);
							}
						}
					}
				}

				@Override
				public void close(MultiSocketEvent e) {
					MultiSocket clientSocket = e.getSocket();
					String username = clientSocket.getUsername();
					server.removeUser(username);
					// 向在线用户发送-用户离开packet
					MultiDatagramPacket p3 = MultiDatagramPacket
							.createExitPacket(username);
					server.sendMessage(null, p3);
				}

			});
			server.start();

			try {
				Thread.sleep(600000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
