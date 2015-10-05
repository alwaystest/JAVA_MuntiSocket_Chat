package multisocket.test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import multisocket.net.MultiDatagramPacket;
/**
 * send packets to client. find if client can accept and handle them.
 *
 */
public class TServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(6666);
		System.out.println("server......");
		Socket socket = server.accept();
		System.out.println("ok......");
		
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
	
		System.out.println("stream ok ....");
		
		Thread.sleep(1000);
		MultiDatagramPacket packet2 = MultiDatagramPacket.createAllocUserNamePacket("Guest 1");
		out.writeObject(packet2);
		out.flush();
		
		
		Thread.sleep(1000);
		MultiDatagramPacket packet4 = MultiDatagramPacket.createOnlineUserListPacket(new String[]{"Guest 1","eric","tom","abc"});
		out.writeObject(packet4);
		out.flush();
		
		Thread.sleep(1000);
		MultiDatagramPacket packet7 = MultiDatagramPacket.createUserRenamePacket("ha ha ha", "eric");
		out.writeObject(packet7);
		out.flush();
		
		Thread.sleep(1000);
		MultiDatagramPacket packet71 = MultiDatagramPacket.createUserRenamePacket("Rename Guest 1", "Guest 1");
		out.writeObject(packet71);
		out.flush();
		

		Thread.sleep(1000);
		MultiDatagramPacket packet3 = MultiDatagramPacket.createExitPacket("tom");
		out.writeObject(packet3);
		out.flush();
		
		Thread.sleep(2000);
		MultiDatagramPacket packet1 = MultiDatagramPacket.createEnterPacket("zhao qian");
		out.writeObject(packet1);
		out.flush();
		

		

		
		Thread.sleep(1000);
		MultiDatagramPacket packet6 = MultiDatagramPacket.createSendWhisperMessagePacket("eric", "abc", true,"你好");
		out.writeObject(packet6);
		out.flush();

		Thread.sleep(1000);
		MultiDatagramPacket packet8 = MultiDatagramPacket.createSendWhisperMessagePacket(null,null, true,"文明用语");
		out.writeObject(packet8);
		out.flush();
		
		Thread.sleep(1000);
		MultiDatagramPacket packet9 = MultiDatagramPacket.createSendWhisperMessagePacket(null,"abc", false,"system-abc-false");
		out.writeObject(packet9);
		out.flush();

		Thread.sleep(1000);
		MultiDatagramPacket packet10 = MultiDatagramPacket.createSendWhisperMessagePacket(null,"abc", true,"system-abc-true");
		out.writeObject(packet10);
		out.flush();
		
		Thread.sleep(1000);
		MultiDatagramPacket packet11 = MultiDatagramPacket.createSendWhisperMessagePacket("abc",null, true,"abc-system-true");
		out.writeObject(packet11);
		out.flush();
		
		
		Thread.sleep(1000);
		MultiDatagramPacket packet12 = MultiDatagramPacket.createSendWhisperMessagePacket("abc",null, false,"abc-system-false");
		out.writeObject(packet12);
		out.flush();
		
		//---------
		
//		Thread.sleep(1000);
//		MultiDatagramPacket packet5 = MultiDatagramPacket.createSendMessagePacket("sendUser", "message");
//		out.writeObject(packet5);
//		out.flush();
		

		

		
		
		Thread.sleep(200000);
	}

}
