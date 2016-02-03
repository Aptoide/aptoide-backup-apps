package pt.aptoide.backupapps.download.state;


import pt.aptoide.backupapps.download.DownloadInfo;
import pt.aptoide.backupapps.download.event.BusProvider;
import pt.aptoide.backupapps.download.event.DownloadStatusEvent;

/**
 * A StatusState is a state in which a {@link DownloadInfo} can be and helps to perform some status specific actions.
 * @author Edward Larsson (edward.larsson@gmx.com)
 */
public abstract class StatusState {

	/** The download object this state is associated with. */
	protected DownloadInfo mDownloadInfo;

	/**
	 * Construct a status state.
	 * @param downloadObject The downloadObject associated with this state.
	 */
	protected StatusState(DownloadInfo downloadObject) {
        mDownloadInfo = downloadObject;
	}

	/**
	 * @return a shallow copy of this status state.
	 */
	public abstract StatusState getShallowCopy();

	/**
	 * @return The download object wrapped in this statusState.
	 */
	public DownloadInfo getDownloadObject() {
		return mDownloadInfo;
	}

	/**
	 * Try to start downloading.
	 */
	public abstract void download();

	/**
	 * Open the file that is at the destination of the wrapped download object.
	 */
	public void openFile() {
//		File file = new File(mDownloadObject.getDestination());
//		if (Desktop.isDesktopSupported() && file.exists()) {
//			try {
//				Desktop.getDesktop().open(file);
//			} catch (IOException ex) {
//				DownloadLogger.LOGGER.log(Level.WARNING, ex.toString());
//			}
//		}
	}

	/**
	 * Try to pause downloading.
	 */
	public abstract void pause();

	/**
	 * Try to change a download object's state from this status state to another.
	 * @param state The status state to change to.
	 */
	public void changeTo(StatusState state) {
//        BusProvider.getInstance().post(new DownloadStatusEvent());
		if (state.changeTo()) {
			changeFrom();
//			DownloadStatusStateEvent event = new DownloadStatusStateEvent(mDownloadObject);
//			mDownloadObject.notifyListeners(event);
            mDownloadInfo = null;
		}
	}

	/**
	 * Change from this state.
	 */
	public abstract void changeFrom();

	/**
	 * Change to this state.
	 * @return <tt>true</tt> the change was successful, <tt>false</tt> otherwise.
	 */
	public abstract boolean changeTo();

	/**
	 * Remove a download object from the download manager.
	 */
	public void remove() {
		changeFrom();
	}

	/**
	 * @return The download object's queue position, or an empty String if the download is not queuing.
	 */
	public abstract int getQueuePosition();


    public abstract EnumState getEnumState();
}
