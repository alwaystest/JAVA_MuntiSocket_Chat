package multisocket.event;

public abstract class MultiSocketAdapter implements MultiSocketListener {

	/* (non-Javadoc)
	 * @see multisocket.event.MultiSocketListener#accept(multisocket.event.MultiSocketEvent)
	 */
	@Override
	public void accept(MultiSocketEvent e) {

	}

	/* (non-Javadoc)
	 * @see multisocket.event.MultiSocketListener#connect(multisocket.event.MultiSocketEvent)
	 */
	@Override
	public void connect(MultiSocketEvent e) {

	}

	/* (non-Javadoc)
	 * @see multisocket.event.MultiSocketListener#read(multisocket.event.MultiSocketEvent)
	 */
	@Override
	public void read(MultiSocketEvent e) {

	}

	/* (non-Javadoc)
	 * @see multisocket.event.MultiSocketListener#write(multisocket.event.MultiSocketEvent)
	 */
	@Override
	public void write(MultiSocketEvent e) {

	}

	/* (non-Javadoc)
	 * @see multisocket.event.MultiSocketListener#close(multisocket.event.MultiSocketEvent)
	 */
	@Override
	public void close(MultiSocketEvent e) {

	}

}
