package pt.aptoide.backupapps.download.state;

import pt.aptoide.backupapps.download.DownloadInfo;
import pt.aptoide.backupapps.download.DownloadManager;

/**
 * The active state represents the status of a download object in the process of downloading.
 *
 * @author Edward Larsson (edward.larsson@gmx.com)
 */
public class ActiveState extends StatusState {

  /**
   * Construct an active state.
   *
   * @param downloadInfo The downloadInfo associated with this state.
   */
  public ActiveState(DownloadInfo downloadInfo) {
    super(downloadInfo);
  }

  @Override public StatusState getShallowCopy() {
    return new ActiveState(null);
  }

  @Override public void download() {
    //do nothing, already active
  }

  @Override public void openFile() {
    // do nothing, can't open a file that's being written to.
  }

  @Override public void pause() {
    mDownloadInfo.changeStatusState(new InactiveState(mDownloadInfo));
  }

  @Override public void changeFrom() {
    DownloadManager.INSTANCE.removeFromActiveList(mDownloadInfo);
  }

  @Override public boolean changeTo() {
    if (DownloadManager.INSTANCE.addToActiveList(mDownloadInfo)) {
      // Set the status state before starting new thread because the while loop in the run method
      // depends on the status state being active.
      //            Toast.makeText(ApplicationAptoide.getContext(), ApplicationAptoide.getContext().getString(R.string.starting_download), Toast.LENGTH_LONG).show();
      mDownloadInfo.setStatusState(this);
      Thread t = new Thread(mDownloadInfo);
      t.start();
      return true;
    }

    mDownloadInfo.changeStatusState(new PendingState(mDownloadInfo));
    return false;
  }

  //	@Override
  //	public int getQueuePosition() {
  //		return DownloadManager.INSTANCE.getActiveQueuePosition(mDownloadInfo);
  //	}

  @Override public int getQueuePosition() {
    return 0;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override public EnumState getEnumState() {
    return EnumState.ACTIVE;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
