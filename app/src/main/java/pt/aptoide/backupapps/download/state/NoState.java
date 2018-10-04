package pt.aptoide.backupapps.download.state;

import pt.aptoide.backupapps.download.DownloadInfo;
import pt.aptoide.backupapps.download.DownloadManager;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 09-07-2013
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public class NoState extends StatusState {
  /**
   * Construct a status state.
   *
   * @param downloadObject The downloadObject associated with this state.
   */
  public NoState(DownloadInfo downloadObject) {
    super(downloadObject);
  }

  @Override public StatusState getShallowCopy() {
    return new InactiveState(null);
  }

  @Override public void download() {
    mDownloadInfo.changeStatusState(new ActiveState(mDownloadInfo));
  }

  @Override public void pause() {
    //do nothing, already inactive
  }

  @Override public void changeFrom() {
    DownloadManager.INSTANCE.removeFromInactiveList(mDownloadInfo);
  }

  @Override public boolean changeTo() {
    if (DownloadManager.INSTANCE.addToInactiveList(mDownloadInfo)) {
      mDownloadInfo.setStatusState(this);
      return true;
    }

    return false;
  }

  @Override public int getQueuePosition() {
    return Integer.MAX_VALUE;
  }

  @Override public EnumState getEnumState() {
    return EnumState.NOSTATE;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
