package multisocket.test;

import multisocket.net.ConfigFiles;
import multisocket.net.MultiSocket;

public class TestConfigFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MultiSocket socket = new MultiSocket(ConfigFiles.SERVER_CONFIG);
			System.out.println("ok");
			socket.connect();
			System.out.println("connect ok");
			
			Thread.sleep(5000);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
