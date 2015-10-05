package multisocket.test;

import java.io.IOException;

import multisocket.event.MultiSocketAdapter;
import multisocket.event.MultiSocketEvent;
import multisocket.net.ConfigFiles;
import multisocket.net.MultiSocket;
/**
 * test if MultiSocketListener can handle events correctly.
 *
 */
public class TSocket {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MultiSocket socket = new MultiSocket(ConfigFiles.SERVER_CONFIG);
			System.out.println("add listener");
			socket.addMultiSocketListener(new MultiSocketAdapter() {
				@Override
				public void connect(MultiSocketEvent e) {
					System.out.println("client connect ok....");
				}
				@Override
				public void read(MultiSocketEvent e) {
					System.out.println("read packet ok....");
				}
				
			});
			socket.addMultiSocketListener(new MultiSocketAdapter() {
				@Override
				public void connect(MultiSocketEvent e) {
					System.out.println("client connect ok---------------");
				}
			}, MultiSocketEvent.EventType.CONNECT);
			System.out.println("connecting...");
			socket.connect();
			
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

}
