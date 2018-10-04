package pt.aptoide.backupapps.download.state;

import pt.aptoide.backupapps.download.DownloadInfo;
import pt.aptoide.backupapps.download.DownloadManager;

/**
 * The pending state represents the status of a download object waiting to download.
 *
 * @author Edward Larsson (edward.larsson@gmx.com)
 */
public class PendingState extends StatusState {

  /**
   * Construct a pending state.
   *
   * @param downloadInfo The downloadInfo associated with this state.
   */
  public PendingState(DownloadInfo downloadInfo) {
    super(downloadInfo);
  }

  @Override public StatusState getShallowCopy() {
    return new PendingState(null);
  }

  @Override public void download() {
    //do nothing, in pending mode already.
  }

  @Override public void pause() {
    mDownloadInfo.changeStatusState(new InactiveState(mDownloadInfo));
  }

  @Override public void changeFrom() {
    DownloadManager.INSTANCE.removeFromPendingList(mDownloadInfo);
  }

  @Override public boolean changeTo() {
    if (DownloadManager.INSTANCE.addToPendingList(mDownloadInfo)) {
      mDownloadInfo.setStatusState(this);
      return true;
    }

    return false;
  }

  @Override public int getQueuePosition() {
    return 0;  //To change body of implemented methods use File | Settings | File Templates.
  }

  //	@Override
  //	public int getQueuePosition() {
  //		return DownloadManager.INSTANCE.getPendingQueuePosition(mDownloadInfo);
  //	}

  @Override public EnumState getEnumState() {
    return EnumState.PENDING;
  }
}
