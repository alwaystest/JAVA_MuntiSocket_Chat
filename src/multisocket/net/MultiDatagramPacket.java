package multisocket.net;

import java.io.Serializable;
import java.util.Arrays;
/**
 * object of packets
 * pass data between server and client
 *
 */

public class MultiDatagramPacket implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int USER_ENTER = 1;
	public static final int USER_EXIT = 2;
	public static final int USER_RENAME = 3;
	public static final int SEND_MESSAGE = 4;
	public static final int ONLINE_USERLIST = 5;
	public static final int ALLOC_USERNAME = 6;

	/**
	 * message type
	 */
	private int packetType;
	private String enterExitSendAllocNewName;
	private String receiveOldName;
	private boolean whisper;
	private String[] userlist;
	private String message;

	/**
	 * @param packetType
	 * @param enterExitSendAllocNewName
	 * @param receiveOldName
	 * @param whisper
	 * @param userlist
	 * @param message
	 */
	private MultiDatagramPacket(int packetType,
			String enterExitSendAllocNewName, String receiveOldName,
			boolean whisper, String[] userlist, String message) {
		super();
		this.packetType = packetType;
		this.enterExitSendAllocNewName = enterExitSendAllocNewName;
		this.receiveOldName = receiveOldName;
		this.whisper = whisper;
		this.userlist = userlist;
		this.message = message;
	}

	/**
	 * create a user_enter package
	 * 
	 * @param enterUserName
	 * @return
	 */
	public static MultiDatagramPacket createEnterPacket(
			String enterExitSendAllocNewName) {
		return new MultiDatagramPacket(USER_ENTER, enterExitSendAllocNewName,
				null, false, null, null);
	}

	/**
	 * create a user_exit package
	 * 
	 * @param exitUserName
	 * @return
	 */
	public static MultiDatagramPacket createExitPacket(
			String enterExitSendAllocNewName) {
		return new MultiDatagramPacket(USER_EXIT, enterExitSendAllocNewName,
				null, false, null, null);
	}

	/**
	 * create a user_rename package
	 * 
	 * @param newUserName
	 * @param OldUserName
	 * @return
	 */
	public static MultiDatagramPacket createUserRenamePacket(
			String enterExitSendAllocNewName, String receiveOldName) {
		return new MultiDatagramPacket(USER_RENAME, enterExitSendAllocNewName,
				receiveOldName, false, null, null);
	}

	/**
	 * create a broad_cast message
	 * 
	 * @param sendUserName
	 * @param message
	 * @return
	 */
	public static MultiDatagramPacket createSendMessagePacket(
			String enterExitSendAllocNewName, String message) {
		return new MultiDatagramPacket(SEND_MESSAGE, enterExitSendAllocNewName,
				null, false, null, message);
	}

	/**
	 * create a whisper message
	 * 
	 * @param sendUserName
	 * @param receiveUserName
	 * @param message
	 * @return
	 */
	public static MultiDatagramPacket createSendWhisperMessagePacket(
			String enterExitSendAllocNewName, String receiveOldName,boolean whisper,
			String message) {
		return new MultiDatagramPacket(SEND_MESSAGE, enterExitSendAllocNewName,
				receiveOldName, whisper, null, message);
	}

	/**
	 * create a userList package
	 * 
	 * @param userlist
	 * @return
	 */
	public static MultiDatagramPacket createOnlineUserListPacket(
			String[] userlist) {
		return new MultiDatagramPacket(ONLINE_USERLIST, null, null, false,
				userlist, null);
	}

	/**
	 * create a alloc name package
	 * 
	 * @param AllocName
	 * @return
	 */
	public static MultiDatagramPacket createAllocUserNamePacket(
			String enterExitSendAllocNewName) {
		return new MultiDatagramPacket(ALLOC_USERNAME,
				enterExitSendAllocNewName, null, false, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MultiDatagramPacket [packetType=" + packetType
				+ ", enterExitSendAllocNewName=" + enterExitSendAllocNewName
				+ ", receiveOldName=" + receiveOldName + ", whisper=" + whisper
				+ ", userlist=" + Arrays.toString(userlist) + ", message="
				+ message + "]";
	}

	/**
	 * @return the packetType
	 */
	public int getPacketType() {
		return packetType;
	}

	/**
	 * @return the enterExitSendAllocNewName
	 */
	public String getEnterExitSendAllocNewName() {
		return enterExitSendAllocNewName;
	}

	/**
	 * @return the receiveOldName
	 */
	public String getReceiveOldName() {
		return receiveOldName;
	}

	/**
	 * @return the whisper
	 */
	public boolean isWhisper() {
		return whisper;
	}

	/**
	 * @return the userList
	 */
	public String[] getUserlist() {
		return userlist;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

}
