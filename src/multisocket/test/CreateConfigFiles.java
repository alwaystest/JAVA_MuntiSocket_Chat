package multisocket.test;

import java.io.FileOutputStream;
import java.util.Properties;
/**
 * for generate config files.
 *
 */
public class CreateConfigFiles {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		Properties server = new Properties();
		server.setProperty("serverIp","127.0.0.1");
		server.setProperty("serverPort", "6666");
		
		Properties client = new Properties();
		client.setProperty("serverIp","127.0.0.1");
		client.setProperty("serverPort", "6666");
		client.setProperty("clientIp","");
		client.setProperty("clientPort", "-1");
		
		server.storeToXML(new FileOutputStream("config/server.xml"),"");
		client.storeToXML(new FileOutputStream("config/client.xml"),"");	
	}

}
